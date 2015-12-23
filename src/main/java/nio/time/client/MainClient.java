package nio.time.client;

public class MainClient {
	public static void main(String[] arg){
		new Thread(new TimeClientHandle("127.0.0.1", 8000)).start();
	}
}
