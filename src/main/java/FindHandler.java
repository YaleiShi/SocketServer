import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import invertedIndex.AmazonDataBase;
import invertedIndex.AmazonMessage;
import invertedIndex.AmazonQA;
import invertedIndex.AmazonReview;

public class FindHandler extends BasicHandler{
	private AmazonDataBase base;
	
	public FindHandler() {
		this.base = AmazonDataBase.getInstance();
	}
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		// TODO Auto-generated method stub
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("GetFind"));
		String page = "<form action=\"find\" method=\"post\">" +
			    "Asin: "+
			    "<input type=\"text\" name=\"asin\"> "+
			    "<input type=\"submit\" value=\"Submit\"></form><hr>"+
				"</body></html>";
		writer.write(page);
		writer.flush();
		System.out.println("finish writing");
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		String asin = request.getParam("asin");
		ArrayList<AmazonMessage> reviewList = base.findReviewAsin(asin);
		ArrayList<AmazonMessage> qaList = base.findQAAsin(asin);
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
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
						+ ar.getReviewID() + "</td><td>"
						+ ar.getScore() + "</td><td>"
						+ ar.getText() + "</td></tr>");
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
						+ ar.getQuestion() + "</td><td>"
						+ ar.getAnswer() + "</td></tr>");
				count++;
			}
			writer.write("</table></body></html>");
			System.out.println("finish writing");
		}
	}

}
