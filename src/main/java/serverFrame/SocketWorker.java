package serverFrame;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketWorker implements Runnable{
	private Socket sock;
	private ConcurrentHashMap<String, Handler> handleMap;
	private static final String CONTENT_HEADER = "Content-Length:";
	private static boolean passed;
	private HttpRequest webRequest;
	private HttpResponse response;
	
	public SocketWorker(Socket sock, ConcurrentHashMap<String, Handler> map) {
		this.sock = sock;
		this.handleMap = map;
		passed = true;
		this.webRequest = new HttpRequest();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try(InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)){
			
			Handler handler = null;
			String line = oneLine(instream);
			String request = line;
			
			System.out.println("--------------------\n" + request);
			
			checkErrorExit(request, writer);
	
			if(passed) {
				handler = this.handleMap.get(webRequest.getPath());
			}
			
			int length = -1;
			while(line != null && !line.trim().isEmpty()) {
				line = oneLine(instream);
				System.out.println(line);
				if(line.startsWith(CONTENT_HEADER)) {
					String[] ss = line.split(":");
					if(ss.length != 2) {
						continue;
					}
					length = Integer.parseInt(ss[1].trim());
				}
			}
			
			if(length != -1) {
				readBody(length);
			}
			
			//handle the request	
			if(passed) {
				this.response = new HttpResponse(writer);
				handler.handle(webRequest, response);
			}
			
			//end the sock
			writer.flush();
			sock.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkErrorExit(String request, PrintWriter writer) {
		//check if GET or POST
		if(!request.startsWith("GET") && !request.startsWith("POST")) {
			writer.write(HttpConstants.METHOD_NOT_ALLOWED);
			passed = false;
		}
		
		//check args and build request
		String[] args = request.split(" ");
		if(!webRequest.buildRequest(args)) {
			writer.write(HttpConstants.BAD_REQUEST);
			passed = false;
		}
		
		//check handlers have the path
		if(!this.handleMap.containsKey(webRequest.getPath())) {
			writer.write(HttpConstants.NOT_FOUND);
			passed = false;
		}
	}
	
	private void readBody(int length) throws IOException {
		byte[] bytes = new byte[length];
		int read = sock.getInputStream().read(bytes);
		
		while(read < length) {
			read += sock.getInputStream().read(bytes, read, (bytes.length-read));
		}
		System.out.println("\n" + new String(bytes));
		webRequest.addParams(new String(bytes));	
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
		while(b != '\n' && b != -1) {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}

}
