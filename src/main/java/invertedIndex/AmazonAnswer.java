package invertedIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * the answer class used to answer the request
 * show the search result
 * @author yalei
 *
 */
public class AmazonAnswer {
	private InvertedIndex forReview;
	private InvertedIndex forQA;
	
	/**
	 * the constructor,
	 * take the two data base
	 * @param forReview the review data base
	 * @param forQA the QA data base
	 */
	public AmazonAnswer(InvertedIndex forReview, InvertedIndex forQA) {
		this.forQA = forQA;
		this.forReview = forReview;
	}
	
	/**
	 * the function which keep accept the request from the command line
	 * if request if not null, pass it to the request method
	 * if the request if exit, end the program
	 */
	public void answer() {
		this.prepareUI();
		Scanner s = new Scanner(System.in);
		String input = s.nextLine();
		while(true) {
			if(input == null) {
				input = s.nextLine();
			}else if(input.equals("exit")) {
				System.exit(1);
			}else {
				this.request(input);
				input = s.nextLine();
			}
		}
	}
	
	/**
	 * prepare the start UI for the user
	 */
	public void prepareUI() {
		System.out.println("Welcome, please endter these commands: \n"
				         + "         find <asin>\n"
				         + "         reviewsearch <term>\n"
				         + "         qasearch <term>\n"
				         + "         reviewpartialsearch <part of the term>\n"
				         + "         qapartialsearch <part of the term>\n"
				         + "         exit");
	}
	
	/**
	 * take the input string and judge if it is valid
	 * if not, print message and return
	 * if valid, pass it to the right function
	 * @param input the command line input
	 */
	public void request(String input) {
		String[] args = input.split(" ");
		if(args.length != 2) {
			System.out.println("invalid args length");
			return;
		}
		if(args[0].equals("find")) {
			System.out.println("go to find");
			this.find(args[1]);
		}else if(args[0].equals("reviewsearch")) {
			System.out.println("go to review");
			this.reviewSearch(args[1]);
		}else if(args[0].equals("qasearch")) {
			System.out.println("go to qa");
			this.qaSearch(args[1]);
		}else if(args[0].equals("reviewpartialsearch")) {
			System.out.println("go to p review");
			this.reviewPartialSearch(args[1]);
		}else if(args[0].equals("qapartialsearch")) {
			System.out.println("go to p qa");
			this.qaPartialSearch(args[1]);
		}else {
			System.out.println("invalid input");
		}
		return;
	}
	
	/**
	 * the general method to gather the output in the amazon message array
	 * @param al the array having all the message
	 * @param i the frequency, if -1, do not output
	 * @return the result String
	 */
	public String reviewText(ArrayList<AmazonMessage> al, int i) {
		StringBuilder sb = new StringBuilder();
		for(AmazonMessage am: al) {
			AmazonReview ar = (AmazonReview) am;
			if(i > 0) sb.append("WordFrequency: " + i + "\n");
			sb.append("Review ID: " + ar.getReviewID() + "\n");
			sb.append("Score: " + ar.getScore() + "\n");
			sb.append("Text: " + ar.getText() + "\n\n");
		}
		return sb.toString();
	}
	
	/**
	 * the general method to gather the output in the amazon message array
	 * @param al the array having all the message
	 * @param i the frequency, if -1, do not output
	 * @return the result String
	 */
	public String qaText(ArrayList<AmazonMessage> al, int i) {
		StringBuilder sb = new StringBuilder();
		for(AmazonMessage am: al) {
			AmazonQA ar = (AmazonQA) am;
			if(i > 0) sb.append("WordFrequency: " + i + "\n");
			sb.append("Question: " + ar.getQuestion() + "\n");
			sb.append("Answer: " + ar.getAnswer() + "\n\n");
		}
		return sb.toString();
	}
	
	/**
	 * the function in charge of the command find
	 * @param asin the asin number
	 */
	public void find(String asin) {
		//search the review
		StringBuilder sb = new StringBuilder();
		sb.append("find asin number: " + asin + "\n");
		sb.append("********** Reviews ***********\n");
		ArrayList<AmazonMessage> al = this.forReview.getAsinArray(asin);
		if(al == null) {
			sb.append("no review\n");
		}else {
			sb.append(this.reviewText(al, -1));
		}
		
		//search the QA
		sb.append("********** QAs ***********\n");
		al = this.forQA.getAsinArray(asin);
		if(al == null) {
			sb.append("no QA\n=======================\n");
		}else {
			sb.append(this.qaText(al, -1));
			sb.append("=========================\n");
		}
		
		System.out.println(sb);
	}
	
	/**
	 * the function in charge of the command reviewsearch
	 * @param term the word user want to search
	 */
	public void reviewSearch(String term) {
		term = term.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append("search review by the word: " + term + "\n");
		sb.append("********** Reviews ***********\n");
		TreeMap<Integer, ArrayList<AmazonMessage>> ts = this.forReview.getTermMap(term);
		if(ts == null) {
			sb.append("no review\n========================\n");
		}else {
			for(int i: ts.keySet()) {
				sb.append(this.reviewText(ts.get(i), i));
			}
			sb.append("=========================\n");
		}

		System.out.println(sb);
	}
	
	/**
	 * the function in charge of the command qasearch
	 * @param term the word user want to search
	 */
	public void qaSearch(String term) {
		term = term.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append("search QA by the word: " + term + "\n");
		sb.append("********** QAs ***********\n");
		TreeMap<Integer, ArrayList<AmazonMessage>> ts = this.forQA.getTermMap(term);
		if(ts == null) {
			sb.append("no QA\n========================\n");
		}else {
			for(int i: ts.keySet()) {
				sb.append(this.qaText(ts.get(i), i));
			}
			sb.append("=========================\n");
		}

		System.out.println(sb);
	}
	
	/**
	 * the function of command review partial search
	 * @param term the string which user want to search
	 */
	public void reviewPartialSearch(String term) {
		term = term.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append("partial search review by the word: " + term + "\n");
		sb.append("********** Reviews ***********\n");
		TreeSet<AmazonMessage> ts = this.forReview.partialSearchResult(term);
		if(ts.size() == 0) {
			sb.append("no review\n========================\n");
		}else {
			for(AmazonMessage am: ts) {
				AmazonReview ar = (AmazonReview) am;
				sb.append("Review ID: " + ar.getReviewID() + "\n");
				sb.append("Score: " + ar.getScore() + "\n");
				sb.append("Text: " + ar.getText() + "\n\n");
			}
			sb.append("=========================\n");
		}

		System.out.println(sb);
	}
	
	/**
	 * the function of command qapartialsearch
	 * @param term the string user want to include in the word
	 */
	public void qaPartialSearch(String term) {
		term = term.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append("partial search QA by the word: " + term + "\n");
		sb.append("********** QAs ***********\n");
		TreeSet<AmazonMessage> ts = this.forQA.partialSearchResult(term);
		
		if(ts.size() == 0) {
			sb.append("no review\n========================\n");
		}else {
			for(AmazonMessage am: ts) {
				AmazonQA ar = (AmazonQA) am;
				sb.append("Question: " + ar.getQuestion() + "\n");
				sb.append("Answer: " + ar.getAnswer() + "\n\n");
			}
			sb.append("=========================\n");
		}

		System.out.println(sb);
	}
}
