package nl.ru.dcc.cognates.datalink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.cognates.AbstractLinguisticData;
import nl.ru.dcc.cognates.exceptions.TooManyTranslationsException;
import nl.ru.dcc.cognates.frequencies.Lexicon;
import nl.ru.dcc.cognates.semantics.Semantics;
import nl.ru.dcc.cognates.types.SemanticInfo;
import nl.ru.dcc.cognates.types.Distance;
import nl.ru.dcc.cognates.types.LexicalInfo;
import nl.ru.dcc.cognates.types.LinkedInfo;
//import nl.ru.dcc.cognates.types.Syntax;

public class LinkedData extends AbstractLinguisticData {

//	private static double MAXFDIFF = 0;
	private LinkedDataStatistics linkStat;
	private int step=0;
	private Integer linkedDataID=0;

	/**
	 * @param src
	 * @param des
	 * @param freqType
	 * @param maxCognates
	 * @param minLength
	 * @param minFrequency
	 * @param oThreshold
	 * @param pThreshold 
	 * @param maxLength
	 * @param simRange 
	 * @throws TooManyTranslationsException
	 */
	public LinkedData(Language l1, Language l2, int maxCognates, double oThreshold, 
			double pThreshold, int minLength, int maxLength, double minFrequency, String freqType, 
			boolean countMatches, double simRange, double maxFDiff)	throws TooManyTranslationsException {
		super(l1, l2, maxCognates, oThreshold, pThreshold, minLength, maxLength, minFrequency, freqType, 
				countMatches, simRange);
		linkStat = new LinkedDataStatistics(0, 0, 0, 0);
//		MAXFDIFF = maxFDiff;
	}
	public void matchAllTranslations(Lexicon sourceLexicon, Lexicon destinationLexicon, Semantics dictionary) throws RuntimeException {	
			
		LOGGER.info("Comparing: " + l1 + "-" + l2);
		sourceLexicon.resetNumberOfConflicts();
		destinationLexicon.resetNumberOfConflicts();

		for (Integer dictionaryID:dictionary.getKeySet()) {			
			SemanticInfo dictionaryInfo = dictionary.getInfo(dictionaryID);
			
			if (parameters.isCOUNTMATCHES()) 
				countMatches(sourceLexicon, dictionaryInfo, dictionaryID,dictionary.getKeySet().size());
			else {
			
			double oDistance = getODistance(dictionaryInfo.getExpression().toLowerCase(), 
					dictionaryInfo.getTranslation().toLowerCase());
			Distance roundedODistance = new Distance(roundDistance(oDistance));
			dictionaryInfo.setODistance(roundedODistance);
			
			if (oDistance>=parameters.getOTHRESHOLD()) {
				int sourceLexiconID=-1;	
				sourceLexiconID = sourceLexicon.getCandidateFromLexicon(dictionaryInfo.getExpression());
				Integer destinationLexiconID = destinationLexicon.getCandidateFromLexicon(dictionaryInfo.getTranslation());
				LinkedInfo linkedInfo = new LinkedInfo(dictionaryID, sourceLexiconID, destinationLexiconID);
				LexicalInfo sourceInfo = null;
				LexicalInfo destInfo = null;				
				sourceInfo = sourceLexicon.getInfo(sourceLexiconID);
				destInfo = destinationLexicon.getInfo(destinationLexiconID);
//				if (compareF(sourceInfo.getFrequency().toDouble(),
//						destInfo.getFrequency().toDouble())) {
//					if (compareSyntax(sourceInfo.getLexSyntax(),destInfo.getLexSyntax())) {
				double pDistance=1;			
				if (sourceInfo == null || destInfo == null) {	
					dictionaryInfo.setPDistance(new Distance("NA"));
				} else {
					//compute p distance with character similarity
					pDistance = getPDistance(sourceInfo.getPhono().toString(), 
							destInfo.getPhono().toString());
					Distance roundedPDistance = new Distance(roundDistance(pDistance ));
					dictionaryInfo.setPDistance(roundedPDistance);
					
					//compute p distance without character similarity (== o distance function with phonetic codes)
					double rawPDistance = getODistance(sourceInfo.getPhono().toString(), 
							destInfo.getPhono().toString());
					Distance roundedRawPDistance = new Distance(roundDistance(rawPDistance ));
					dictionaryInfo.setRawPDistance(roundedRawPDistance);
				}
				//set this to include NA pairs
				if (sourceInfo != null && destInfo != null) {
					linkStat.setDoubleMatches(linkStat.getDoubleMatches() + 1);
				}
				if (pDistance>=parameters.getPTHRESHOLD()) {
					if(linguisticData.containsKey(linkedDataID))
				        throw new RuntimeException("Duplicate expression '"+linkedDataID+"'");
					linguisticData.put(linkedDataID, linkedInfo);
					linkedDataID++;
					linkStat.setCognates(linkStat.getCognates()+1);
//				}
				}
//				}}
//				Integer linkedDataID = computeNewID(sourceLexiconID);
				if (linkStat.getDoubleMatches()> (step+1)*(dictionary.getKeySet().size()/20)) {
					step++;
					if (step*5 != 100) {
						System.out.print( step*5 + "%, ");
					} else System.out.println("100%\n");
				}
				if (sourceLexiconID != -1 && destinationLexiconID == -1) 
					linkStat.setSrcMatches(linkStat.getSrcMatches() + 1);
				if (sourceLexiconID == -1 && destinationLexiconID != -1) 
					linkStat.setDesMatches(linkStat.getDesMatches() + 1);
				if (sourceLexiconID == -1 && destinationLexiconID == -1) 
					linkStat.setNoMatches(linkStat.getNoMatches() + 1);
			}
		}
		}
		System.out.println("\n");
		LOGGER.info(sourceLexicon.getNumberOfConflicts() + " found more than once in " + l1 + " lexicon");
		LOGGER.info(destinationLexicon.getNumberOfConflicts() + " found more than once in " + l2 + " lexicon");
		LOGGER.info(linkStat.getSrcMatches() + " found in " + l1 + " lexicon only");
		LOGGER.info(linkStat.getDesMatches() + " found in " + l2 + " lexicon only");
		LOGGER.info(linkStat.getNoMatches() + " found in no lexicon at all");
		LOGGER.info("Obtained " + linkStat.getDoubleMatches() + " double matches\n");
	}
	
	/**
	 * @param srcSyntax
	 * @param destSyntax
	 * @return
	 */
//	private boolean compareSyntax(Syntax srcSyntax, Syntax destSyntax) {
//		if (srcSyntax.toString().equals("NA") || destSyntax.toString().equals("NA")) {
//			return false;
//		} else if (srcSyntax.toString().equals(destSyntax.toString())) {
//			return true;
//		}
//		return false;
//	}
	/**
	 * @param double1
	 * @param double2
	 * @return
	 */
//	private boolean compareF(Double f1, Double f2) {
//		if (Math.abs(f1-f2)<=MAXFDIFF) 
//			return true;
//		else return false;
//	}
	
	/**
	 * @param sourceLexicon 
	 * @param dictionaryInfo 
	 * @param dictionaryID 
	 * @param dictionary 
	 * 
	 */
	private void countMatches(Lexicon sourceLexicon, SemanticInfo dictionaryInfo, Integer dictionaryID, int dictionarySize) {
		int sourceLexiconID=-1;	

		sourceLexiconID = sourceLexicon.getCandidateFromLexicon(dictionaryInfo.getExpression());
		
		LinkedInfo linkedInfo = new LinkedInfo(dictionaryID, sourceLexiconID, 0);
		
		if (sourceLexiconID != -1) {
			linguisticData.put(linkedDataID, linkedInfo);
			linkedDataID++;
		}

		if (linkStat.getDoubleMatches()> (step+1)*(2000)) {
			step++;
			if (step*5 != 100) {
				System.out.print( step*5 + "%, ");
			} else System.out.println("100%\n");
		}
	}
	/**
	 * @param simulationNumber
	 * @param exportNumber
	 * @param date
	 * @param wordType 
	 * @param sourceLexicon
	 * @param destinationLexicon
	 * @param dictionary
	 */
	public void listExporter(int simulationNumber, int exportNumber, String date, String wordType, 
			Lexicon sourceLexicon, Lexicon destinationLexicon, Semantics dictionary) {
		
		LOGGER.info("Exporting: " + l1 + "-" + l2);
		try
	    {	      
	      boolean success = (new File("results/"+date)).mkdir();
	      if (!success) {
	    	  LOGGER.info("Adding results to directory " + date + "\n");
	      }    
	      
	      //String time = getDateString("HH.mm");
	      
	      String params = l1+"-"+l2+"\t"+wordType+"\t"+
	      sourceLexicon.getStats().getName() + "\t" + destinationLexicon.getStats().getName() + "\t"+
	      parameters.getFREQTYPE() + "\t" + parameters.getMINFREQUENCY() + "\t" + 
	      parameters.getOTHRESHOLD() + "\t" + parameters.getPTHRESHOLD();
	      
	      String fileName="results/"+date+"/"+simulationNumber + "_" + exportNumber + "_" + wordType+".txt";
	      String scoreOFileName="results/"+date+"/"+simulationNumber + "_" + exportNumber + "_" + "o_scores"+".txt";
	      String scorePFileName="results/"+date+"/"+simulationNumber + "_" + exportNumber + "_" + "p_scores"+".txt";
	      String scoreRawPFileName="results/"+date+"/"+simulationNumber + "_" + exportNumber + "_" + "raw_p_scores"+".txt";
	      
	      BufferedWriter scoreOWriter = new BufferedWriter(new FileWriter(scoreOFileName));
	      BufferedWriter scorePWriter = new BufferedWriter(new FileWriter(scorePFileName));
	      BufferedWriter scoreRawPWriter = new BufferedWriter(new FileWriter(scoreRawPFileName));
//	      BufferedWriter cognateWriter = new BufferedWriter(new OutputStreamWriter(
//	    		  new FileOutputStream(fileName), "UTF-16"));
	    //   BufferedWriter cognateWriter = new BufferedWriter(new OutputStreamWriter(
	    // 		  new FileOutputStream(fileName)));
		//   BufferedWriter cognateWriter = new BufferedWriter(new OutputStreamWriter(
					//   new FileOutputStream(fileName)));
		  BufferedWriter cognateWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
		

	      writeHeader(cognateWriter);
		  writeEntries(cognateWriter, scoreOWriter, scorePWriter, scoreRawPWriter, sourceLexicon, destinationLexicon, dictionary);
	      cognateWriter.close();
	      
	      
	      StatExporter exporter = new StatExporter();	      
	      exporter.exportStats(l1, l2, params, simulationNumber, exportNumber, date,
	    		  sourceLexicon.getStats(), destinationLexicon.getStats(),
	    		  dictionary.getStats(), this.linkStat);
	    }
	    catch(IOException e1)
	    {
	      e1.printStackTrace();
	    }
	    LOGGER.info("Exported "+linguisticData.size()+" word pairs.\n");
	}

	/**
	 * @param cognateWriter
	 * @throws IOException
	 */
	private void writeHeader(BufferedWriter cognateWriter) throws IOException {
		  //Header
	      cognateWriter.write("" +
	      		l1 + "\t" + l2 +"\t" +
	      		"p1\tp2\t" +
	      		"s_relation\to_distance\tp_distance\traw_p_distance\t" +
	      		"l1_frequency\tl2_frequency\t" +
	      		"l1_nr_of_conflicts\tl2_nr_of_conflicts\t" +
	      		"l1_syntax\tl2_syntax\t" +
	      		"l1_length\t l2_length\t" +
	      		"nr_of_meanings\tmeanings");
	      if (l1 == Language.Spanish && l2 == Language.Italian) {
	    	  cognateWriter.write(
	    		"l1_text2speech\tl2_text2speech");
	      }
	      cognateWriter.newLine();
	}
	
	/**
	 * @param cognateWriter
	 * @param scoreOWriter
	 * @param scorePWriter
	 * @param scoreRawPWriter
	 * @param sourceLexicon
	 * @param destinationLexicon
	 * @param dictionary
	 * @throws IOException
	 */
	private void writeEntries(BufferedWriter cognateWriter, 
			BufferedWriter scoreOWriter, BufferedWriter scorePWriter, BufferedWriter scoreRawPWriter, 
			Lexicon sourceLexicon, Lexicon destinationLexicon, Semantics dictionary) 
			throws IOException {
		  //Entries
	      Vector<Integer> matches=new Vector<Integer>(linguisticData.keySet());
	      Collections.sort(matches);
	      
	      Hashtable<String, Integer> scoreOTable = new Hashtable<String, Integer>(120,0.75f);
	      Hashtable<String, Integer> scorePTable = new Hashtable<String, Integer>(120,0.75f);
	      Hashtable<String, Integer> scoreRawPTable = new Hashtable<String, Integer>(120,0.75f);
	      
	      for (Integer id:matches) {
	    	  LinkedInfo linkedInfo = (LinkedInfo) linguisticData.get(id);
	    	  LexicalInfo sourceLexiconInfo = sourceLexicon.getInfo(linkedInfo.getSourceLexiconID());
	    	  LexicalInfo destinationLexiconInfo = destinationLexicon.getInfo(linkedInfo.getDestinationLexiconID());
	    	  SemanticInfo dictionaryInfo = dictionary.getInfo(linkedInfo.getDictionaryID());
			  
	    	  putDistance(dictionaryInfo.getExpression().length(), 
	    			  dictionaryInfo.getTranslation().length(), 
	    			  scoreOTable, dictionaryInfo.getODistance());
	    	  putDistance(dictionaryInfo.getExpression().length(), 
	    			  dictionaryInfo.getTranslation().length(), 
	    			  scorePTable, dictionaryInfo.getPDistance());
	    	  putDistance(dictionaryInfo.getExpression().length(), 
	    			  dictionaryInfo.getTranslation().length(), 
	    			  scoreRawPTable, dictionaryInfo.getRawPDistance());
	    	  
	    	  writeEntry(cognateWriter, sourceLexiconInfo,
					destinationLexiconInfo, dictionaryInfo);
	      }
	      try
	      {
	        saveScoreTable(scoreOWriter, scoreOTable);
	        saveScoreTable(scorePWriter, scorePTable);
	        saveScoreTable(scoreRawPWriter, scoreRawPTable);
	        scoreOWriter.close();
	        scorePWriter.close();
	        scoreRawPWriter.close();
	      }
	      catch(IOException e1)
	      {
	        e1.printStackTrace();
	      }
	}
	/**
	 * @param cognateWriter
	 * @param sourceLexiconInfo
	 * @param destinationLexiconInfo
	 * @param dictionaryInfo
	 * @throws IOException
	 */
	private void writeEntry(BufferedWriter cognateWriter,
			LexicalInfo sourceLexiconInfo, LexicalInfo destinationLexiconInfo,
			SemanticInfo dictionaryInfo) throws IOException {

		  if (sourceLexiconInfo != null) {
			  cognateWriter.write(
				  //expression,  
				  sourceLexiconInfo.getLexiconItem().toString() + "\t");  
		  } else cognateWriter.write(dictionaryInfo.getExpression() + "\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  // translation,
				//   System.out.println(destinationLexiconInfo.getLexiconItem().toString());
				  destinationLexiconInfo.getLexiconItem().toString() + "\t");
		  else cognateWriter.write(dictionaryInfo.getTranslation() + "\t");
		  if (sourceLexiconInfo != null)
			  cognateWriter.write(
				  //source phonetic transcription
				  sourceLexiconInfo.getPhono().toString()+ "\t");
		  else cognateWriter.write("NA\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  //destination phonetic transcription
				  destinationLexiconInfo.getPhono().toString() + "\t");
		  else cognateWriter.write("NA\t");
		  cognateWriter.write(
				  //abc, o distance, p distance, raw p distance  
				  dictionaryInfo.getPrintInfoMeasures()+ "\t");
		  
		  if (sourceLexiconInfo != null)
			  cognateWriter.write(	    			  
				  //source frequency
				  sourceLexiconInfo.getFrequency().toDouble()+ "\t");
		  else cognateWriter.write("NA\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  //destination frequency
				  destinationLexiconInfo.getFrequency().toDouble() + "\t");
		  else cognateWriter.write("NA\t");
		  if (sourceLexiconInfo != null)
			  cognateWriter.write(
				  //source number of conflicts
				  sourceLexiconInfo.getNumberOfCondflicts() + "\t");
		  else cognateWriter.write("NA\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  //destination number of conflicts
				  destinationLexiconInfo.getNumberOfCondflicts() + "\t");
		  else cognateWriter.write("NA\t");
		  if (sourceLexiconInfo != null)
			  cognateWriter.write(
				  //lexicon source syntax,
				  sourceLexiconInfo.getLexSyntax().toString() + "\t");
		  else cognateWriter.write("NA\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  //lexicon destination syntax
				  destinationLexiconInfo.getLexSyntax().toString() + "\t");
		  else cognateWriter.write("NA\t");
		  if (sourceLexiconInfo != null)
			  cognateWriter.write(
				  //source length,
				  sourceLexiconInfo.getLexiconItem().length() + "\t");
		  else cognateWriter.write(dictionaryInfo.getExpression().length() + "\t");
		  if (destinationLexiconInfo != null)
			  cognateWriter.write(
				  //destination length,
				  destinationLexiconInfo.getLexiconItem().length() +"\t");
		  else cognateWriter.write(dictionaryInfo.getTranslation().length() + "\t");
		  cognateWriter.write(
				  //meanings (optional)
				  dictionaryInfo.getPrintInfoMeanings());
		  if (l1 == Language.Spanish && l2 == Language.Italian) {
			  if (sourceLexiconInfo != null)
				  cognateWriter.write(
						  sourceLexiconInfo.getText2Speech()+"\t");
			  else cognateWriter.write("NA\t");
			  if (destinationLexiconInfo != null)
				  cognateWriter.write(
						  destinationLexiconInfo.getText2Speech()+"\t");
			  else cognateWriter.write("NA\t");
		  }
		  cognateWriter.newLine();
	}
	
	/**
	 * @param scoreWriter
	 * @param scoreTable
	 * @throws IOException
	 */
	private void saveScoreTable(BufferedWriter scoreWriter, Hashtable<String, Integer> scoreTable) throws IOException
	  {
	    Vector<String> v=new Vector<String>(scoreTable.keySet());
	    Collections.sort(v);
	    for(Enumeration<String> e=v.elements();e.hasMoreElements();)
	    {
	      String key=(String)e.nextElement();
	      Integer val=(Integer)scoreTable.get(key);
	      String[] keys = key.split(":");
	      scoreWriter.write(keys[0]+"\t"+keys[1]+"\t"+keys[2]+"\t"+val);
	      scoreWriter.newLine();
	    }
	  }
	
	private void putDistance(int sourceLength, int destLength, Map<String, Integer> scoreMap, Distance distance)
	{
		if (distance != null) {
		    //save scores in a hashtable
		    //key: score, value: number of scores
		    Locale loc = new Locale("en","US");
		    NumberFormat nf = NumberFormat.getNumberInstance(loc);
		    DecimalFormat df = (DecimalFormat)nf;
		    String pattern = "00";
		    df.applyPattern(pattern);
		    String length1 = df.format(sourceLength);
		    String lenght2 = df.format(destLength);
		    Object value=scoreMap.get(distance.toString()+":"+length1+":"+lenght2);
		    
		    if(value == null) {
		    	scoreMap.put(distance+":"+length1+":"+lenght2,new Integer(1));
		    } 	
		    else {
		        int i=((Integer)value).intValue()+1;
		        scoreMap.put(distance+":"+length1+":"+lenght2,new Integer(i));
		    }
		}
	}
	
//	private int countExistingSourceIDs(Integer sourceLexiconID) {
//	int c=0;
//	for (Integer id:linguisticData.keySet()) {
//		LinkedInfo info = (LinkedInfo) linguisticData.get(id);
//		if (info.getSourceLexiconID()==sourceLexiconID) {
//			c=c+1;
//		}
//	}
//	return c;
//}
		
//private Integer computeNewID(Integer sourceLexiconID) {
//	
//	int c=countExistingSourceIDs(sourceLexiconID);
//	String ZEROS = "000";
//	if (c>9) {
//		ZEROS="00";
////		if (equalSources>99) {
////			zeros="0";
////		}
//	}
//	Integer newID = Integer.valueOf(sourceLexiconID.toString()+ZEROS+c);
//	return newID;
//}
	
}
