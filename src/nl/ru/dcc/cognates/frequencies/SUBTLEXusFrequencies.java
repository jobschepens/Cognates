/**
 * 
 */
package nl.ru.dcc.cognates.frequencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.client.LexiconName;
import nl.ru.dcc.cognates.exceptions.FregTypeException;
import nl.ru.dcc.cognates.types.Frequency;
import nl.ru.dcc.cognates.types.Info;
import nl.ru.dcc.cognates.types.LexicalInfo;
import nl.ru.dcc.cognates.types.LexiconItem;
import nl.ru.dcc.cognates.types.Phono;
import nl.ru.dcc.cognates.types.Sex;
import nl.ru.dcc.cognates.types.Syntax;
import nl.ru.dcc.cognates.types.Number;

/**
 * @author lpxjjs1
 *
 */
public class SUBTLEXusFrequencies extends Lexicon {

	private static LexiconName lexiconName = LexiconName.SUBTLEXus;
	/**
	 * @param l
	 * @param lexiconName
	 * @param maxTranslations
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 */
	public SUBTLEXusFrequencies(Language l, int maxTranslations, double threshold, int minLength, int maxLength,
			double minFrequency, String freqType) {
		super(l, lexiconName, maxTranslations, threshold, minLength, maxLength,
				minFrequency, freqType);
	}

	/* (non-Javadoc)
	 * @see nl.ru.dcc.cognates.frequencies.Lexicon#importFrequencies()
	 */
	@Override
	public void importFrequencies() throws FregTypeException {
		
		LOGGER.info("Reading SUBTLEXus " + parameters.getFREQTYPE() +": " + l1);
		String filename="data/SUBTLEXus/SUBTLEXus.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line=br.readLine()) != null) {
				freqStats.setLexiconSize(freqStats.getLexiconSize() + 1);
				String[] list = line.split("\\t");
				LexiconItem lexItem = null;
				Frequency frequency = null;
				if (parameters.getFREQTYPE().equals("Lemma")) {
					throw new FregTypeException("'%s' does not contain lemma frequencies", lexiconName);
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					 frequency = new Frequency(list[3]);
					 lexItem = new LexiconItem(list[0]);
				}
				
				Number number = new Number("");
				Sex sex =  new Sex ("");
				Syntax syntax = new Syntax("");
				
				if (lexItem!=null) {
			        if (lexItem.toString().length()>=parameters.getMINLENGTH()) {
			        	if (lexItem.toString().length()<=parameters.getMAXLENGTH()) {
			        		if (validateSpelling(lexItem.toString(),l1)) {
				        		if (frequency.toDouble()>=parameters.getMINFREQUENCY()) {
//				        			if (!existing(lexItem)) {
					    				Info info = new LexicalInfo(lexItem,frequency,freqStats.getIdentifiedFrequencies(), 
					    						syntax, sex, number, new Phono("NA"), null);
					    				linguisticData.put(freqStats.getIdentifiedFrequencies(), info);
					    				freqStats.setId(freqStats.getIdentifiedFrequencies()+1);
//				        			}
								} else freqStats.setLowFreq(freqStats.getLowFreq() + 1);
			        		} else freqStats.setNotAllowed(freqStats.getNotAllowed() + 1);
			        	} else freqStats.setTooBig(freqStats.getTooBig() + 1);
			        } else freqStats.setTooSmall(freqStats.getTooSmall() + 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    LOGGER.info("Imported " + freqStats.getLexiconSize() + " words from SUBTLEXus");
	    LOGGER.info(freqStats.getLowFreq() + " words have a lower frequency than " + parameters.getMINFREQUENCY());
		LOGGER.info(freqStats.getNotAllowed() + " words have disallowed characters (* , '  )");
		LOGGER.info((freqStats.getTooBig()+freqStats.getTooSmall()) + " words have a length that is not between " + parameters.getMINLENGTH() + " and " + parameters.getMAXLENGTH());
		LOGGER.info("Obtained " + linguisticData.size() + " words\n");
		computeAVG();
	}
}
