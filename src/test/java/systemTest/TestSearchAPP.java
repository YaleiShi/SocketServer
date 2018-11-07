package systemTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import serverFrame.HttpConstants;

/**
 * Test the search api
 * @author yalei
 *
 */
public class TestSearchAPP extends HtmlTest{
	private final static int PORT = 8080;
	private final static String PATH = "/reviewsearch";
	
	/**
	 * test method not right
	 */
	@Test
	public void testMethod() {
		String html = TestHelper.getHttpResponse(URL, "GE", PATH, PORT, "");
		assertTrue(html.startsWith(HttpConstants.METHOD_NOT_ALLOWED));
	}
	
	/**
	 * test path not right
	 */
	@Test
	public void testPath() {
		String html = TestHelper.getHttpResponse(URL, "GET", "/review", PORT, "");
		assertTrue(html.startsWith(HttpConstants.NOT_FOUND));
	}
	
	/**
	 * test method and path both wrong
	 * should return bad method
	 */
	@Test 
	public void testMix() {
		String html = TestHelper.getHttpResponse(URL, "GE", "/review", PORT, "");
		assertTrue(html.startsWith(HttpConstants.METHOD_NOT_ALLOWED));
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
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "query=supermarket");
		assertTrue(html.startsWith(HttpConstants.OK_HEADER));
	}
	
	/**
	 * test post page have a table
	 */
	@Test
	public void testIfHaveTable() {
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "query=supermarket");
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
		String html = TestHelper.getHttpResponse(URL, POST, PATH, PORT, "query=superman");
		assertTrue(html.contains("No such result"));
	}
}
