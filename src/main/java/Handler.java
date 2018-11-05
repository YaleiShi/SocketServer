
public interface Handler {

	/**
	 * pass the request and respond into the handler to handle
	 * @param request
	 * @param respond
	 */
	public void handle(HttpRequest request, HttpResponse response);
}
