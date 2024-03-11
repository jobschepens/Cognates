package nl.ru.dcc.cognates.frequencies;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.client.LexiconName;
import nl.ru.dcc.cognates.AbstractLinguisticData;
import nl.ru.dcc.cognates.exceptions.FregTypeException;
import nl.ru.dcc.cognates.types.LexicalInfo;
import nl.ru.dcc.cognates.types.Phono;

public abstract class Lexicon extends AbstractLinguisticData {

	protected FrequencyStatistics freqStats;
	
	/**
	 * @param l
	 * @param lexiconName
	 * @param threshold
	 * @param minLength
	 * @param freqType
	 * @param minFrequency
	 * @param maxLength
	 * @param maxCognates
	 */
	public Lexicon(Language l, LexiconName lexiconName, int maxCognates, double threshold, 
			int minLength, int maxLength, double minFrequency, String freqType) {
		super(l, null, maxCognates, threshold, minLength, maxLength, minFrequency, freqType);
		freqStats = new FrequencyStatistics(0, 0, 0, 0, 0, 0, 0, lexiconName);
	}
	
	/**
	 * imports word frequencies from a database
	 * @throws FregTypeException 
	 */
	public abstract void importFrequencies() throws FregTypeException;
	
	public int getCandidateFromLexicon(String expression) {
		
		int candidate = -1, moreThanOnce=0;
		
		//for every frequency
		for (Integer id:linguisticData.keySet()) {
			LexicalInfo infoFr = (LexicalInfo) linguisticData.get(id);
			
			//compare candidate with source	
			if (infoFr.getLexiconItem().toString().toLowerCase().equals(expression.toLowerCase())) {
				//TODO LinkedData: Syntax, match between lexicon and dictionary
				//if a candidate has already been found 
				if (moreThanOnce>0) {
					//more options available, choose largest frequency
					if (infoFr.getFrequency().toDouble() > ((LexicalInfo) linguisticData.get(candidate)).getFrequency().toDouble()) {
						candidate = id;
					} //else candidate stays the same
				} else candidate = id;
				moreThanOnce++;
			}
		}
		if (candidate == -1) {
			//TODO LinkedData: Wordform, find close match 
		}
		if (candidate != -1) {
			freqStats.setTotalConflicts(freqStats.getConflicts() + (moreThanOnce - 1));
			LexicalInfo infoFr = (LexicalInfo) linguisticData.get(candidate);
			infoFr.setNumberOfConflicts(moreThanOnce - 1);
			//TODO Stats: this statistic is only almost equal to the total of single word conflicts
			linguisticData.put(candidate, infoFr); 
		}
		return candidate;
	}

	/**
	 * @param id
	 * @return
	 */
	public LexicalInfo getInfo(Integer id) {
	
//		if (linguisticData.get(id) == null){
//			throw new RuntimeException("Missing link '"+id+"'");
//		}
		if (id != null) {
			if (linguisticData.get(id) instanceof LexicalInfo)
				return (LexicalInfo) linguisticData.get(id);
		}
		return null;
	}
	
//	public Frequency getFrequencyFromLexicon(int sourceLexiconID) {
//		if (linguisticData.get(sourceLexiconID) == null){
//			System.out.println("error");
//		}
//		return linguisticData.get(sourceLexiconID).getFrequency();
//	}

	public int getNumberOfConflicts() {
		return freqStats.getConflicts();
	}

	public void resetNumberOfConflicts() {
		freqStats.setTotalConflicts(0);
	}

	public void computeAVG() {
		double averageF=0;
		for (Integer id:linguisticData.keySet()) {
			LexicalInfo info = (LexicalInfo) linguisticData.get(id);
			averageF = averageF + info.getFrequency().toDouble();
		}
		averageF = averageF/linguisticData.size();
		freqStats.setAverageF(averageF);
	}

	public FrequencyStatistics getStats() {
		return freqStats;
	}
	
	/**
	 * @param phonStrs
	 * @return
	 */
	protected Phono removeStresses(Phono phono) {
		if (phono != null) {
			String phon = phono.toString();	
			phon = phon.replace(" ", ""); 	//remove spaces
			phon = phon.replace("-", ""); 	//remove syllables markers
			phon = phon.replace("'", ""); 	//remove primary stress markers
			phon = phon.replace("\"", "");	//remove secondary stress markers
			return new Phono(phon);
		} else return new Phono("NA");
	}
}
