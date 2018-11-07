package systemTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TestHelper {
	
	public static String getHttpResponse(String host, String method, String path, int PORT, String body) {
		
		StringBuffer buf = new StringBuffer();
		
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
			) { 

			//send request
			String request = getRequest(method, host, path, body);
			out.write(request.getBytes());
			out.flush();

			//receive response
			String line = reader.readLine();
			while(line != null) {				
				buf.append(line + "\n"); //append the newline stripped by readline
				line = reader.readLine();
			}

		} catch (IOException e) {
			System.out.println("HTTPFetcher::download " + e.getMessage());
		}
		return buf.toString();
	}
	
	public static String getHtml(String s) {
		if(!s.contains("<!DOCTYPE html>")) {
			return "";
		}
		int start = s.indexOf("<html>") + 6;
		int end = s.indexOf("</html>");
		return s.substring(start, end);
	}

	private static String getRequest(String method, String host, String path, String body) {
		String request = method + " " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n"; //make sure the server closes the connection after we fetch one page
		if(method.equals("POST")) {
			request += "Content-Length: " + String.valueOf(body.length()) + "\n";
		}
		request += "\r\n";
		if(method.equals("POST")) {
			request += body;
		}
//		System.out.println(request);
		return request;
	}	
}
