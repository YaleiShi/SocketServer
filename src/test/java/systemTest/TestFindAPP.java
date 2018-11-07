package systemTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import serverFrame.HttpConstants;

public class TestFindAPP extends HtmlTest{
	private final static int PORT = 8080;
	private final static String PATH = "/find";
	
	/**
	 * test asin have two string
	 * should return 400 bad request
	 */
	@Test
	public void testBadRequest() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "asin=bbbb+aaa");
		assertTrue(html.startsWith(HttpConstants.BAD_REQUEST));
		System.out.println(html);
	}
	
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
	 */
	@Test
	public void testValidPost() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "asin=3998899561");
		assertTrue(html.startsWith(HttpConstants.OK_HEADER));
	}
	
	/**
	 * test post page have a table
	 */
	@Test
	public void testIfHaveTable() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "asin=3998899561");
		assertTrue(html.contains("<table"));
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
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "unused=bbbb");
		assertTrue(html.contains("Not Enough Parameters"));
	}
	
	/**
	 * test no search result
	 */
	@Test
	public void testNoResult() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "query=bbbb");
		assertFalse(html.contains("No such result"));
	}
}
