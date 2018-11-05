import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ChatHandler extends BasicHandler{
	private final String TOKEN = "xoxp-378520430422-422686698807-473070785030-a5b0d935fcb997054f9f1907cfaecf86";
	private final String CHANEL = "#project3";
	private final String URL = "https://slack.com/api/chat.postMessage";
	
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("GetSlackbot"));
		String page = "<form action=\"slackbot\" method=\"post\">" +
			    "Message: "+
			    "<input type=\"text\" name=\"message\"> "+
			    "<input type=\"submit\" value=\"Submit\"></form><hr>"+
				"</body></html>";
		writer.write(page);
		writer.flush();
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		String message = request.getParam("message");
		String body = "token=" + TOKEN + "&channel=" + CHANEL + "&text=" + message;
		URL url;
		try {
			url = new URL(URL);
			
			//create secure connection 
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			
			//set HTTP method
			connection.setRequestMethod("POST");	
			connection.setRequestProperty("Content-length", String.valueOf(body.length()));
			connection.setDoOutput(true);
			connection.connect();
			
			//write the body
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			output.writeBytes(body);
			
			printHeaders(connection);
			printBody(connection);
			
			output.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("GetSlackbot"));
		String page = "<p>Message Already Sent</p>"
				+ "<form action=\"slackbot\" method=\"post\">" +
			    "Message: "+
			    "<input type=\"text\" name=\"message\"> "+
			    "<input type=\"submit\" value=\"Submit\"></form><hr>"+
				"</body></html>";
		writer.write(page);
		writer.flush();
		
	}
	
	public static void printHeaders(URLConnection connection) {
		Map<String,List<String>> headers = connection.getHeaderFields();
		for(String key: headers.keySet()) {
			System.out.println(key);
			List<String> values = headers.get(key);
			for(String value: values) {
				System.out.println("\t" + value);
			}
		}		
	}

	public static void printBody(URLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String line;
		while((line = reader.readLine()) != null) {
			System.out.println(line);
		} 
	}

}
