package nl.ru.dcc.cognates.semantics;

public class DictionaryStats {
	private int id;
	private int th;
	private int atr;
	private int btr;
	private int ctr;
	private int eqt;
	private int invalidSrc;
	private int invalidTr;
	private int validatedSrc;
	private int validatedTrPairs;
	private int trAboveThreshold;
	private int uniqueSourceExpressions;
	private int sourceDictionarySize;
	private int destDictionarySize;
	private int uniqueDestExpressions;
	private int validatedTr;
	private int uniqueASrc;
	private int uniqueATr;
	private int uniqueAPairs;

	public DictionaryStats(int id, int th, int atr, int btr, int ctr, int eqt,
			int invalidSrc, int invalidTr, int validatedSrc, int validatedTr, int trAboveThreshold) {
		this.id = id;
		this.th = th;
		this.atr = atr;
		this.btr = btr;
		this.ctr = ctr;
		this.eqt = eqt;
		this.invalidSrc = invalidSrc;
		this.invalidTr = invalidTr;
		this.validatedSrc = validatedSrc;
		this.validatedTrPairs = validatedTr;
		this.trAboveThreshold = trAboveThreshold;
	}

	public int getIdentifiedPairs() {
		return id;
	}

	public void setIdentifiedPairs(int id) {
		this.id = id;
	}

	public int getTh() {
		return th;
	}

	public void setTh(int th) {
		this.th = th;
	}

	public int getAtr() {
		return atr;
	}

	public void setAtr(int atr) {
		this.atr = atr;
	}

	public int getBtr() {
		return btr;
	}

	public void setBtr(int btr) {
		this.btr = btr;
	}

	public int getCtr() {
		return ctr;
	}

	public void setCtr(int ctr) {
		this.ctr = ctr;
	}

	public int getEqt() {
		return eqt;
	}

	public void setEqt(int eqt) {
		this.eqt = eqt;
	}

	public int getInvalidSrc() {
		return invalidSrc;
	}

	public void setInvalidSrc(int unvalidSrc) {
		this.invalidSrc = unvalidSrc;
	}

	public int getInvalidTr() {
		return invalidTr;
	}

	public void setInvalidTr(int unvalidTr) {
		this.invalidTr = unvalidTr;
	}

	public int getValidatedSrc() {
		return validatedSrc;
	}

	public void setValidatedSrc(int validatedSrc) {
		this.validatedSrc = validatedSrc;
	}

	public int getValidatedTrPairs() {
		return validatedTrPairs;
	}

	public void setValidatedTrPairs(int validatedTr) {
		this.validatedTrPairs = validatedTr;
	}

	public int getTrAboveThreshold() {
		return trAboveThreshold;
	}

	public void setTrAboveThreshold(int trAboveThreshold) {
		this.trAboveThreshold = trAboveThreshold;
	}

	/**
	 * @param uniqueSourceExpressions the uniqueSourceExpressions to set
	 */
	public void setUniqueSourceExpressions(int uniqueSourceExpressions) {
		this.uniqueSourceExpressions = uniqueSourceExpressions;
	}

	/**
	 * @return the uniqueSourceExpressions
	 */
	public int getUniqueSourceExpressions() {
		return uniqueSourceExpressions;
	}

	/**
	 * @return
	 */
	public int getSourceDictionarySize() {
		return sourceDictionarySize;
	}

	/**
	 * @param i
	 */
	public void setSourceDictionarySize(int i) {
		this.sourceDictionarySize = i;
	}

	/**
	 * @return
	 */
	public int getDestDictionarySize() {
		return destDictionarySize;
	}
	/**
	 * @param i
	 */
	public void setDestDictionarySize(int i) {
		this.destDictionarySize = i;
	}

	/**
	 * @param uniqueSourceExpressions the uniqueSourceExpressions to set
	 */
	public void setUniqueDestExpressions(int uniqueDestExpressions) {
		this.uniqueDestExpressions = uniqueDestExpressions;
	}

	/**
	 * @return the uniqueSourceExpressions
	 */
	public int getUniqueDestExpressions() {
		return uniqueDestExpressions;
	}

	/**
	 * @param validDestExpressions
	 */
	public void setValidTr(int validDestExpressions) {
		this.validatedTr = validDestExpressions;
	}

	/**
	 * @return
	 */
	public int getValidatedDest() {
		return validatedTr;
	}

	/**
	 * @param uniqueASrc the uniqueASrc to set
	 */
	public void setUniqueASrc(int uniqueASrc) {
		this.uniqueASrc = uniqueASrc;
	}

	/**
	 * @return the uniqueASrc
	 */
	public int getUniqueASrc() {
		return uniqueASrc;
	}

	/**
	 * @param uniqueATr the uniqueATr to set
	 */
	public void setUniqueATr(int uniqueATr) {
		this.uniqueATr = uniqueATr;
	}

	/**
	 * @return the uniqueATr
	 */
	public int getUniqueATr() {
		return uniqueATr;
	}

	/**
	 * @param uniqueAPairs the uniqueAPairs to set
	 */
	public void setUniqueAPairs(int uniqueAPairs) {
		this.uniqueAPairs = uniqueAPairs;
	}

	/**
	 * @return the uniqueAPairs
	 */
	public int getUniqueAPairs() {
		return uniqueAPairs;
	}
}