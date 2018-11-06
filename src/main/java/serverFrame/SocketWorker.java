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
	
	public SocketWorker(Socket sock, ConcurrentHashMap<String, Handler> map) {
		this.sock = sock;
		this.handleMap = map;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try(InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)){
			String line = oneLine(instream);
			String request = line;
			System.out.print("--------------------\n" + request);
			//check if GET or POST
			if(!request.startsWith("GET") && !request.startsWith("POST")) {
				writer.write(HttpConstants.METHOD_NOT_ALLOWED);
				return;
			}
			//check args and build request
			String[] args = request.split(" ");
			HttpRequest webRequest = new HttpRequest();
			if(!webRequest.buildRequest(args)) {
				writer.write(HttpConstants.BAD_REQUEST);
				return;
			}
			
			//check handlers have the path
			if(!this.handleMap.containsKey(webRequest.getPath())) {
				writer.write(HttpConstants.NOT_FOUND + "\r\n");
				return;
			}
			Handler handler = this.handleMap.get(webRequest.getPath());
			
			int length = -1;
			while(line != null && !line.trim().isEmpty()) {
				line = oneLine(instream);
				if(line.startsWith("Content-Length:")) {
					String[] ss = line.split(":");
					if(ss.length != 2) {
						continue;
					}
					length = Integer.parseInt(ss[1].trim());
				}
			}
			
			if(length != -1) {
				byte[] bytes = new byte[length];
				int read = sock.getInputStream().read(bytes);
				
				while(read < length) {
					read += sock.getInputStream().read(bytes, read, (bytes.length-read));
				}
				System.out.println("body: " + new String(bytes));
				webRequest.addParams(new String(bytes));
			}
			
			//handle the request
			HttpResponse response = new HttpResponse(writer);
			handler.handle(webRequest, response);
			
			//end the sock
			writer.flush();
			writer.close();
			sock.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
