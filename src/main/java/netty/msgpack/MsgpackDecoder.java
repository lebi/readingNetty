package netty.msgpack;

import java.util.List;

import netty.echoobj.model.User;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgpackDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		final byte[] arr;
		final int len=msg.readableBytes();
		arr=new byte[len];
		System.out.println(len);
		msg.readBytes(arr,msg.readerIndex() , len);
		MessagePack msgpack=new MessagePack();
		out.add(msgpack.read(arr,User.class));
		System.out.println(msgpack.read(arr,User.class).getAge());
	}

}
