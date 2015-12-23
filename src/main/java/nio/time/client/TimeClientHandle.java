package nio.time.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandle implements Runnable{
	private String host;
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;
	private volatile boolean stop;
	
	public TimeClientHandle(String host,int port){
		this.host=host;
		this.port=port;
		try{
			selector=Selector.open();
			socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
		}catch(Exception e){
			
		}
	}
	
	public void run() {
		try {
			doConnect();
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
						handleInput(key);
					}catch(Exception e){
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
	}
	
	private void handleInput(SelectionKey key) throws ClosedChannelException, IOException{
		if(key.isValid()){
			SocketChannel sc=(SocketChannel)key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				}
			}
			
			if(key.isReadable()){
				ByteBuffer readBuffer=ByteBuffer.allocate(1024);
				int readBytes=sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes=new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body=new String(bytes,"UTF-8");
					System.out.println("now time:"+body);
					this.stop=true;
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}
			}
		}
	}
	
	private void doConnect() throws ClosedChannelException, IOException{
		if(socketChannel.connect(new InetSocketAddress(host, port))){
			socketChannel.register(selector, SelectionKey.OP_READ);
		}else
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}
	
	private void doWrite(SocketChannel sc) throws IOException{
		byte[] req="QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer=ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		sc.write(writeBuffer);
		if(!writeBuffer.hasRemaining())
			System.out.println("all send");
	}
}
