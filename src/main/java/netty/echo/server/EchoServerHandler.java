package netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter{
	int n=0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body=(String)msg;
		System.out.println("times:"+ ++n);
		System.out.println("receive:"+ body);
		body+="$_";
		ByteBuf echo=Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(echo);
	}
	
}
