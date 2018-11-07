package searchApplication;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringEscapeUtils;

import invertedIndex.AmazonDataBase;
import invertedIndex.AmazonMessage;
import invertedIndex.AmazonQA;
import invertedIndex.AmazonReview;
import serverFrame.BasicHandler;
import serverFrame.HttpConstants;
import serverFrame.HttpRequest;
import serverFrame.HttpResponse;

/**
 * the find handler which handle the URL: /find
 * @author yalei
 *
 */
public class FindHandler extends BasicHandler{
	private AmazonDataBase base;
	private final String KEY = "asin";
	
	/**
	 * get the data base from the singleton data base
	 */
	public FindHandler() {
		this.base = AmazonDataBase.getInstance();
	}
	
	/**
	 * handle the get method
	 * output the form
	 */
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		PrintWriter writer = okStatus(response);
		writer.write(simpleHeader("GetFind"));
		writer.write(simpleForm("find", KEY));
		writer.write(simpleFooter());
		System.out.println("finish writing");
	}

	/**
	 * handle the post method
	 * output the table or empty page if nothing found
	 * or bad request if query have multiple components
	 */
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		if(request.hasParam(KEY) && request.getParam(KEY).split(" ").length > 1) {
			badRequest(response);
			return;
		}
		PrintWriter writer = okStatus(response);
		if(!checkParam(KEY, request, response)) {
			return;
		}
		String asin = request.getParam(KEY).trim();
		
		ArrayList<AmazonMessage> reviewList = base.findReviewAsin(asin);
		ArrayList<AmazonMessage> qaList = base.findQAAsin(asin);
		writer.write(simpleHeader("PostSearch"));
		writer.write("<p>Asin Searched: " + asin + "</p>");
		writer.write("<p>Review Table</p>");
		int count = 0;
		if(reviewList == null) {
			System.out.println("no reviewList");
			writer.write("<p>No such result</p>");
		}else {
			writer.write(TableStyle);
			writer.write("<tr><th>Review Id</th><th>Score</th><th>Text</th></tr>");
			for(AmazonMessage am: reviewList) {
				if(count >= LIMIT/2) {
					break;
				}
				AmazonReview ar = (AmazonReview) am;
				writer.write("<tr><td>" 
						+ StringEscapeUtils.escapeHtml4(ar.getReviewID()) + "</td><td>"
						+ StringEscapeUtils.escapeHtml4(ar.getScore()) + "</td><td>"
						+ StringEscapeUtils.escapeHtml4(ar.getText()) + "</td></tr>");
				count++;
			}
			writer.write("</table>");
		}
		writer.write("<p>QA Table</p>");
		if(qaList == null) {
			System.out.println("no qaList");
			writer.write("<p>No such result</p></body></html>");
		}else {
			writer.write(TableStyle);
			writer.write("<tr><th>Question</th><th>Answer</th></tr>");
			count = 0;
			for(AmazonMessage am: qaList) {
				if(count >= LIMIT/2) {
					break;
				}
				AmazonQA ar = (AmazonQA) am;
				writer.write("<tr><td>"
						+ StringEscapeUtils.escapeHtml4(ar.getQuestion()) + "</td><td>"
						+ StringEscapeUtils.escapeHtml4(ar.getAnswer()) + "</td></tr>");
				count++;
			}
			writer.write("</table>");
			writer.write(simpleFooter());
			System.out.println("finish writing");
		}
	}

}
