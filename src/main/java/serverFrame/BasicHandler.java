package serverFrame;

import java.io.PrintWriter;

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


	public void handle(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		if(request.getType().equals("GET")) {
			this.doGet(request, response);
		}else if(request.getType().equals("POST")) {
			this.doPost(request, response);
		}
	}
	
	public abstract void doGet(HttpRequest request, HttpResponse response);
	
	public abstract void doPost(HttpRequest request, HttpResponse response);
	
	protected String simpleHeader(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><head><title>" + title + "</title></head><body>");
		sb.append("<h1>This is inverted index api</h1>");
		return sb.toString();
	}
	
	protected String simpleFooter() {
		StringBuilder sb = new StringBuilder();
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	protected String simpleForm(String path, String query) {
		String form = "<form action=\"" + path + "\" method=\"post\">" +
			    "Query: "+
			    "<input type=\"text\" name=\"" + query + "\"/> "+
			    "<input type=\"submit\" value=\"Submit\"/></form>";
		return form;
	}
	
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
	
	protected PrintWriter okStatus(HttpResponse response) {
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		return writer;
	}
	
	protected void badRequest(HttpResponse response) {
		response.setStatus(HttpConstants.BAD_REQUEST);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("Bad Request"));
		writer.write("<p>400 Bad Request</p>");
		writer.write(simpleFooter());
	}

}
