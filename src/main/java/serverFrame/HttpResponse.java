package serverFrame;
import java.io.PrintWriter;

/**
 * http response
 * store the writer and response headers, status
 * @author yalei
 *
 */
public class HttpResponse {
	private String status;
	private String headers;
	private PrintWriter writer;
	
	/**
	 * pass the writer into the response
	 * and initiate the status and headers to be empty string
	 * @param writer
	 */
	public HttpResponse(PrintWriter writer) {
		this.writer = writer;
		this.status = "";
		this.headers = "";
	}
	
	/**
	 * set up the response status
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * set up the content type header
	 * @param s
	 */
	public void setContentType(String s) {
		this.headers += "Content-Type: " + s + "\n";
	}
	
	/**
	 * write the headers out and return the writer
	 * @return
	 */
	public PrintWriter prepareWriter() {
		this.writer.write(status + headers + "\r\n");
		return this.writer;
	}
	
	/**
	 * just return the writer
	 * @return
	 */
	public PrintWriter getWriter() {
		return this.writer;
	}
}
