package nio.time.server;

public class TimeServer {
	public static void main(String[] args){
		int port=8000;
		MultiplexerTimerServer timeServer=new MultiplexerTimerServer(port);
		new Thread(timeServer).start();
	}
}
