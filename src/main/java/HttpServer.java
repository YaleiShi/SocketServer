import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
	private ConcurrentHashMap<String, Handler> handleMap;
	private ServerSocket server;
	private boolean running;
	private int port;
	private ExecutorService pool;
	
	public HttpServer(int port) {
		this.port = port;
		this.running = true;
		this.handleMap = new ConcurrentHashMap<String, Handler>();
		this.pool = Executors.newFixedThreadPool(10);
	}
	
	public void addHandler(String key, Handler handler) {
		this.handleMap.put(key, handler);
	}
	
	public void start() {
		server = null;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(server == null) {
			System.out.println("start server failed");
			return;
		}
		
		while(running) {
			
//			try(	Socket sock = server.accept();
//					InputStream instream = sock.getInputStream();
//					PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)
//					)
			try{
				Socket sock = server.accept();
				System.out.println("socket received");
//				Thread t = new Thread(new SocketWorker(sock, handleMap));
//				t.start();
				this.pool.execute(new SocketWorker(sock, handleMap));
//				String message = "";
//				String line = oneLine(instream);
//				System.out.println(line);
//				
//				int length = 0;
//				while(line != null && !line.trim().isEmpty()) {
//					
//					message += line + "\n";
//					line = oneLine(instream);
//					
//					//TODO: fix this messy hack
//					if(line.startsWith("Content-Length:")) {
//						length = Integer.parseInt(line.split(":")[1].trim());
//					}
//					//1. is this a valid format (key : value)?
//					//2. is the key valid? (constants defined somewhere)
//					//3. is the value valid for the key?							
//				}
//				System.out.println("Request: \n" + message);
//				
//
//				byte[] bytes = new byte[length];
//				int read = sock.getInputStream().read(bytes);
//				
//				while(read < length) {
//					read += sock.getInputStream().read(bytes, read, (bytes.length-read));
//				}
//				
//				System.out.println("Bytes expected: " + length + " Bytes read: " + read);
//				System.out.println("Body: \n" + new String(bytes));
//				
//				
//				//send response to client
//				String headers = "HTTP/1.0 200 OK\n" +
//							     "\r\n";
//				
//				String page = "<html> " + 
//							"<head><title>TEST</title></head>" + 
//						    "<body>This is a short test page.</body>"
//						    + "<form action=\"songsPage\" method=\"post\">"
//						    + "Search type: " + 
//						    "<select name=\"searchType\">" +
//						    "<option value=\"artist\">artist</option>"+
//						    "<option value=\"title\">title</option>"+
//						    "<option value=\"tag\">tag</option>"+
//						    "</select>"+
//						    "Query: "+
//						    "<input type=\"text\" name=\"query\"> "+
//						    "<input type=\"submit\" value=\"Submit\"></form><hr>"+
//							"</html>";
//				
//				writer.write(headers);
//				writer.write(page);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Read a line of bytes until \n character.
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != '\n') {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}
	
}
