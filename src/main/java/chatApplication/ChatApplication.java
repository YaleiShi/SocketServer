package chatApplication;
import serverFrame.ConfigReader;
import serverFrame.HttpServer;

/**
 * chat server
 * use server socket to send request to slack api
 * @author yalei
 *
 */
public class ChatApplication {
	
	/**
	 * read the config file, get the param 
	 * start the server
	 * @param args
	 */
	public static void main(String[] args) {
		//get the config setting
		ConfigReader cr = new ConfigReader("config.json");
		String token = cr.getConfig("token");
		String channel = cr.getConfig("channel");
		String url = cr.getConfig("URL");
		String path = cr.getConfig("chatPath");
		int port = Integer.parseInt(cr.getConfig("chatPort"));
		
		//start the server;
		HttpServer chatServer = new HttpServer(port);
		chatServer.addHandler(path, new ChatHandler(token, channel, url));
		chatServer.start();
	}
	
}
