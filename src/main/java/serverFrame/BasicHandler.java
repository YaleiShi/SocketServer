package serverFrame;

import java.io.PrintWriter;

public abstract class BasicHandler implements Handler {
	protected final int LIMIT = 50;
	protected static final String TableStyle = 
			"<table border=2 border-spacing=3px style=\"width:100%\"><style>\r\n" + 
			"table {\r\n" + 
			"    font-family: arial, sans-serif;\r\n" + 
			"    width: 200%;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"td, th {\r\n" + 
			"    border: 1px solid #dddddd;\r\n" + 
			"    text-align: left;\r\n" + 
			"    padding: 8px;\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"tr:nth-child(even) {\r\n" + 
			"    background-color: rgb(170, 203, 255);\r\n" + 
			"}\r\n" + 
			"</style>";

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
		sb.append("<html><head><title>" + title + "</title></head><body>");
		sb.append("<center>");
		sb.append("<h1>This is inverted index api</h1>");
		return sb.toString();
	}
	
	protected String simpleFooter() {
		StringBuilder sb = new StringBuilder();
		sb.append("</center>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	protected String simpleForm(String path, String query) {
		String form = "<form action=\"" + path + "\" method=\"post\">" +
			    "Query: "+
			    "<input type=\"text\" name=\"" + query + "\"> "+
			    "<input type=\"submit\" value=\"Submit\"></form><hr/>";
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

}
