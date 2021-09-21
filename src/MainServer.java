import java.io.IOException;

public class MainServer {
	public static void main(String[] args) throws IOException {
		while (true) {
			SocketServer server = new SocketServer();
			server.start(6666);
			server.stop();
		}
	}
}
