package searchApplication;
import java.util.HashMap;

import serverFrame.Handler;
import serverFrame.HttpServer;

public class SearchApplication {

	public static void main(String[] args) {
		HttpServer searchServer = new HttpServer(8080);
		Handler search = new SearchHandler();
		searchServer.addHandler("/reviewsearch", search);
		searchServer.addHandler("/find", new FindHandler());
		searchServer.start();
	}

}
