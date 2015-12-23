package aio.time.server;

import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeHandle implements Runnable{
	CountDownLatch latch;
	AsynchronousServerSocketChannel asocketChannel;
	public AsyncTimeHandle(String address,int port) {
		try{
			asocketChannel=AsynchronousServerSocketChannel.open();
			asocketChannel.bind(new InetSocketAddress(address,port));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		latch=new CountDownLatch(1);
		doAccept();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void doAccept(){
		asocketChannel.accept(this, new AcceptCompletionHandler());
	}

}
