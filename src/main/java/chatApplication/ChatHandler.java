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

/**
 * the chat handler
 * extend basic handler
 * receive the request with the path of /slackbot
 * then handle the request
 * @author yalei
 *
 */
public class ChatHandler extends BasicHandler{
	private String token;
	private String channel;
	private String url;
	private static boolean ifSent;
	private static final String KEY = "message";
	
	/**
	 * need token and channel and url, which come
	 * @param token
	 * @param channel
	 * @param url
	 */
	public ChatHandler(String token, String channel, String url) {
		this.token = token;
		this.channel = channel;
		this.url = url;
		this.ifSent = false;
	}
	
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		PrintWriter writer = okStatus(response);
		writer.write(simpleHeader("GetSlackbot"));
		String page = simpleForm("slackbot", KEY);
		writer.write(page);
		writer.write(simpleFooter());
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		
		PrintWriter writer = okStatus(response);
		if(!checkParam(KEY, request, response)) {
			return;
		}
		String message = request.getParam(KEY);
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
			
			printHeaders(connection);
			printBody(connection);
			
			output.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			ifSent = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ifSent = false;
			e.printStackTrace();
		}
		
		//write the page
		writer.write(simpleHeader("PostSlackbot"));
		if(ifSent) {
			writer.write("<p>Message Sent</p>");
		}else {
			writer.write("<p>Message Not Sent: Some Error Happen</p>");
		}
		writer.write(simpleForm("slackbot", "message"));
		writer.write(simpleFooter());
		
	}
	
	public static void printHeaders(URLConnection connection) {
		Map<String,List<String>> headers = connection.getHeaderFields();
		ifSent = false;
		for(String key: headers.keySet()) {
			System.out.println("key: " + key);
			List<String> values = headers.get(key);
			for(String value: values) {
				System.out.println(value);
				System.out.println("\t" + value);
			}
		}		
	}

	public static void printBody(URLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String line;
		while((line = reader.readLine()) != null) {
			if(line.contains("\"ok\":true")) {
				ifSent = true;
			}
			System.out.println(line);
		} 
	}
	
	public static boolean ifSent() {
		return ifSent;
	}

}
