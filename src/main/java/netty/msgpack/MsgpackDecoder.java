package netty.msgpack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		System.out.println("decode");
		final byte[] arr;
		final int len=msg.readableBytes();
		arr=new byte[len];
		msg.getBytes(msg.readerIndex(), arr,0,len);
		MessagePack msgpack=new MessagePack();
		out.add(msgpack.read(arr));
		
	}

}
