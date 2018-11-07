package unitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import serverFrame.HttpRequest;

public class TestRequest {
	private HttpRequest request;
	private final String[] Valid = {"GET", "/find", "http/1.0"};
	private final String[] Invalid = {"GET", "http/1.0"};
	
	@Test
	public void testOriginal() {
		request = new HttpRequest();
		assertEquals(request.getPath(), "");
		assertEquals(request.getType(), "");
	}
	
	@Test
	public void testInvalidArgsLength() {
		request = new HttpRequest();
		String[] args = {"GET", "http/1.0"};
		assertFalse(request.buildRequest(Invalid));
	}
	
	@Test
	public void testValidArgsLength() {
		request = new HttpRequest();
		assertTrue(request.buildRequest(Valid));
	}
	
	@Test
	public void testAddParamFromArgs() {
		request = new HttpRequest();
		String[] args = {"GET", "/find?test=good", "http/1.0"};
		request.buildRequest(args);
		assertTrue(request.hasParam("test"));
	}
	
	@Test
	public void testAddParam() {
		request = new HttpRequest();
		request.addParams("test=good&cs=601");
		assertTrue(request.hasParam("test"));
		assertTrue(request.hasParam("cs"));
	}
	
	@Test
	public void testInvalidAddParam() {
		request = new HttpRequest();
		request.addParams("test=good&cs=");
		assertTrue(request.hasParam("test"));
		assertFalse(request.hasParam("cs"));
	}
	
	@Test
	public void testDecode() {
		request = new HttpRequest();
		request.addParams("test=good+bad");
		String value = request.getParam("test");
		assertEquals(value, "good bad");
	}
}
