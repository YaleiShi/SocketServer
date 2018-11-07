package integrationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import chatApplication.ChatHandler;
import serverFrame.Handler;
import serverFrame.HttpRequest;
import serverFrame.HttpResponse;

public class TestSlackbot {
	private final String Valid_Token = "xoxp-378520430422-422686698807-473070785030-a5b0d935fcb997054f9f1907cfaecf86";
	private final String Invalid_Token = "123456789";
	private final String Valid_Channel = "#project3";
	private final String Invalid_Channel = "lalala";
	private final String URL = "https://slack.com/api/chat.postMessage";
	private final String[] args = {"POST", "/slackbot?message=4:48 not sleep", "http/1.0"};
	
	@Test
	public void testValidRequestToSlack() {
		ChatHandler slackHandle = new ChatHandler(Valid_Token, Valid_Channel, URL);
		HttpRequest request = new HttpRequest();
		request.buildRequest(args);
		try {
			slackHandle.doPost(request, new HttpResponse(new PrintWriter(new FileWriter("test.txt"))));
			assertTrue(slackHandle.ifSent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvalidToken() {
		ChatHandler slackHandle = new ChatHandler(Invalid_Token, Valid_Channel, URL);
		HttpRequest request = new HttpRequest();
		request.buildRequest(args);
		try {
			slackHandle.doPost(request, new HttpResponse(new PrintWriter(new FileWriter("test.txt"))));
			assertTrue(!slackHandle.ifSent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvalidChannel() {
		ChatHandler slackHandle = new ChatHandler(Valid_Token, Invalid_Channel, URL);
		HttpRequest request = new HttpRequest();
		request.buildRequest(args);
		try {
			slackHandle.doPost(request, new HttpResponse(new PrintWriter(new FileWriter("test.txt"))));
			assertTrue(!slackHandle.ifSent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
