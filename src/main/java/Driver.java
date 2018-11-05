import java.util.HashMap;

public class Driver {

	public static void main(String[] args) {
//		HttpServer searchServer = new HttpServer(8080);
//		Handler search = new SearchHandler();
//		searchServer.addHandler("/reviewsearch", search);
//		searchServer.addHandler("/find", new FindHandler());
		HttpServer chatServer = new HttpServer(9090);
		chatServer.addHandler("/slackbot", new ChatHandler());
//		searchServer.start();
		chatServer.start();
	}
}
