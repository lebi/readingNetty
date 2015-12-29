package netty.echo.obj.server;

import netty.msgpack.MsgpackDecoder;
import netty.msgpack.MsgpackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ObjectEchoServer {
	public void bind(int port) throws InterruptedException{
		EventLoopGroup boss=new NioEventLoopGroup();
		EventLoopGroup worker=new NioEventLoopGroup();
		try{
			ServerBootstrap boot=new ServerBootstrap();
			boot.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1000)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
						ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
						ch.pipeline().addLast(new ObjectServerHandler());
					}
					
				});
			ChannelFuture f=boot.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public static void main(String[] arg){
		try {
			new ObjectEchoServer().bind(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
