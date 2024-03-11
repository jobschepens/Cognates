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
import nl.ru.dcc.cognates.types.Number;
import nl.ru.dcc.cognates.types.Phono;
import nl.ru.dcc.cognates.types.Sex;
import nl.ru.dcc.cognates.types.Syntax;

/**
 * @author lpxjjs1
 *
 */
public class CoLFISFrequencies extends Lexicon {

	private static LexiconName lexiconName = LexiconName.CoLFIS;
	
	/**
	 * @param l
	 * @param lexiconName
	 * @param maxCognates
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 */
	public CoLFISFrequencies(Language l, int maxCognates,
			double threshold, int minLength, int maxLength,
			double minFrequency, String freqType) {
		super(l, lexiconName, maxCognates, threshold, minLength, maxLength,
				minFrequency, freqType);
	}

	public void importFrequencies() throws FregTypeException {
		LOGGER.info("Reading CoLFIS " + parameters.getFREQTYPE() + ": " + l1);
		String filename=null;
		AllLanguagesNameCoder code = new AllLanguagesNameCoder(Language.Spanish); 
		if (parameters.getFREQTYPE().equals("Lemma")) {
			filename="data/CoLFIS/Lemmi.txt";		}
		if (parameters.getFREQTYPE().equals("Word forms")) {
			filename="data/CoLFIS/Forme.txt";		
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line=br.readLine()) != null) {
				freqStats.setLexiconSize(freqStats.getLexiconSize() + 1);
				String[] list = line.split("\\t");
				
				LexiconItem lexItem = null;
				Frequency frequency = null;
				Syntax syntax = null; 
				
				//missing information:
				Phono phon=null;
				Phono text2speech=null;
				Number number = new Number("");
				Sex sex = new Sex("");
				
				if (parameters.getFREQTYPE().equals("Lemma")) {
					 lexItem = new LexiconItem(list[0].toLowerCase());
					 frequency = new Frequency(list[1]);
					 syntax = new Syntax(list[2]);
					 text2speech = new Phono(list[0].toLowerCase());
					 phon = new Phono(list[0]);
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					 lexItem = new LexiconItem(list[0]);
					 frequency = new Frequency(list[1]);
					 syntax = new Syntax(list[2]);
					 text2speech = new Phono(list[0].toLowerCase());
					 phon = new Phono(list[0]);
				}
				text2speech = convertText2Speech(lexItem, code);
				phon = convertToDisc(lexItem);
				
				if (syntax != null) {
					syntax = convertSyntax(syntax);
				} else syntax = new Syntax("NA");
				if (lexItem!=null) {
					if (lexItem.toString().length()>=parameters.getMINLENGTH()) {
			        	if (lexItem.toString().length()<=parameters.getMAXLENGTH()) {
			        		if (validateSpelling(lexItem.toString(),l1)) {
				        		if (frequency.toDouble()>=parameters.getMINFREQUENCY()) {
					        			Info info = new LexicalInfo(lexItem,frequency,
					        					freqStats.getIdentifiedFrequencies(), syntax, sex, number, phon, text2speech);
					        			linguisticData.put(freqStats.getIdentifiedFrequencies(), info);
					        			freqStats.setId(freqStats.getIdentifiedFrequencies()+1);
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
		LOGGER.info("Imported " + freqStats.getLexiconSize() + " words from CoLFIS");
		LOGGER.info(freqStats.getLowFreq() + " words have a lower frequency than " + parameters.getMINFREQUENCY());
		LOGGER.info(freqStats.getNotAllowed() + " words have disallowed characters (* , '  )");
		LOGGER.info((freqStats.getTooBig()+freqStats.getTooSmall()) + " words have a length that is not between " + parameters.getMINLENGTH() + " and " + parameters.getMAXLENGTH());
		LOGGER.info("Obtained " + linguisticData.size() + " words\n");
	}
	
	private Phono convertToDisc(LexiconItem lexItem) {
		String phon = lexItem.toString();
		//discards punctuation
		phon = phon.replaceAll( "\\p{Punct}+\\s*|\\s+", "" ).toLowerCase().trim();
		//4-gram
		phon = phon.replace("agli", "a§");
		phon = phon.replace("ugli", "u§");
		phon = phon.replace("igli", "i§");
		phon = phon.replace("ogli", "O§");
		phon = phon.replace("egli", "E§");
		phon = phon.replace("agno", "a¨");
		phon = phon.replace("igno", "i¨");
		phon = phon.replace("asci", "aª");
		phon = phon.replace("usci", "u©");
		//3-grams
		phon = phon.replace("sce", "Se");
		phon = phon.replace("sci", "Si");
		phon = phon.replace("gli", "L");
		phon = phon.replace("izz", "I¡");
		phon = phon.replace("azz", "A¡");
		phon = phon.replace("ezz", "E¢");
		phon = phon.replace("ozz", "O¢");
		phon = phon.replace("ggi", "¦i");
		phon = phon.replace("gge", "¦e");
		phon = phon.replace("asc", "a©");
		phon = phon.replace("cci", "«i");
		
		//2-grams
		phon = phon.replace("ch", "k");
		phon = phon.replace("gh", "g");
		phon = phon.replace("gn", "µ");
		phon = phon.replace("sm", "zm");
		phon = phon.replace("sb", "zb");
		phon = phon.replace("so", "So");
		phon = phon.replace("su", "Su");
		phon = phon.replace("ci", "£i");
		phon = phon.replace("ce", "£i");
		phon = phon.replace("qu", "¤");
		phon = phon.replace("gi", "¥i");
		phon = phon.replace("ge", "¥e");
		phon = phon.replace("cc", "«");
		//1-grams
		for (int i=0;i<phon.length();i++) {
			if (phon.charAt(i)=='z') {
				if (i==0) {
					phon = phon.replace("z", "_");
				}
				if (i!=phon.length()-1) {
					if (phon.charAt(i+1)!='m' || phon.charAt(i+1)!='b'||phon.charAt(i+1)!='o'||phon.charAt(i+1)!='u') {
						phon = phon.replace("z", "J");
					}
				}
			}
		}
		phon = phon.replace("c", "k");
		phon = phon.replace("é", "e");
		phon = phon.replace("ì", "i");
		phon = phon.replace("à", "A");
		phon = phon.replace("ù", "u");
		phon = phon.replace("è", "E");
		phon = phon.replace("ò", "O");
		phon = phon.replace("ua", "­");
		phon = phon.replace("ue", "®");
		phon = phon.replace("ui", "¯");
		phon = phon.replace("uo", "°");
		
		return new Phono(phon);
	}
	/**
	 * @param lexItem
	 * @param code
	 * @return
	 */
	private Phono convertText2Speech(LexiconItem lexItem, AllLanguagesNameCoder code) {
		
		Phono text2speech = new Phono(code.codeSentence(lexItem.toString()));
		
		return text2speech;
	}
	
	/**
	 * @param syntax
	 * @return
	 */
	private Syntax convertSyntax(Syntax syntax) {
		String s = syntax.toString();
		
		if (s.equals("B")) return new Syntax ("ADV");
		if (s.equals("G")) return new Syntax("A");
		if (s.equals("S")) return new Syntax("N");
		if (s.equals("I")) return new Syntax("INTERJ");
		if (s.equals("P")) return new Syntax("PREPOS");
		if (s.equals("N")) return new Syntax("PRON");
		if (s.equals("V")) return syntax;
		if (s.equals("NU")) return new Syntax("NUM");
		if (s.equals("R")) return new Syntax("ART");
		if (s.equals("C")) return new Syntax("CONJ");
        return new Syntax("NA");
	}
}
