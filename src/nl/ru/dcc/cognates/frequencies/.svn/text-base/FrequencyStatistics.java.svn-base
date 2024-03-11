package nl.ru.dcc.cognates.frequencies;

import nl.ru.dcc.client.LexiconName;

public class FrequencyStatistics {
	private int lowFreq;
	private int tooBig;
	private int tooSmall;
	private int notAllowed;
	private int lexiconSize;
	private int id;
	private int conflicts;
	private LexiconName name;
	private double averageF;

	/**
	 * @param lowFreq
	 * @param tooBig
	 * @param tooSmall
	 * @param notAllowed
	 * @param lexiconSize
	 * @param id
	 * @param conflicts
	 * @param lexiconName
	 */
	public FrequencyStatistics(int lowFreq, int tooBig, int tooSmall,
			int notAllowed, int lexiconSize, int id, int conflicts, LexiconName lexiconName) {
		this.lowFreq = lowFreq;
		this.tooBig = tooBig;
		this.tooSmall = tooSmall;
		this.notAllowed = notAllowed;
		this.lexiconSize = lexiconSize;
		this.id = id;
		this.conflicts=conflicts;
		this.name = lexiconName;
	}

	public int getConflicts() {
		return conflicts;
	}

	public void setTotalConflicts(int conflicts) {
		this.conflicts = conflicts;
	}
	
	public int getIdentifiedFrequencies() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getLowFreq() {
		return lowFreq;
	}

	public void setLowFreq(int lowFreq) {
		this.lowFreq = lowFreq;
	}

	public int getTooBig() {
		return tooBig;
	}

	public void setTooBig(int tooBig) {
		this.tooBig = tooBig;
	}

	public int getTooSmall() {
		return tooSmall;
	}

	public void setTooSmall(int tooSmall) {
		this.tooSmall = tooSmall;
	}

	public int getNotAllowed() {
		return notAllowed;
	}

	public void setNotAllowed(int notAllowed) {
		this.notAllowed = notAllowed;
	}

	public int getLexiconSize() {
		return lexiconSize;
	}

	public void setLexiconSize(int lexiconSize) {
		this.lexiconSize = lexiconSize;
	}

	public String getName() {
		switch (name) {
			case Celex:
				return "Celex";
			case Lexique:
				return "Lexique";
			case SUBTLEXus:
				return "SUBTLEXus";
			case BPal:
				return "BPal";
			case CoLFIS:
				return "CoLFIS";
		}
		return null;
	}

	/**
	 * @param averageF the averageF to set
	 */
	public void setAverageF(double averageF) {
		this.averageF = averageF;
	}

	/**
	 * @return the averageF
	 */
	public double getAverageF() {
		return averageF;
	}
}