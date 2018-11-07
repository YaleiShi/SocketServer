package invertedIndex;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import serverFrame.ConfigReader;

/**
 * the process class
 * process the json file and save all the data into data base
 * @author yalei
 *
 */
public class AmazonDataBase {
	private static AmazonDataBase instance;
	private InvertedIndex forReview;
	private InvertedIndex forQA;
	private String inputReview;
	private String inputQA;
	private int limitLine;
	
	/**
	 * constructor of the processor
	 * call the getArgs method to get the args
	 * @param args the args pass into the processor
	 */
	private AmazonDataBase() {
		this.forReview = new InvertedIndex();
		this.forQA = new InvertedIndex();
		ConfigReader cr = new ConfigReader("config.json");
		this.inputReview = cr.getConfig("inputReview");
		this.inputQA = cr.getConfig("inputQA");
		this.limitLine = Integer.parseInt(cr.getConfig("limitLine"));
		process();
	}
	
	/**
	 * get the singleton instance of AmazonDataBase
	 * @return
	 */
	public static synchronized AmazonDataBase getInstance() {
		if(instance == null) {
			instance = new AmazonDataBase();
		}
		return instance;
	}
	
	/**
	 * process the two json file to get all the data and save into the data base
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void process(){
		process(this.inputReview, this.forReview, true, "review");
		process(this.inputQA, this.forQA, false, "QA");
	}
	
	/**
	 * process one specific file, get all the data, save into one data base
	 * count the lines read, print it out for debug
	 * @param input the input path
	 * @param index the data base saved into
	 * @param ifReview if parse the data into a review or a QA
	 * @param title the title print out in the console
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void process(String input, InvertedIndex index, boolean ifReview, String title){
		//prepare the parsers
		AmazonParser aParser = new AmazonParser();
		JsonParser parser = new JsonParser();
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		AmazonReview ar;
		AmazonQA aq;
		int read = 0;
		
		//read the file
		try(BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(input)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"),10 * 1024 * 1024)){
			String line = reader.readLine();
			while(line != null && read <= limitLine) {
				read++;
				JsonElement element;
				try {
					element = parser.parse(line);
				}catch(JsonSyntaxException jse) {
					line = reader.readLine();
					continue;
				}	
				if(element.isJsonObject()) {
					JsonObject jo = (JsonObject) element;
					AmazonMessage am = aParser.parse(jo, ifReview);
					if(ifReview) {
						ar = (AmazonReview) am;
						this.calculateFreq(freqMap, ar.getText());
					}else {
						aq = (AmazonQA) am;
						this.calculateFreq(freqMap, aq.getQuestion());
						this.calculateFreq(freqMap, aq.getAnswer());
					}
					index.add(am, freqMap);
					freqMap.clear();
				}	
				line = reader.readLine();
			}	
		}catch(FileNotFoundException fnfe) {
			System.out.println(fnfe.getMessage());
		}catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		System.out.println("we read " + title + " lines: " + read);	
	}
	
	/**
	 * get the term frequency hashmap of the text
	 * @param freqMap
	 * @param text
	 */
	public void calculateFreq(HashMap<String, Integer> freqMap, String text) {
		String[] args = text.split("\\s+");
		for(String s: args) {
			s = s.replaceAll("\\W", "");
			s = s.toLowerCase();
			if(!freqMap.containsKey(s)) {
				freqMap.put(s, 1);
			}else {
				int freq = freqMap.get(s);
				freq++;
				freqMap.put(s, freq);
			}
		}
	}
	
	/**
	 * output the array list by the asin number from the review
	 * @param asin asin number
	 * @return the array list with all the message
	 */
	public ArrayList<AmazonMessage> findReviewAsin(String asin){
		ArrayList<AmazonMessage> al = this.forReview.getAsinArray(asin);
		return al;
	}
	
	/**
	 * output the array list by the asin number from the qa
	 * @param asin asin number
	 * @return the array list with all the message
	 */
	public ArrayList<AmazonMessage> findQAAsin(String asin){
		ArrayList<AmazonMessage> al = this.forQA.getAsinArray(asin);
		return al;
	}
	
	/**
	 * return the tree map under the given word from the review
	 * @param term the word we are searching
	 * @return the tree map
	 */
	public TreeMap<Integer, ArrayList<AmazonMessage>> reviewSearch(String term){
		return this.forReview.getTermMap(term);
	}
	
	/**
	 * return the tree map under the given word from the review
	 * @param term the word we are searching
	 * @return the tree map
	 */
	public TreeMap<Integer, ArrayList<AmazonMessage>> qaSearch(String term){
		return this.forQA.getTermMap(term);
	}

}
