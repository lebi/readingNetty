package netty.echo.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter{
	int n=0;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0;i<10;i++){
			ctx.writeAndFlush(Unpooled.copiedBuffer("welcome to netty.$_".getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("times:"+ ++n);
		System.out.println("receive from server:"+msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
