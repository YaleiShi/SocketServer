package serverFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigReader {
	private JsonObject object;
	
	public ConfigReader(String input) {
		JsonParser jp = new JsonParser();
		try(BufferedReader reader = new BufferedReader(new FileReader(input))){
			String file = reader.readLine();
			JsonElement e = jp.parse(file);
			this.object = (JsonObject) e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getConfig(String key) {
		return object.get(key).getAsString();
	}

}
