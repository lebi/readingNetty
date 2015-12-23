package aio.time.server;

public class MainServer {
	public static void main(String[] arg){
		new Thread(new AsyncTimeHandle("127.0.0.1",8000)).start();
	}
}
