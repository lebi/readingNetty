package netty.time.server;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerCountHandler extends ChannelHandlerAdapter{
	private int count;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body=(String)msg;
		System.out.println("server receive order:"+body+";count:"+ ++count);
		String current="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD REQUEST";
		current=current+System.getProperty("line.separator");
		ByteBuf resp=Unpooled.copiedBuffer(current.getBytes());
		ctx.writeAndFlush(resp);
	}
	
	
}
