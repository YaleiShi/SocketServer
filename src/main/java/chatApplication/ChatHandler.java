package chatApplication;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import serverFrame.BasicHandler;
import serverFrame.HttpConstants;
import serverFrame.HttpRequest;
import serverFrame.HttpResponse;

public class ChatHandler extends BasicHandler{
	private String token;
	private String channel;
	private String url;
	
	public ChatHandler(String token, String channel, String url) {
		this.token = token;
		this.channel = channel;
		this.url = url;
	}
	
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		PrintWriter writer = okStatus(response);
		writer.write(simpleHeader("GetSlackbot"));
		String page = simpleForm("slackbot", "message");
		writer.write(page);
		writer.write(simpleFooter());
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		
		PrintWriter writer = okStatus(response);
		if(!checkParam("message", request, response)) {
			return;
		}
		String message = request.getParam("message");
		@SuppressWarnings("deprecation")
		String body = "token=" + token + "&channel=" + channel + "&text=" + URLEncoder.encode(message);
		URL url;
		try {
			url = new URL(this.url);
			
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
			
//			printHeaders(connection);
			printBody(connection);
			
			output.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//write the page
		writer.write(simpleHeader("PostSlackbot"));
		writer.write("<p>Message Sent</p>");
		writer.write(simpleForm("slackbot", "message"));
		writer.write(simpleFooter());
		
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
