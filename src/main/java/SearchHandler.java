import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import invertedIndex.AmazonDataBase;
import invertedIndex.AmazonMessage;
import invertedIndex.AmazonReview;
import invertedIndex.InvertedIndex;

public class SearchHandler extends BasicHandler{
	private AmazonDataBase base;
	
	public SearchHandler() {
		this.base = AmazonDataBase.getInstance();
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		request.printRequest();
		// TODO Auto-generated method stub
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("GetSearch"));
		String page = "<form action=\"reviewsearch\" method=\"post\">" +
			    "Query: "+
			    "<input type=\"text\" name=\"query\"> "+
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
		String query = request.getParam("query");
		TreeMap<Integer, ArrayList<AmazonMessage>> data = base.reviewSearch(query);
		response.setStatus(HttpConstants.OK_HEADER);
		response.setContentType("text/html");
		PrintWriter writer = response.prepareWriter();
		writer.write(simpleHeader("PostSearch"));
		if(data == null) {
			writer.write("No such result</body></html>");
			return;
		}
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
		writer.write("</table></body></html>");
	}

}
