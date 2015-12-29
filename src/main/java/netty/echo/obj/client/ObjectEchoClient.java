package netty.echo.obj.client;

import netty.msgpack.MsgpackDecoder;
import netty.msgpack.MsgpackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ObjectEchoClient {
	public void connect(int port,String host) throws InterruptedException{
		EventLoopGroup group=new NioEventLoopGroup();
		try{
			Bootstrap boot=new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
						ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
						ch.pipeline().addLast(new ObjectClientHandler());
					}
				});
			ChannelFuture f=boot.connect(host, port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args){
		try {
			new ObjectEchoClient().connect(8000, "127.0.0.1");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
