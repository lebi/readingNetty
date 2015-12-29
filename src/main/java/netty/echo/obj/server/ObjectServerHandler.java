package netty.echo.obj.server;

import netty.echo.obj.model.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ObjectServerHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		User user=(User)msg;
		System.out.println(user.getAge());
		System.out.println(user.getName());
		ctx.writeAndFlush(msg);
	}

}
