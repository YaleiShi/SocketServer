package serverFrame;
import java.io.PrintWriter;

public class HttpResponse {
	private String status;
	private String headers;
	private PrintWriter writer;
	
	public HttpResponse(PrintWriter writer) {
		this.writer = writer;
		this.status = "";
		this.headers = "";
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setContentType(String s) {
		this.headers += "Content-Type: " + s + "\n";
	}
	
	public PrintWriter prepareWriter() {
		this.writer.write(status + headers + "\r\n");
		return this.writer;
	}
	
	public PrintWriter getWriter() {
		return this.writer;
	}
}
