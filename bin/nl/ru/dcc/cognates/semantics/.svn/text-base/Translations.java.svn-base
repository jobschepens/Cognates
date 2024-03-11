package nl.ru.dcc.cognates.semantics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.cognates.exceptions.TooManyTranslationsException;
import nl.ru.dcc.cognates.semantics.dictionary.DictionaryReaderException;
import nl.ru.dcc.cognates.semantics.dictionary.MyDictionary;
import nl.ru.dcc.cognates.types.AbcTranslation;
import nl.ru.dcc.cognates.types.SemanticInfo;
import nl.ru.dcc.cognates.types.Info;
import nl.ru.dcc.cognates.types.Syntax;
import nl.ru.nici.euroglot.dictionary.Reading;
import nl.ru.nici.euroglot.dictionary.Relation;
import nl.ru.nici.euroglot.dictionary.Word;

/**
 * @author lpxjjs1 (J.&nbsp;J. Schepens)
 *
 */
public class Translations extends Semantics {

	private Map<String, Word> sourceLanguageWords;
	private int step=0;

	/**
	 * used for extracting translations from the specified language
	 * @param wordSource 
	 * @param source language
	 * @param destination language
	 * @throws DictionaryReaderException 
	 */
	public Translations(Language s, Language d, int maxTranslations, 
			double threshold, int minLength, int maxLength, 
			double minFrequency, String freqType, String wordSource, 
			boolean countMatches, double simRange) throws DictionaryReaderException {
		
		super(s, d, maxTranslations, threshold, minLength, maxLength, minFrequency, 
				freqType, countMatches, simRange);

		if (wordSource.equals("limited")) {
			LOGGER.info("Reading dictionary: " + l1);
		    sourceLanguage=new MyDictionary("data/limited/lim"+l1+".xml", wordSource, false);
		    LOGGER.info("Reading dictionary: " + l2);
		    destinationLanguage=new MyDictionary("data/limited/lim"+l1+".xml", wordSource, false);
		}
		else {
			LOGGER.info("Reading dictionary: " + l1);
		    sourceLanguage=new MyDictionary("data/"+l1+".enc", wordSource, false);
		    LOGGER.info("Reading dictionary: " + l2);
		    destinationLanguage=new MyDictionary("data/"+l2+".enc", wordSource, false);
		}
		sourceLanguageWords = sourceLanguage.getWordByExpression();
	}
	
	/**
	 * used for extracting translations of the single words from a specified file
	 * @param l1
	 * @param l2
	 * @param maxTranslationPairs
	 * @param oThreshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 * @param wordType
	 * @param wordSource
	 * @param countMatches 
	 */
	public Translations(Language l1, Language l2, int maxTranslationPairs,
			double threshold, int minLength, int maxLength,
			double minFrequency, String freqType, String wordType, String wordSource, 
			boolean countMatches, double simRange) throws DictionaryReaderException {
		super(l1, l2, maxTranslationPairs, threshold, minLength, maxLength,
				minFrequency, freqType, countMatches, simRange);
		
		LOGGER.info("Extracting " + wordType + " from the " + l1 + " dictionary");
		
		LOGGER.info("Reading dictionary: " + l1);
	    sourceLanguage = new MyDictionary("data/"+l1+".enc", wordSource, false);
	    LOGGER.info("Reading dictionary: " + l2);
	    destinationLanguage=new MyDictionary("data/"+l2+".enc", wordSource, false);
	    
	    
		String filename="data/" + wordSource;
		Map<String, Word> tempSourceLanguageWords = sourceLanguage.getWordByExpression(); 
		sourceLanguageWords = new HashMap<String, Word>();
		Integer lengthOfList = 0, notFound=0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = br.readLine(); //skip header
			while ((line=br.readLine()) != null) {
				lengthOfList+=1;
				String[] list = line.split("\\t");
				String expression = list[0];
				Word word=tempSourceLanguageWords.get(expression);
				if (word!=null) {
					sourceLanguageWords.put(expression, word);
				} else notFound++;
			}
			LOGGER.info("Imported " + lengthOfList + " words from " + filename + ", " + notFound + " words were not found in Euroglot.");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * summary <p> newline
	 * 
	 * 
	 * @throws TooManyTranslationsException
	 */
	public void obtainData() throws TooManyTranslationsException {
		
		LOGGER.info("Imported " + sourceLanguageWords.keySet().size() + " different words from " + l1);
		dicStat.setSourceDictionarySize(sourceLanguageWords.keySet().size());
		LOGGER.info("Imported " + destinationLanguage.getWordByExpression().size() + " different words from " + l2);
		dicStat.setDestDictionarySize(destinationLanguage.getWordByExpression().size());
		LOGGER.info("Translating");
		for(String expression:sourceLanguageWords.keySet())
	    {
			if (validateSpelling(expression, l1)) {
		      Word word=sourceLanguageWords.get(expression);
		      
		      Reading[] reading=word.getReadingArray();
		      for(int i=0;i<reading.length;i++)
		      {
				if(parameters.isCOUNTMATCHES()) {
					countMatches(expression);
					
				} else { 
		        String concept=reading[i].getConcept();
		        List<Word> translations=destinationLanguage.getTranslation(concept);
		        for(Word translation:translations)
		        {
		        	//at this point we have translation pairs
		        	if (validateSpelling(translation.getExpression(), l2)) {
						Reading[] trReading = translation.getReadingArray();
						for(int k=0;k<trReading.length;k++) {
							if (trReading[k].getConcept().equals(concept)) {
								Relation[] relation=reading[i].getRelationArray();
								Relation[] trRelation=trReading[k].getRelationArray();													
								for (int j=0;j<relation.length;j++) {													
				        			for (int l=0;l<trRelation.length;l++) {
				        				double distance;
				        				//don't compute distance here if there is no threshold anyway
				        				if (parameters.getOTHRESHOLD()!=0) {
				        					distance = getODistance(expression.toLowerCase(), translation.getExpression().toLowerCase());
										} else distance = 1;
										if (distance>=parameters.getOTHRESHOLD()) {
											if (linguisticData.size()<parameters.getMaxPairs()) {
						        				AbcTranslation abcTranslation = setABC(relation[j], trRelation[l]);
						        				if (abcTranslation.toString().equals("a")) {
						        					Syntax sourceSyntax = getSyntax(reading[i]);
													Syntax destinationSyntax = getSyntax(trReading[k]);
													//simply added a meaning to the specific translation pair
													if (existingTranslationPair(
															expression, translation.getExpression(), 
															sourceSyntax, destinationSyntax,
															abcTranslation, relation[j], concept)) {
														dicStat.setEqt(dicStat.getEqt() + 1);
													}
													else {
														List<String> meanings = findRelatedMeaningInSource(relation[j], concept, expression);
														
														Info info = new SemanticInfo(expression,translation.getExpression(), 
																sourceSyntax, destinationSyntax, abcTranslation, meanings);
														//add a new translation pair to the collection
														linguisticData.put(dicStat.getIdentifiedPairs(), info);
														dicStat.setIdentifiedPairs(dicStat.getIdentifiedPairs() + 1);
														
														if (dicStat.getIdentifiedPairs() > (step+1)*(10000)) {
															step++;
															System.out.print( step*10000 + ", ");
														}
														
													}
						        				}
											} else throw new TooManyTranslationsException("Already more than '%s' identified", parameters.getMaxPairs());
											dicStat.setTrAboveThreshold(dicStat.getTrAboveThreshold() + 1);
										}
										dicStat.setValidatedTrPairs(dicStat.getValidatedTrPairs() + 1);
				        			}
								}
							} 
						}
		        	} else dicStat.setInvalidTr(dicStat.getInvalidTr() + 1);
		        }
		      }
		      }
		      dicStat.setValidatedSrc(dicStat.getValidatedSrc() + 1);
			} else dicStat.setInvalidSrc(dicStat.getInvalidSrc() + 1);
	    }
		LOGGER.info(dicStat.getValidatedSrc() + " valid expressions");
		LOGGER.info(dicStat.getInvalidSrc() + " invalid expressions: smaller than " + parameters.getMINLENGTH() + ", " +
				"bigger than " + parameters.getMAXLENGTH() + ", starting with an uppercase letter, " +
				"or have disallowed characters (* , '  )");
		LOGGER.info(dicStat.getValidatedTrPairs() + " valid translation pairs");
		LOGGER.info(dicStat.getInvalidTr() + " invalid translations: smaller than " + parameters.getMINLENGTH() + ", " +
				"bigger than " + parameters.getMAXLENGTH() + ", starting with an uppercase letter, " +
				"or have disallowed characters (* , '  )");
		LOGGER.info(dicStat.getAtr() + " a translations, " + 
				dicStat.getBtr() + " b translations, " + "and " + 
				dicStat.getCtr() + " c translations " + "have been found " +
				"(note that a translations overwrite equal b and c translations when exporting translation pairs)");
		LOGGER.info(dicStat.getEqt() + " related source expressions have been added to the translation pairs " +
				"(1 for each shared meaning, and only different expressions)");
		int uniqueSourceExpressions = countUniqueSources();
		int uniqueDestExpressions = countUniqueDestinations();
		int validDestExpressions = countValidDestinations();
		dicStat.setUniqueSourceExpressions(uniqueSourceExpressions);
		dicStat.setUniqueDestExpressions(uniqueDestExpressions);
		dicStat.setValidTr(validDestExpressions);
		LOGGER.info("Obtained " + dicStat.getIdentifiedPairs() + " unique translation pairs of " + uniqueSourceExpressions + " different expressions\n");
	}

	/**
	 * @param expression 
	 * @param relation 
	 * @param concept 
	 * 
	 */
	private void countMatches(String expression) {
		String translation="aap";
		Info info = new SemanticInfo(expression,translation, 
				null, null, new AbcTranslation("a"), null);
		//add a new translation pair to the collection
		if(!existingSource(expression)) {
			linguisticData.put(dicStat.getIdentifiedPairs(), info);
			dicStat.setIdentifiedPairs(dicStat.getIdentifiedPairs() + 1);
		}
		
		if (dicStat.getIdentifiedPairs() > (step+1)*(10000)) {
			step++;
			System.out.print( step*10000 + ", ");
		}
	}

	private boolean existingSource(String expression){
		for (Integer id:linguisticData.keySet()) {
			SemanticInfo info = (SemanticInfo) linguisticData.get(id);
			if (info.getExpression().equals(expression)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return
	 */
	private int countValidDestinations() {
		int validDest = 0;
		for (String expression:destinationLanguage.getWordByExpression().keySet()) {
			if (validateSpelling(expression,l2)) {
				validDest++;
			}
		}
		return validDest;
	}

	/**
	 * @return
	 */
	private int countUniqueDestinations() {
		HashMap<String,AbcTranslation> unique = new HashMap<String,AbcTranslation>();
		for (Integer id:linguisticData.keySet()) {
			if (linguisticData.get(id) instanceof SemanticInfo) {
				SemanticInfo e = ((SemanticInfo) linguisticData.get(id));
				if (!unique.containsKey(e)) {
					unique.put(e.getTranslation(), e.getAbcTranslation());
				}
			}
		}
		for (String e:unique.keySet()) {
			if(unique.get(e).toString() == "a") {
				dicStat.setUniqueATr(dicStat.getUniqueATr() + 1);
			}
		}
		return unique.size();
	}

	/**
	 * @return
	 */
	private int countUniqueSources() {
		HashMap<String,AbcTranslation> unique = new HashMap<String,AbcTranslation>();
		for (Integer id:linguisticData.keySet()) {
			if (linguisticData.get(id) instanceof SemanticInfo) {
				SemanticInfo e = ((SemanticInfo) linguisticData.get(id));
				if (!unique.containsKey(e)) {
					unique.put(e.getExpression(), e.getAbcTranslation());
				}
				if(((SemanticInfo) linguisticData.get(id)).getAbcTranslation().toString() == "a") {
					dicStat.setUniqueAPairs(dicStat.getUniqueAPairs() + 1);
				}
			}
		}
		for (String e:unique.keySet()) {
			if(unique.get(e).toString() == "a") {
				dicStat.setUniqueASrc(dicStat.getUniqueASrc() + 1);
			}
		}
		return unique.size();
	}

	/**
	 * @param srcExpression
	 * @param destExpression
	 * @return
	 */
	public boolean isTranslation(String srcExpression, String destExpression) {
		for (Integer id:linguisticData.keySet()) {
			SemanticInfo info = (SemanticInfo) linguisticData.get(id);
			if (info.getExpression().equals(srcExpression) && info.getTranslation().equals(destExpression)) {
//				System.out.println("ok");
				return true;
			}
		}
		return false;
	}
	
//	public void printHead(int headsize) {
//		LOGGER.info("Printing the head of " + linguisticData.size() + " values");
//		int t=0;
//		for (Integer id:linguisticData.keySet()) {
//			if (t<headsize) {
//				String printInfo = null; 
//				printInfo = linguisticData.get(id).getPrintInfoDictionary();
//				System.out.println(id+":"+printInfo);
//				t++;
//			}
//		}
//	}
//
//	public void exportDictionary() {
//		try
//	    {
//	      BufferedWriter cognateWriter=new BufferedWriter(new FileWriter("results/cognates "+l1+" "+l2+".out"));
//	      Vector<Integer> v=new Vector<Integer>(linguisticData.keySet());
//	      Collections.sort(v);
//	      for (Integer pair:v) {
//	    	  cognateWriter.write(pair+":"+linguisticData.get(pair).getPrintInfoDictionary());
//	    	  cognateWriter.newLine();
//	      }
//	      cognateWriter.close();
//	    }
//	    catch(IOException e1)
//	    {
//	      e1.printStackTrace();
//	    }
//	}
}
	
