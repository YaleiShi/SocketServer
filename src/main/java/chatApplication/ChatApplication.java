package chatApplication;
import serverFrame.ConfigReader;
import serverFrame.HttpServer;

public class ChatApplication {
	
	public static void main(String[] args) {
		//get the config setting
		ConfigReader cr = new ConfigReader("config.json");
		String token = cr.getConfig("token");
		String channel = cr.getConfig("channel");
		String url = cr.getConfig("URL");
		
		//start the server;
		HttpServer chatServer = new HttpServer(9090);
		chatServer.addHandler("/slackbot", new ChatHandler(token, channel, url));
		chatServer.start();
	}
	
}
