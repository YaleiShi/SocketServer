package invertedIndex;

/**
 * the data structure to store the qa
 * @author yalei
 *
 */
public class AmazonQA extends AmazonMessage implements Comparable<AmazonQA>{
	protected String question;
	protected String answer;
	
	/**
	 * constructor of qa
	 * @param asin the asin number
	 * @param question the text of question
	 * @param answer the answer of the question
	 */
	public AmazonQA(String asin, String question, String answer) {
		super(asin);
		this.question = question;
		this.answer = answer;
	}

	/**
	 * get the text of question
	 * @return the question text
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * get the text of answer
	 * @return the answer text
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * compare two AmazonQA
	 */
	@Override
	public int compareTo(AmazonQA o) {
		// TODO Auto-generated method stub
		if(this.asin.compareTo(o.asin) != 0) return this.asin.compareTo(o.asin);
		if(this.answer.compareTo(o.answer) != 0) return this.answer.compareTo(o.answer);
		return this.question.compareTo(o.question);
	}
	
	

}
