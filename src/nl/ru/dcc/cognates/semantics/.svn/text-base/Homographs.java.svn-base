/**
 * 
 */
package nl.ru.dcc.cognates.semantics;

import java.util.ArrayList;
import java.util.Map;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.cognates.exceptions.TooManyTranslationsException;
import nl.ru.dcc.cognates.semantics.dictionary.DictionaryReaderException;
import nl.ru.dcc.cognates.semantics.dictionary.MyDictionary;
import nl.ru.dcc.cognates.types.AbcTranslation;
import nl.ru.dcc.cognates.types.SemanticInfo;
import nl.ru.dcc.cognates.types.Info;
import nl.ru.dcc.cognates.types.Syntax;
import nl.ru.nici.euroglot.dictionary.Word;

/**
 * @author lpxjjs1
 *
 */
public class Homographs extends Semantics {

	/**
	 * @param l1
	 * @param l2
	 * @param maxHomographs
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 * @throws DictionaryReaderException 
	 */
	public Homographs(Language l1, Language l2, int maxHomographs,
			double threshold, int minLength, int maxLength,
			double minFrequency, String freqType, String wordSource, double simRange) 
			throws DictionaryReaderException {
		super(l1, l2, maxHomographs, threshold, minLength, maxLength, minFrequency,
				freqType, false, simRange);
		
		if (wordSource.equals("limited")) {
			LOGGER.info("Reading dictionary: " + l1);
		    sourceLanguage=new MyDictionary("data/limited/lim"+l1+".xml", wordSource, false);
		    LOGGER.info("Reading dictionary: " + l2);
		    destinationLanguage=new MyDictionary("limited/lim"+l1+".xml", wordSource, false);
		}
		else {
			LOGGER.info("Reading dictionary: " + l1);
		    sourceLanguage=new MyDictionary("data/"+l1+".enc", wordSource, false);
		    LOGGER.info("Reading dictionary: " + l2);
		    destinationLanguage=new MyDictionary("data/"+l2+".enc", wordSource, false);
		}
	}

	public void obtainData() throws TooManyTranslationsException {

		int step = 0;
		Map<String, Word> sourceLanguageWords=sourceLanguage.getWordByExpression();
		Map<String, Word> destinationLanguageWords=destinationLanguage.getWordByExpression();
		LOGGER.info("Imported " + sourceLanguageWords.size() + " words from " + l1);
		LOGGER.info("Imported " + destinationLanguage.getWordByExpression().size() + " words from " + l2);
		LOGGER.info("Searching homographs");
		for(String srcExpression:sourceLanguageWords.keySet()) {
			if (validateSpelling(srcExpression,l1)) {
				if (parameters.getOTHRESHOLD()==1.0) {
					Word destWord = getDestWord(destinationLanguageWords, srcExpression);
					if (destWord != null){
					    String destExpression = destWord.getExpression();					      
						if (validateSpelling(destExpression,l2)) {
							Syntax destSyntax = new Syntax("");
							Syntax srcSyntax = new Syntax("");
							Info info = new SemanticInfo(srcExpression,destExpression, 
									srcSyntax, destSyntax, new AbcTranslation("_"), 
									new ArrayList<String>());								
							linguisticData.put(dicStat.getIdentifiedPairs(), info);
							dicStat.setIdentifiedPairs(dicStat.getIdentifiedPairs() + 1);
							if (dicStat.getIdentifiedPairs()> (step+1)*(sourceLanguageWords.keySet().size()/20)) {
								step++;
								System.out.print( step*5 + "%, ");
							}
							dicStat.setValidatedTrPairs(dicStat.getValidatedTrPairs() + 1);
						} else dicStat.setInvalidTr(dicStat.getInvalidTr() + 1);
					}
					dicStat.setTrAboveThreshold(dicStat.getTrAboveThreshold() + 1);
				} else {
					for(String destExpression:destinationLanguageWords.keySet()) {
						if (validateSpelling(destExpression,l2)) {
	//						Word sourceWord=sourceLanguageWords.get(srcExpression);
	//						Word destWord=destinationLanguageWords.get(destExpression);
	//					    Reading[] srcReading=sourceWord.getReadingArray();
	//					    Reading[] destReading=destWord.getReadingArray();
	//					    Set<Syntax> srcSyntaxes = new HashSet<Syntax>();
	//					    Set<Syntax> destSyntaxes = new HashSet<Syntax>();
	//					    for(int i=0;i<srcReading.length;i++) {
	//					    	srcSyntaxes.add(getSyntax(srcReading[i]));
	//					    }
	//					    for(int i=0;i<destReading.length;i++) {
	//					    	destSyntaxes.add(getSyntax(destReading[i]));
	//					    }

							double distance;
	        				if (parameters.getOTHRESHOLD()!=0) {
	        					distance = getODistance(srcExpression.toLowerCase(), destExpression.toLowerCase());
							} else distance = 1;
	        				if (distance>parameters.getOTHRESHOLD()) {
								if (linguisticData.size()<parameters.getMaxPairs()) {
									
									Syntax destSyntax = new Syntax("");
									Syntax srcSyntax = new Syntax("");
									
									Info info = new SemanticInfo(srcExpression,destExpression, 
											srcSyntax, destSyntax, new AbcTranslation("_"), 
											new ArrayList<String>());								
									//add a new translation pair to the collection
									linguisticData.put(dicStat.getIdentifiedPairs(), info);
									dicStat.setIdentifiedPairs(dicStat.getIdentifiedPairs() + 1);
									if (dicStat.getIdentifiedPairs()> (step+1)*(sourceLanguageWords.keySet().size()/20)) {
										step++;
										System.out.print( step*5 + "%, ");
									}
								}  else throw new TooManyTranslationsException(
										"Already more than '%s' identified", parameters.getMaxPairs());
								dicStat.setTrAboveThreshold(dicStat.getTrAboveThreshold() + 1);
							}
							dicStat.setValidatedTrPairs(dicStat.getValidatedTrPairs() + 1);
						} else dicStat.setInvalidTr(dicStat.getInvalidTr() + 1);
					}
				}
				dicStat.setValidatedSrc(dicStat.getValidatedSrc() + 1);
			} else dicStat.setInvalidSrc(dicStat.getInvalidSrc() + 1);
	    }
		System.out.println("\n");
		LOGGER.info(dicStat.getValidatedSrc() + " valid expressions");
		LOGGER.info(dicStat.getInvalidSrc() + " invalid expressions: smaller than " + parameters.getMINLENGTH() + ", " +
				"bigger than " + parameters.getMAXLENGTH() + ", starting with an uppercase letter, " +
				"or have disallowed characters (* , '  )");
		LOGGER.info(dicStat.getValidatedTrPairs() + " valid homographs");
		LOGGER.info(dicStat.getInvalidTr() + " invalid homographs: smaller than " + parameters.getMINLENGTH() + ", " +
				"bigger than " + parameters.getMAXLENGTH() + ", starting with an uppercase letter, " +
				"or have disallowed characters (* , '  )");
		LOGGER.info("Obtained " + dicStat.getIdentifiedPairs() + " homographs\n");
	}
	
	/**
	 * @param destinationLanguageWords 
	 * @param srcExpression 
	 * @return
	 */
	private Word getDestWord(Map<String, Word> destinationLanguageWords, String srcExpression) {
		Word destWord = destinationLanguageWords.get(srcExpression);
		if (destWord == null){
		      String first_letter = srcExpression.substring(0,1);
		      String rest = srcExpression.substring(1,srcExpression.length());
		      if (first_letter == first_letter.toUpperCase()) {
		        String w = first_letter.toLowerCase();
		        srcExpression = w.concat(rest); 
		      }
		      else if (first_letter == first_letter.toLowerCase()) {
		        String w = first_letter.toUpperCase();
		        srcExpression = w.concat(rest); 
		      }
		      destWord = destinationLanguageWords.get(srcExpression);
		    }
		return destWord;
	}
//
//	/**
//	 * @param srcSyntaxes
//	 * @return
//	 */
//	private Syntax concatSyntaxes(Set<Syntax> srcSyntaxes) {
//		
//		String cc = "";
//		Iterator<Syntax> it = srcSyntaxes.iterator();
//		if (!it.hasNext()) {
//			cc = "_";
//		}
//		while (it.hasNext()) {
//			Syntax next = it.next();
//			if (!next.toString().equals("")) {
//				if (!it.hasNext()) {
//					cc = cc + next.toString();
//				} else {
//					cc = cc + next.toString() + ",";
//				}
//			}
//		}
//		return new Syntax(cc);
//	}
//
//	private Syntax getSyntax(Reading reading) {
//		Syntax syntax = new Syntax("");
////		if (reading.getPos().getCategory().toString() != null) {
////			syntax.set(reading.getPos().getCategory().toString());
////		}
//		return syntax;
//	}
}
