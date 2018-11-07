package systemTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import serverFrame.HttpConstants;

public class TestSlackAPP extends HtmlTest{
	private final static int PORT = 9090;
	private final static String PATH = "/slackbot";
	
	/**
	 * valid url
	 */
	@Test
	public void testValidGet() {
		String response = TestHelper.getHttpResponse(URL, GET, PATH, PORT, "");
		assertTrue(response.startsWith(HttpConstants.OK_HEADER));
	}
	
	/**
	 * test have a form
	 */
	@Test
	public void testIfHaveForm() {
		String html = TestHelper.getHttpResponse(URL, GET, PATH, PORT, "");
		assertTrue(html.contains("<form"));
	}
	
	/**
	 * test valid input of post
	 * input is not encode
	 */
	@Test
	public void testValidPost() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "message=no sleep tonight");
		assertTrue(html.startsWith(HttpConstants.OK_HEADER));
	}
	
	/**
	 * test valid input of post
	 * input is not encode
	 */
	@Test
	public void testValidPostEncode() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "message=no+sleep+tonight");
		assertTrue(html.startsWith(HttpConstants.OK_HEADER));
	}
	
	/**
	 * test post page have a table
	 */
	@Test
	public void testIfHaveTable() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "message=still fighting");
		assertTrue(html.contains("Message Sent"));
	}
	
	/**
	 * test no query
	 */
	@Test
	public void testNoQuery() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "aaaaa");
		assertTrue(html.contains("Not Enough Parameters"));
	}
	
	/**
	 * test unused query
	 */
	@Test
	public void testQueryFormatWrong() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "aaaaa=bbbb");
		assertTrue(html.contains("Not Enough Parameters"));
	}
	
}
