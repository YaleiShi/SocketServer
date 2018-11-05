import java.util.HashMap;

public class Driver {

	public static void main(String[] args) {
		HttpServer server = new HttpServer(8080);
		Handler search = new SearchHandler();
		server.addHandler("/reviewsearch", search);
		server.addHandler("/find", new FindHandler());
		server.start();
		
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		map.put("nina", 1);
//		int i = map.get("yalei");
//		System.out.println(i);
		
//		String test = "/super?cat=shit&";
//		int index = test.indexOf("?");
//		String out = test.substring(index + 1);
//		System.out.println(out.isEmpty());
//		String[] ss = out.split("&");
//		System.out.println(ss.length);
//		for(String s: ss) {
//			System.out.println(s);
//		}
	}
}
