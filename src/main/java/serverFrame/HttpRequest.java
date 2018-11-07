package serverFrame;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * store the data used in the http handler
 * passed into the handler to construct the html page
 * @author yalei
 *
 */
public class HttpRequest {
	private String type;
	private String path;
	private HashMap<String, String> params;
	
	/**
	 * initiate the hash map and all data
	 */
	public HttpRequest() {
		this.params = new HashMap<String, String>();
		this.type = "";
		this.path = "";
	}
	
	/**
	 * build the request from the args
	 * check the path and take out the parameters
	 * return true if good request
	 * false if not
	 * @param args
	 * @return
	 */
	public boolean buildRequest(String[] args) {
		if(args.length != 3) {
			return false;
		}
		this.type = args[0];
		if(args[1].contains("?")) {
			int index = args[1].indexOf("?");
			this.path = args[1].substring(0, index);
			String param = args[1].substring(index + 1);
			if(!param.isEmpty()) {
				addParams(param);
			}
		}else {
			this.path = args[1];
		}
		return true;
	}
	
	/**
	 * input the string 
	 * check the string and take out the parameters
	 * put them into the hash map
	 * @param s
	 */
	public void addParams(String s) {
		String[] ps = s.split("&");
		for(String p: ps) {
			//can not treat "cat&=pretty&"
			if(!p.contains("=")) {
				continue;
			}
			int index = p.indexOf("=");
			if(index == 0 || index == p.length() - 1) {
				//can not treat "=cat=pretty"
				continue;
			}
			String param = p.substring(0, index);
			String value = p.substring(index + 1);
			this.params.put(param, value);
		}
	}

	/**
	 * return the type of the request
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * return the path which the request asked
	 * @return
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * return if the request has the parameter
	 * @param key
	 * @return
	 */
	public boolean hasParam(String key) {
		return this.params.containsKey(key);
	}
	
	/**
	 * return the value of the key parameter
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		if(!this.params.containsKey(key)) {
			return null;
		}
		String s = this.params.get(key);
		try {
			s = URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * print out the request
	 * for testing only
	 */
	public void printRequest() {
		System.out.println("type: " + type + "; path: " + path);
		for(String s: this.params.keySet()) {
			System.out.println("param: " + s + " -> " + this.params.get(s));
		}
	}
	
}
