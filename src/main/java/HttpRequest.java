import java.util.HashMap;

public class HttpRequest {
	private String type;
	private String path;
	private HashMap<String, String> params;
	
	public HttpRequest() {
		this.params = new HashMap<String, String>();
		this.type = "";
		this.path = "";
	}
	
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

	public String getType() {
		return type;
	}

	public String getPath() {
		return path;
	}
	
	public String getParam(String key) {
		if(!this.params.containsKey(key)) {
			return null;
		}
		return this.params.get(key);
	}
	
	public void printRequest() {
		for(String s: this.params.keySet()) {
			System.out.println("param: " + s + " -> " + this.params.get(s));
		}
	}
	
}
