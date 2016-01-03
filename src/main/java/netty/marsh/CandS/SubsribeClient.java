package netty.marsh.CandS;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.echoobj.client.ObjectClientHandler;

public class SubsribeClient {
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
						ch.pipeline().addLast(MarshallingCodeCFactory.buildDecoder());
						ch.pipeline().addLast(MarshallingCodeCFactory.buildEncode());
					    ch.pipeline().addLast(new SubReqClientHandler());
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
			new SubsribeClient().connect(8000, "127.0.0.1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
