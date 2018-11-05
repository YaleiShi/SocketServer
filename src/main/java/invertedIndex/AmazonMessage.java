package invertedIndex;
import java.util.HashMap;

/**
 * the parent class of amazon qa and review
 * @author yalei
 *
 */
public abstract class AmazonMessage {
	protected String asin;
	
	/**
	 * take the asin to construct
	 * @param asin the asin number of the message
	 */
	public AmazonMessage(String asin) {
		this.asin = asin;
	}

	/**
	 * get the asin
	 * @return
	 */
	public String getAsin() {
		return asin;
	}
	
	
}
