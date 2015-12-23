package aio.time.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeHandle>{

	public void completed(AsynchronousSocketChannel result,
			AsyncTimeHandle attachment) {
		attachment.asocketChannel.accept(attachment, this);
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	public void failed(Throwable exc, AsyncTimeHandle attachment) {
		exc.printStackTrace();
		attachment.latch.countDown();
		
	}

}
