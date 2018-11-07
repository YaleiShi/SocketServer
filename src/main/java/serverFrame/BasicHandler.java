package serverFrame;

import java.io.PrintWriter;

/**
 * the basic handler abstract class
 * store the Constants value and method
 * @author yalei
 *
 */
public abstract class BasicHandler implements Handler {
	protected final int LIMIT = 50;
	protected static final String TableStyle = "<style>\r\n" + 
			"table {\r\n" + 
			"    font-family: arial, sans-serif;\r\n" + 
			"    border-collapse: collapse;\r\n" + 
			"    width: 100%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"td, th {\r\n" + 
			"    border: 1px solid #dddddd;\r\n" + 
			"    text-align: left;\r\n" + 
			"    padding: 8px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"tr:nth-child(even) {\r\n" + 
			"    background-color: #dddddd;\r\n" + 
			"}\r\n" + 
			"</style>" + 
			"<table style=\"width:100%\">";


	/**
	 * check the method of the request
	 * send it to the right place;
	 */
	public void handle(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		if(request.getType().equals("GET")) {
			this.doGet(request, response);
		}else if(request.getType().equals("POST")) {
			this.doPost(request, response);
		}
	}
	
	/**
	 * abstract method 
	 * handle the get method
	 * @param request
	 * @param response
	 */
	public abstract void doGet(HttpRequest request, HttpResponse response);
	
	/**
	 * handle the post method
	 * @param request
	 * @param response
	 */
	public abstract void doPost(HttpRequest request, HttpResponse response);
	
	/**
	 * prepare a simple header for the html page
	 * @param title
	 * @return
	 */
	protected String simpleHeader(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><head><title>" + title + "</title></head><body>");
		sb.append("<h1>This is inverted index api</h1>");
		return sb.toString();
	}
	
	/**
	 * prepare a simple footer for the html page
	 * @return
	 */
	protected String simpleFooter() {
		StringBuilder sb = new StringBuilder();
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	/**
	 * prepare a simple form for the html page
	 * @param path
	 * @param query
	 * @return
	 */
	protected String simpleForm(String path, String query) {
		String form = "<form action=\"" + path + "\" method=\"post\">" +
			    "Query: "+
			    "<input type=\"text\" name=\"" + query + "\"/> "+
			    "<input type=\"submit\" value=\"Submit\"/></form>";
		return form;
	}
	
	/**
	 * check the request to see if it has the key parameter
	 * if not, out put the html page and return false
	 * if has, return true, let the upper method do the rest things
	 * @param key
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean checkParam(String key, HttpRequest request, HttpResponse response) {
		if(!request.hasParam(key)) {
			PrintWriter pw = response.getWriter();
			pw.write(simpleHeader("Not Enough Parameters"));
			pw.write("<p>Need Parameter: " + key + "</p>");
			pw.write(simpleFooter());
			return false;
		}
		return true;
	}
	
	/**
	 * prepare an ok status response and return print writer
	 * @param response
	 * @return
	 */
	protected PrintWriter okStatus(HttpResponse response) {
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		return writer;
	}
	
	/**
	 * prepare a bad request response and return the writer
	 * @param response
	 */
	protected void badRequest(HttpResponse response) {
		response.setStatus(HttpConstants.BAD_REQUEST);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("Bad Request"));
		writer.write("<p>400 Bad Request</p>");
		writer.write(simpleFooter());
	}

}
