package serverFrame;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the runnable socket worker
 * read the request
 * check if the request is valid
 * write the response
 * @author yalei
 *
 */
public class SocketWorker implements Runnable{
	private Socket sock;
	private ConcurrentHashMap<String, Handler> handleMap;
	private static final String CONTENT_HEADER = "Content-Length:";
	private static boolean passed;
	private HttpRequest webRequest;
	private HttpResponse response;
	
	/**
	 * start up the worker by the sock and handle map
	 * @param sock
	 * @param map
	 */
	public SocketWorker(Socket sock, ConcurrentHashMap<String, Handler> map) {
		this.sock = sock;
		this.handleMap = map;
		passed = true;
		this.webRequest = new HttpRequest();
		
	}
	
	/**
	 * open the input stream and output stream
	 * read the request from the input stream
	 * check if the request if valid
	 * return the right status
	 * call the right handler to handle the request
	 */
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
	
	/**
	 * check if the request is valid
	 * change the passed value by it
	 * if passed if false, the sock will not handle the request
	 * @param request
	 * @param writer
	 */
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
	
	/**
	 * read the body of the request
	 * return the string of it
	 * @param length
	 * @throws IOException
	 */
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
	 * Read a line of bytes until \n character or -1.
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
