/**
 * 
 */
package nl.ru.dcc.cognates.types;



/**
 * @author lpxjjs1
 *
 */
public class LinkedInfo implements Info {

	private Integer sourceLexiconID;
	private Integer destinationLexiconID;
	private Integer dictionaryID;

	/**
	 * @param dictionaryID
	 * @param sourceLexiconID
	 * @param destinationLexiconID
	 */
	public LinkedInfo(Integer dictionaryID, int sourceLexiconID,
			Integer destinationLexiconID) {
		if (sourceLexiconID != -1 && destinationLexiconID == -1) {
			this.sourceLexiconID = sourceLexiconID;
			this.destinationLexiconID = null;
		}
		if (sourceLexiconID == -1 && destinationLexiconID != -1) {
			this.sourceLexiconID = null;
			this.destinationLexiconID = destinationLexiconID;
		}
		if (sourceLexiconID != -1 && destinationLexiconID != -1) {
			this.sourceLexiconID = sourceLexiconID;
			this.destinationLexiconID = destinationLexiconID;
		}
		this.dictionaryID = dictionaryID;
	}

	/**
	 * @return
	 */
	public Integer getSourceLexiconID() {
		return sourceLexiconID;
	}
	
	/**
	 * @return
	 */
	public Integer getDestinationLexiconID() {
		return destinationLexiconID;
	}

	/**
	 * @return
	 */
	public Integer getDictionaryID() {
		return dictionaryID;
	}
}
