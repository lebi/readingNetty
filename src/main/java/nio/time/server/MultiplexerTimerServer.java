package nio.time.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimerServer implements Runnable{

	private Selector selector;
	private ServerSocketChannel serverChannel;
	private boolean stop;
	
	public MultiplexerTimerServer(int port) {
		try{
			selector=Selector.open();
			serverChannel=ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(port), 1024);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		}catch(Exception e){
			
		}
	}
	
	public void run() {
		while(!stop){
			try {
				selector.select(1000);
				Set<SelectionKey> keys=selector.selectedKeys();
				Iterator<SelectionKey> it=keys.iterator();
				SelectionKey key=null;
				while(it.hasNext()){
					key=it.next();
					it.remove();
					try{
						handle(key);
					}catch(IOException e){
						if(key!=null){
							key.cancel();
							if(key.channel()!=null)
								key.channel().close();
						}
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(selector!=null)
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private void handle(SelectionKey key) throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel channel=(ServerSocketChannel)key.channel();
				SocketChannel sc=channel.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc=(SocketChannel)key.channel();
				ByteBuffer readBuffer=ByteBuffer.allocate(1024);
				int readBytes=sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes=new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body=new String(bytes,"UTF-8");
					System.out.println("server receive:"+body);
					String current="QUERY TIME ORDER".equalsIgnoreCase(body)?
							new Date(System.currentTimeMillis()).toString()
							:"BAD ORDER";
					doWrite(sc,current);
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}
			}
		}
	}
	
	private void doWrite(SocketChannel channel,String str) throws IOException{
		byte[] bytes=str.getBytes();
		ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		channel.write(writeBuffer);
	}

}
