package aio.time.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

public class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer>{

	private AsynchronousSocketChannel channel;
	
	
	public ReadCompletionHandler(AsynchronousSocketChannel channel) {
		if(this.channel==null)
			this.channel=channel;
	}
	
	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] body=new byte[attachment.remaining()];
		attachment.get(body);
		try{
			String req=new String(body,"UTF-8");
			System.out.println("server receive:"+req);
			String time="QUERY TIME ORDER".equalsIgnoreCase(req)?
					new Date().toString():"BAD ORDER";
			doWrite(time);
		}catch(Exception e){
			
		}
	}
	
	private void doWrite(String str){
		if(str!=null&&str.length()>0){
			byte[] bytes=str.getBytes();
			ByteBuffer write=ByteBuffer.allocate(bytes.length);
			write.put(bytes);
			write.flip();
			channel.write(write, write, new CompletionHandler<Integer, ByteBuffer>() {

				
				public void completed(Integer result, ByteBuffer attachment) {
					if(attachment.hasRemaining())
						channel.write(attachment, attachment, this);
				}

				
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
