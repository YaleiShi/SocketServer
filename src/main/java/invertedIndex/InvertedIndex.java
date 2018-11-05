package invertedIndex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * the data structure to store all the AmazonMessage in the json text
 * the data structure:
 * String          Frequency           ArrayList of AmazonMessage
 * cat   --------->   3      --------> {"cat cat cat", "cat dog cat cat"}
 *                    2      --------> {"cat cat", "doge cat cat", "cat cat dog"}
 * dog   --------->   2      --------> {"dog dog", "dog and dog"}
 *                    1      --------> {"cat dog cat cat", "cat cat dog"}
 * ......
 * ...... 
 * @author yalei
 *
 */
public class InvertedIndex {
	private HashMap<String, TreeMap<Integer, ArrayList<AmazonMessage>>> termIndex;
	private HashMap<String, ArrayList<AmazonMessage>> asinIndex;
	
	/**
	 * the consturctor of InvertedIndex
	 */
	public InvertedIndex() {
		this.termIndex = new HashMap<String, TreeMap<Integer, ArrayList<AmazonMessage>>>();
		this.asinIndex = new HashMap<String, ArrayList<AmazonMessage>>();
	}
	
	/**
	 * the function used to add one message into the termIndex and asinIndex;
	 */
	public void add(AmazonMessage message, HashMap<String, Integer> map) {
		this.addIntoTerm(message, map);
		this.addIntoAsin(message);
	}
	
	/**
	 * the function to add the message into the termIndex
	 * @param message the amazon message contains all the data
	 */
	public void addIntoTerm(AmazonMessage message, HashMap<String, Integer> map) {
		//add it into the termIndex by the hash map inside of it
		for(String s: map.keySet()) {
			int freq = map.get(s);
			if(!this.termIndex.containsKey(s)) {
				ArrayList<AmazonMessage> al = new ArrayList<AmazonMessage>();
				al.add(message);
				TreeMap<Integer, ArrayList<AmazonMessage>> tm = new TreeMap<Integer, ArrayList<AmazonMessage>>(new IntComparator());
				tm.put(freq, al);
				this.termIndex.put(s, tm);
			}else {
				if(!this.termIndex.get(s).containsKey(freq)) {
					ArrayList<AmazonMessage> al = new ArrayList<AmazonMessage>();
					al.add(message);
					this.termIndex.get(s).put(freq, al);
				}else {
					this.termIndex.get(s).get(freq).add(message);
				}
			}
		}
	}
	
	/**
	 * the function to add message into the asinIndex
	 * @param message the amazon message contains all the data
	 */
	public void addIntoAsin(AmazonMessage message) {
		//add it into the asinIndex by the asin inside of the message
		String asin = message.getAsin();
		if(!this.asinIndex.containsKey(asin)) {
			ArrayList<AmazonMessage> al = new ArrayList<AmazonMessage>();
			al.add(message);
			this.asinIndex.put(asin, al);
		}else {
			this.asinIndex.get(asin).add(message);
		}	
	}
	
	/**
	 * get the term index
	 * @return the pointer to the term index
	 */
	public HashMap<String, TreeMap<Integer, ArrayList<AmazonMessage>>> getTermIndex() {
		return termIndex;
	}

	/**
	 * get the asin index
	 * @return the pointer to the asin index
	 */
	public HashMap<String, ArrayList<AmazonMessage>> getAsinIndex() {
		return asinIndex;
	}
	
	/**
	 * output the array list by the asin number
	 * @param asin asin number
	 * @return the array list with all the message
	 */
	public ArrayList<AmazonMessage> getAsinArray(String asin){
		ArrayList<AmazonMessage> al = this.asinIndex.get(asin);
		return al;
	}
	
	/**
	 * return the tree map under the given word
	 * @param term the word we are searching
	 * @return the tree map
	 */
	public TreeMap<Integer, ArrayList<AmazonMessage>> getTermMap(String term){
		return this.termIndex.get(term);
	}
	
	/**
	 * serve for the partial search, 
	 * traverse the tree map to search the string
	 * @param term the partial word
	 * @return the tree set having all the result
	 */
	public TreeSet<AmazonMessage> partialSearchResult(String term){
		TreeSet<AmazonMessage> ts = new TreeSet<AmazonMessage>();
		for(String s: this.termIndex.keySet()) {
			if(s.contains(term)) {
				System.out.println("we find: " + s);
				TreeMap<Integer, ArrayList<AmazonMessage>> tm = this.termIndex.get(s);
				for(ArrayList<AmazonMessage> al: tm.values()) {
					for(AmazonMessage am: al) {
						ts.add(am);
					}
				}
			}
		}
		return ts;
	}

	/**
	 * the function just used to debug
	 * print out the term list
	 */
	public void printTermList() {
		System.out.println("Term List: ");
		for(String s: this.termIndex.keySet()) {
			System.out.println("word: " + s + "; documents: ");
			for(int i: this.termIndex.get(s).keySet()){
				System.out.print("\tfreq: " + i);
				for(AmazonMessage am: this.termIndex.get(s).get(i)) {
					System.out.print(" " + am.getAsin());
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * the function just used to debug
	 * print out the asin list
	 */
	public void printAsinList() {
		System.out.println("Asin List: ");
		for(String s: this.asinIndex.keySet()) {
			System.out.print("word: " + s + "; documents: ");
			ArrayList<AmazonMessage> ts = this.asinIndex.get(s);
			for(AmazonMessage am: ts) {
				System.out.print(am.getAsin() + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * the private  class just used to make the treeset sort by large to small
	 * @author yalei
	 *
	 */
	private class IntComparator implements Comparator<Integer>{

		@Override
		public int compare(Integer o1, Integer o2) {
			// TODO Auto-generated method stub
			return o2 - o1;
		}
		
	}

}
