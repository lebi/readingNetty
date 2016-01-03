package netty.msgpack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import netty.echoobj.model.User;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

public class MsgpackEncoder extends  MessageToByteEncoder<User>{
	
	public static void main(String[] args){
		User u=new User();
		u.setAge(18);
		MessagePack pack=new MessagePack();
		try {
			byte[] raw=pack.write(u);
			User user=pack.read(raw, User.class);
			System.out.println(u.getAge());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out)
			throws Exception {
		
		System.out.println(msg.getAge());
		MessagePack pack=new MessagePack();
		byte[] raw=pack.write(new User());
		System.out.println(raw);
		out.writeBytes(raw);
		
	}

}
