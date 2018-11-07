package searchApplication;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringEscapeUtils;

import invertedIndex.AmazonDataBase;
import invertedIndex.AmazonMessage;
import invertedIndex.AmazonReview;
import invertedIndex.InvertedIndex;
import serverFrame.BasicHandler;
import serverFrame.HttpConstants;
import serverFrame.HttpRequest;
import serverFrame.HttpResponse;

/**
 * the search handler
 * handle the path: /reviewsearch
 * @author yalei
 *
 */
public class SearchHandler extends BasicHandler{
	private AmazonDataBase base;
	private final String KEY = "query";
	
	/**
	 * get the data from the singleton data base
	 */
	public SearchHandler() {
		this.base = AmazonDataBase.getInstance();
	}

	/**
	 * handle the get method
	 * output the form
	 */
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		request.printRequest();
		// TODO Auto-generated method stub
		PrintWriter writer = okStatus(response);
		writer.write(simpleHeader("GetSearch"));
		writer.write(simpleForm("reviewsearch", KEY));
		writer.write(simpleFooter());
		System.out.println("finish writing");
	}

	/**
	 * handle the post method
	 * first check the request
	 * if the key is absent, return not enough parameter
	 * if the key have two or more components, search them all
	 * output the table if we have search result
	 */
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		request.printRequest();
		PrintWriter writer = okStatus(response);
		if(!checkParam(KEY, request, response)) {
			return;
		}
		String query = request.getParam(KEY).toLowerCase();
		String[] qs = query.split("\\s+");
		ArrayList<TreeMap<Integer, ArrayList<AmazonMessage>>> list = new ArrayList<TreeMap<Integer, ArrayList<AmazonMessage>>>();
		for(String q: qs) {
			TreeMap<Integer, ArrayList<AmazonMessage>> data = base.reviewSearch(q);
			if(data != null) {
				list.add(data);
			}
		}
		writer.write(simpleHeader("PostSearch"));
		
		//check if the we have the data
		if(list.size() == 0) {
			writer.write("<p>No such result</p>");
			writer.write(simpleFooter());
			return;
		}
		
		//write the result table;
		writer.write(TableStyle);
		writer.write("<tr><th>Word Frequency</th><th>Review Id</th><th>Score</th><th>Text</th></tr>");
		int count = 0;
		for(TreeMap<Integer, ArrayList<AmazonMessage>> data: list) {
			for(int i: data.keySet()) {
				for(AmazonMessage am: data.get(i)) {
					if(count >= LIMIT) {
						break;
					}
					AmazonReview ar = (AmazonReview) am;
					writer.write("<tr><td>" + i + "</td><td>" 
							+ StringEscapeUtils.escapeHtml4(ar.getReviewID()) + "</td><td>"
							+ StringEscapeUtils.escapeHtml4(ar.getScore()) + "</td><td>"
							+ StringEscapeUtils.escapeHtml4(ar.getText()) + "</td></tr>");
					count++;
				}
				if(count >= LIMIT) {
					break;
				}
			}
			if(count >= LIMIT) {
				break;
			}
		}
		writer.write("</table>");
		writer.write(simpleFooter());
	}

}
