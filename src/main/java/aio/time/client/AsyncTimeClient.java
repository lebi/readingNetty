package aio.time.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClient implements Runnable,CompletionHandler<Void,AsyncTimeClient>{
	private AsynchronousSocketChannel channel;
	private CountDownLatch latch;
	String address;
	int port;
	
	
	public AsyncTimeClient(String address,int port){
		this.address=address;
		this.port=port;
		try {
			channel=AsynchronousSocketChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void completed(Void result, AsyncTimeClient attachment) {
		System.out.println("connection comlete");
		byte[] req="QUERY TIME ORDER".getBytes();
		ByteBuffer buffer=ByteBuffer.allocate(req.length);
		buffer.put(req);
		buffer.flip();
		channel.write(buffer, buffer, new WriteCompletionHandler());
		
	}


	public void failed(Throwable exc, AsyncTimeClient attachment) {
		exc.printStackTrace();
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		latch.countDown();
		
	}

	
	public void run() {
		latch=new CountDownLatch(1);
		channel.connect(new InetSocketAddress(address, port), this, this);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{

		
		public void completed(Integer arg0, ByteBuffer buffer) {
			if(buffer.hasRemaining())
				channel.write(buffer,buffer, this);
			else {
				ByteBuffer readbuffer=ByteBuffer.allocate(1024);
				channel.read(readbuffer, readbuffer, new ReadCompletionHander());
			}
		}

		
		public void failed(Throwable arg0, ByteBuffer arg1) {
			arg0.printStackTrace();
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			latch.countDown();
			
		}
		
	}
	
	class ReadCompletionHander implements CompletionHandler<Integer, ByteBuffer>{

		
		public void completed(Integer result, ByteBuffer buffer) {
			buffer.flip();
			byte[] bytes=new byte[buffer.remaining()];
			buffer.get(bytes);
			String body=null;
			try {
				body=new String(bytes,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println("now is:"+body);
			latch.countDown();
		}

		
		public void failed(Throwable exc, ByteBuffer attachment) {
			exc.printStackTrace();
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			latch.countDown();
			
		}
		
	}
}
