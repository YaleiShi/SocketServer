package searchApplication;
import java.util.HashMap;

import serverFrame.Handler;
import serverFrame.HttpServer;

public class SearchApplication {

	/**
	 * start the server of search application
	 * initiate and add two handler into the server
	 * then start
	 * @param args
	 */
	public static void main(String[] args) {
		HttpServer searchServer = new HttpServer(8080);
		Handler search = new SearchHandler();
		searchServer.addHandler("/reviewsearch", search);
		searchServer.addHandler("/find", new FindHandler());
		searchServer.start();
	}

}
