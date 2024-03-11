package nl.ru.dcc.cognates.frequencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.client.LexiconName;
import nl.ru.dcc.cognates.types.Frequency;
import nl.ru.dcc.cognates.types.Info;
import nl.ru.dcc.cognates.types.LexicalInfo;
import nl.ru.dcc.cognates.types.LexiconItem;
import nl.ru.dcc.cognates.types.Phono;
import nl.ru.dcc.cognates.types.Sex;
import nl.ru.dcc.cognates.types.Syntax;
import nl.ru.dcc.cognates.types.Number;

public class LexiqueFrequencies extends Lexicon {
	
	private static LexiconName lexiconName = LexiconName.Lexique;
	
	public LexiqueFrequencies(Language l, int maxCognates, double threshold, int minLength, int maxLength, 
			double minFrequency, String freqType) {
		super(l, lexiconName, maxCognates, threshold, minLength, maxLength, minFrequency, freqType);
	}

	public void importFrequencies() {
		
		LOGGER.info("Reading Lexique " + parameters.getFREQTYPE() + ": " + l1);
		String filename=null;
		if (parameters.getFREQTYPE().equals("Lemma")) {
			filename="data/Lexique/Lex3.lemmes.txt";		}
		if (parameters.getFREQTYPE().equals("Word forms")) {
			filename="data/Lexique/Lexique3.txt";		
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line=br.readLine()) != null) {
				freqStats.setLexiconSize(freqStats.getLexiconSize() + 1);
				String[] list = line.split("\\t");
				
				LexiconItem lexItem = null;
				Frequency frequency = null;
				Phono phon =null;
				Syntax syntax = null; 
				if (parameters.getFREQTYPE().equals("Lemma")) {
					 frequency = new Frequency(list[8]);
					 lexItem = new LexiconItem(list[0]);
					 phon = new Phono(list[3].split(";")[0]);
					 syntax = new Syntax(list[1]);
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					 frequency = new Frequency(list[8]);
					 lexItem = new LexiconItem(list[0]);
					 phon = new Phono(list[1]);
					 syntax = new Syntax(list[3]);
				}
				if (syntax!=null) {
					syntax = convertSyntax(syntax);
				} else syntax = new Syntax("NA");
				if (phon!=null) {
					phon = convertLexiqueToDisc(phon);
				}
				Sex sex = new Sex(list[5]);
				Number number = new Number(list[5]);
				if (lexItem!=null) {
					if (lexItem.toString().length()>=parameters.getMINLENGTH()) {
			        	if (lexItem.toString().length()<=parameters.getMAXLENGTH()) {
			        		if (validateSpelling(lexItem.toString(),l1)) {
				        		if (frequency.toDouble()>=parameters.getMINFREQUENCY()) {
//				        			if (!existing(lexItem)) {
					        			Info info = new LexicalInfo(lexItem,frequency,freqStats.getIdentifiedFrequencies(),
					        					syntax, sex, number, phon, null);
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
		LOGGER.info("Imported " + freqStats.getLexiconSize() + " words from Lexique");
		LOGGER.info(freqStats.getLowFreq() + " words have a lower frequency than " + parameters.getMINFREQUENCY());
		LOGGER.info(freqStats.getNotAllowed() + " words have disallowed characters (* , '  )");
		LOGGER.info((freqStats.getTooBig()+freqStats.getTooSmall()) + " words have a length that is not between " + parameters.getMINLENGTH() + " and " + parameters.getMAXLENGTH());
		LOGGER.info("Obtained " + linguisticData.size() + " words\n");
	}

	/**
	 * @param phon
	 * @return
	 */
	private Phono convertLexiqueToDisc(Phono phono) {
		phono = removeStresses(phono); //remove separators
		String phon = phono.toString();
		phon = convertToXSampa(phon);
		
		//if no disc transcription was directly available 
		//it is treated as a similar phoneme according to the IPA table
		//of which these conversion are the result
		phon = phon.replace("q", "k"); //no dorsal uvular plosive in disc, treated as a dorsal velar plosive 
		phon = phon.replace("J","N"); //no palatal nasal in Disc, treated as velar nasal
		phon = phon.replace("R","r"); //no r's in lexique, possibly this r is similar to the lexique R
		phon = phon.replace("tS", "J"); //the sound as in matsch
		phon = phon.replace("dz", "_"); //the sound as in jazz
		//it seems that lexique is more like sampa than x-sampa
		//there are no G/,?,F,B,4,T,D,C,P in Lexique apparently		
		
		return new Phono(phon);
	}

	/**
	 * @param phon
	 * @return
	 */
	private String convertToXSampa(String phon) {
		//vowels
		phon = phon.replace("5", "e");
		phon = phon.replace("@", "a");
		phon = phon.replace("§", "o");
		phon = phon.replace("1", "^");
		phon = phon.replace("°", "@");
		phon = phon.replace("a", "A");
		
		//consonants
		phon = phon.replace("G", "N");
		phon = phon.replace("N", "J");
		phon = phon.replace("8", "¬");
		phon = phon.replace("R", "=");
		phon = phon.replace("r", "R");
		phon = phon.replace("=", "r");
		phon = phon.replace("dz", "_");
		
		return phon;
	}

	/**
	 * @param syntax
	 * @return
	 */
	private Syntax convertSyntax(Syntax syntax) {
		String s = syntax.toString();
		
		if (s.equals("ADJ")) return new Syntax("A");
		if (s.equals("ADV")) return syntax;
		if (s.equals("NOM")) return new Syntax("N");
		if (s.equals("ONO")) return new Syntax("INTERJ");
		if (s.equals("PRE")) return new Syntax("PREPOS");
		if (s.equals("PRO")) return new Syntax("PRON");
		if (s.equals("VER")) return new Syntax("V");
		if (s.equals("ADJ NUM")) return new Syntax("NUM");
		if (s.equals("ART")) return syntax;
		if (s.equals("CON")) return new Syntax("CONJ");
        return new Syntax("NA");
	}
}
