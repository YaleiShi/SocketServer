package searchApplication;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import invertedIndex.AmazonDataBase;
import invertedIndex.AmazonMessage;
import invertedIndex.AmazonReview;
import invertedIndex.InvertedIndex;
import serverFrame.BasicHandler;
import serverFrame.HttpConstants;
import serverFrame.HttpRequest;
import serverFrame.HttpResponse;

public class SearchHandler extends BasicHandler{
	private AmazonDataBase base;
	
	public SearchHandler() {
		this.base = AmazonDataBase.getInstance();
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		request.printRequest();
		// TODO Auto-generated method stub
		PrintWriter writer = okStatus(response);
		writer.write(simpleHeader("GetSearch"));
		writer.write(simpleForm("reviewsearch", "query"));
		writer.write(simpleFooter());
		System.out.println("finish writing");
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		PrintWriter writer = okStatus(response);
		if(!checkParam("query", request, response)) {
			return;
		}
		String query = request.getParam("query").toLowerCase();
		TreeMap<Integer, ArrayList<AmazonMessage>> data = base.reviewSearch(query);
		writer.write(simpleHeader("PostSearch"));
		
		//check if the we have the data
		if(data == null) {
			writer.write("<p>No such result</p>");
			writer.write(simpleFooter());
			return;
		}
		
		//write the result table;
		writer.write(TableStyle);
		writer.write("<tr><th>Word Frequency</th><th>Review Id</th><th>Score</th><th>Text</th></tr>");
		int count = 0;
		for(int i: data.keySet()) {
			for(AmazonMessage am: data.get(i)) {
				if(count >= LIMIT) {
					break;
				}
				AmazonReview ar = (AmazonReview) am;
				writer.write("<tr><td>" + i + "</td><td>" 
						+ ar.getReviewID() + "</td><td>"
						+ ar.getScore() + "</td><td>"
						+ ar.getText() + "</td></tr>");
				count++;
			}
			if(count >= LIMIT) {
				break;
			}
		}
		writer.write("</table>");
		writer.write(simpleFooter());
	}

}
