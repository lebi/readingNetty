package netty.marsh.CandS;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodeCFactory {
	public static MarshallingDecoder buildDecoder(){
		final MarshallerFactory factory=Marshalling.getMarshallerFactory("serial",Thread.class.getClassLoader());
		final MarshallingConfiguration config=new MarshallingConfiguration();
		config.setVersion(5);
		UnmarshallerProvider provider=new DefaultUnmarshallerProvider(factory, config);
		return new MarshallingDecoder(provider,1024);
	}
	
	public static MarshallingEncoder buildEncode(){
		final MarshallerFactory factory=Marshalling.getMarshallerFactory("serial", Thread.class.getClassLoader());
		final MarshallingConfiguration config=new MarshallingConfiguration();
		config.setVersion(5);
		MarshallerProvider provider=new DefaultMarshallerProvider(factory, config);
		return new MarshallingEncoder(provider);
	}
}
