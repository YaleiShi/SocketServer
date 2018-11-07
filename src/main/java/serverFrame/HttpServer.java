package serverFrame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
	private ConcurrentHashMap<String, Handler> handleMap;
	private ServerSocket server;
	private boolean running;
	private final int SIZE = 10;
	private int port;
	private ExecutorService pool;
	
	public HttpServer(int port) {
		this.port = port;
		this.running = true;
		this.handleMap = new ConcurrentHashMap<String, Handler>();
		this.pool = Executors.newFixedThreadPool(SIZE);
	}
	
	public void addHandler(String key, Handler handler) {
		this.handleMap.put(key, handler);
	}
	
	public void start() {
		server = null;
		
		//start the server socket
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check if the server is started
		if(server == null) {
			System.out.println("start server failed");
			return;
		}
		
		System.out.println("server start");
		
		//keep receiving request
		while(running) {
			try{
				Socket sock = server.accept();
				this.pool.execute(new SocketWorker(sock, handleMap));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
