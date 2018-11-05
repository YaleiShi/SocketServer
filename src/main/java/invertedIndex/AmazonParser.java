package invertedIndex;

import com.google.gson.JsonObject;

/**
 * the amazon parser class
 * having the function which can parse the json object into a amazon message 
 * @author yalei
 *
 */
public class AmazonParser {
	
	/**
	 * the function to parse the json object
	 * if the boolean is true, parse it into a AmazonReview
	 * else parse into a AmazonQA
	 * @param jo the jsonObject used to parse
	 * @param ifReview if parse into AmazonReview
	 * @return An AmazonMessage
	 */
	public AmazonMessage parse(JsonObject jo, boolean ifReview) {
		AmazonMessage am;
		if(ifReview) {
			am = this.parseReview(jo);
		}else {
			am = this.parseQA(jo);
		}
		return am;
	}
	
	/**
	 * the function to parse the json into a AmazonReview
	 * @param jo the json file to parse
	 * @return the AmazonReview
	 */
	public AmazonMessage parseReview(JsonObject jo) {
		String asin = jo.get("asin").getAsString();
		String score = jo.get("overall").getAsString();
		String text = jo.get("reviewText").getAsString();
		String reviewId = jo.get("reviewerID").getAsString();
		AmazonMessage am = new AmazonReview(asin, score, text, reviewId);
		return am;
	}
	
	/**
	 * the function to parse the json into a AmazonQA
	 * @param jo the json file to parse
	 * @return the AmazonQA
	 */
	public AmazonMessage parseQA(JsonObject jo) {
		String asin = jo.get("asin").getAsString();
		String question = jo.get("question").getAsString();
		String answer = jo.get("answer").getAsString();
		AmazonMessage am = new AmazonQA(asin, question, answer);
		return am;
	}

}
