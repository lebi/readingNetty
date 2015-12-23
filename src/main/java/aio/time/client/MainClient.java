package aio.time.client;

public class MainClient {
	public static void main(String[] arg){
		new Thread(new AsyncTimeClient("127.0.0.1", 8000)).start();
	}
}
