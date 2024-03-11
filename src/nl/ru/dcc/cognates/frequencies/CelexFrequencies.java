package nl.ru.dcc.cognates.frequencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

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

public class CelexFrequencies extends Lexicon {
	
	private static LexiconName lexiconName = LexiconName.Celex;
	private Hashtable<Integer, Phono> phonoCodes;
	private Hashtable<Integer, Syntax> syntaxCodes;
	private Hashtable<Integer, String> diaCodes; 
	
	public CelexFrequencies(Language l, int maxCognates, double threshold, int minLength, int maxLength, 
			double minFrequency, String freqType) {
		super(l, lexiconName, maxCognates, threshold, minLength, maxLength, minFrequency, freqType);
	}

	
	public void importFrequencies() {
		
		LOGGER.info("Reading Celex " + parameters.getFREQTYPE() +": " + l1);
		String lcode = l1.toString().charAt(0) + "F" + parameters.getFREQTYPE().charAt(0);
		String filename="data/Celex/"+l1+"/"+lcode+"/"+lcode+".CD";
		readPhonos();
		readSyntaxes();
		readDiacritics();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line=br.readLine()) != null) {
				freqStats.setLexiconSize(freqStats.getLexiconSize() + 1);
				String[] list = line.split("\\\\");
				
				Integer id = Integer.valueOf(list[0]).intValue();
				LexiconItem lexItem = convertDia(diaCodes.get(id));
				if (lexItem==null) {
					lexItem = new LexiconItem(list[1]);
				}
				
				Phono phon = removeStresses(phonoCodes.get(id)); //PhonStrsDisc word forms head words

				Frequency frequency = null;
				Syntax syntax = null;
				if (parameters.getFREQTYPE().equals("Lemma")) {
					frequency = new Frequency(list[4]);
					syntax = syntaxCodes.get(id);
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					//lemma frequencies are in another file and in another column
					frequency = new Frequency(list[5]);
					syntax = new Syntax("NA");
				}				
				
				Number number = new Number("");
				Sex sex = new Sex("");
				
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
	    LOGGER.info("Imported " + freqStats.getLexiconSize() + " words from Celex");
	    LOGGER.info(freqStats.getLowFreq() + " words have a lower frequency than " + parameters.getMINFREQUENCY());
		LOGGER.info(freqStats.getNotAllowed() + " words have disallowed characters (* , '  )");
		LOGGER.info((freqStats.getTooBig()+freqStats.getTooSmall()) + " words have a length that is not between " + parameters.getMINLENGTH() + " and " + parameters.getMAXLENGTH());
		LOGGER.info("Obtained " + linguisticData.size() + " words\n");
		computeAVG();
	}

/**
	 * @param string
	 * @return
	 */
	private LexiconItem convertDia(String string) {
		if (string != null) {
			string.replace("#a", "á");
			string.replace("#e", "é");
			string.replace("#i", "í");
			string.replace("#o", "ó");
			string.replace("#u", "ú");
			string.replace("\"a", "ä");
			string.replace("\"e", "ë");
			string.replace("\"i", "ï");
			string.replace("\"o", "ö");
			string.replace("\"u", "ü");
			string.replace("`a", "à");
			string.replace("`e", "è");
			string.replace("`i", "ì");
			string.replace("`o", "ò");
			string.replace("`u", "ù");
			string.replace("^a", "â");
			string.replace("^e", "ê");
			string.replace("^i", "î");
			string.replace("^o", "ô");
			string.replace("^u", "û");
			string.replace(",c", "ç");
			string.replace("~n", "ñ");
			string.replace("@a", "å");
			return new LexiconItem(string);
		}

		return null;
	}


private void readDiacritics() {
		
		String lcodeDia = l1.toString().charAt(0) + "O" + parameters.getFREQTYPE().charAt(0);
		String filenameDia="data/Celex/"+l1+"/"+lcodeDia+"/"+lcodeDia+".CD";
		diaCodes = new Hashtable<Integer, String>(120,0.75f);
		try {
			BufferedReader brDia = new BufferedReader(new FileReader(new File(filenameDia)));
			String lineDia = null;
			while ((lineDia=brDia.readLine()) != null) {
				String[] list = lineDia.split("\\\\");
				String diaStr = null;
				if (parameters.getFREQTYPE().equals("Lemma")) {
					diaStr = list[1]; //HeadDia
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					diaStr = list[1]; //WordDia
				}
				Integer id = Integer.valueOf(list[0]).intValue();
				if (diaStr != null) {
					diaCodes.put(id, diaStr);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param syntax
	 * @return
	 */
	private Syntax convertClass(Integer syntax) {
        switch (syntax) {
            case 0:
            	return new Syntax("EXP");
            case 1:
            	return new Syntax("N");
            case 2:
            	return new Syntax("A");
            case 3:
            	return new Syntax("NUM");
            case 4:
            	return new Syntax("V");
            case 5:
            	return new Syntax("ART");
            case 6:
            	return new Syntax("PRON");
            case 7:
            	return new Syntax("ADV");
            case 8:
            	return new Syntax("PREPOS");
            case 9:
            	return new Syntax("CONJ");
            case 10:
            	return new Syntax("INTERJ");
            }
        return new Syntax ("NA");
	}


	/**
	 * 
	 */
	private void readPhonos() {
		
		String lcodePhono = l1.toString().charAt(0) + "P" + parameters.getFREQTYPE().charAt(0);
		String filenamePhono="data/Celex/"+l1+"/"+lcodePhono+"/"+lcodePhono+".CD";
		phonoCodes = new Hashtable<Integer, Phono>(120,0.75f);
		try {
			BufferedReader brPhono = new BufferedReader(new FileReader(new File(filenamePhono)));
			String linePhono = null;
			while ((linePhono=brPhono.readLine()) != null) {
				String[] list = linePhono.split("\\\\");
				Phono phonStrs = null;
				if (parameters.getFREQTYPE().equals("Lemma")) {
					if (this.l1.equals(Language.Dutch) || this.l1.equals(Language.German)) {
						if (list.length>3) {
							//assumes that idNum, Head and Inl are never missing
							phonStrs = new Phono(list[3]);
						}
					}
					if (this.l1.equals(Language.English)) {
						if (list.length>4) {
							//assumes that idNum, Word, Inl, and IdNumLemma are never missing
							phonStrs = new Phono(list[5]); //PhonStrsDisc word forms head words
						}
					}
				}
				if (parameters.getFREQTYPE().equals("Word forms")) {
					if (this.l1.equals(Language.Dutch) || this.l1.equals(Language.German)) {
						if (list.length>4) {
							//assumes that idNum, Word, Inl, and IdNumLemma are never missing
							phonStrs = new Phono(list[4]); //PhonStrsDisc word forms head words
						}
					}
					if (this.l1.equals(Language.English)) {
						if (list.length>4) {
							phonStrs = new Phono(list[6]); //PhonStrsDisc word forms head words
						}
					}
				}
				Integer id = Integer.valueOf(list[0]).intValue();
				
				if (phonStrs != null) {
					phonoCodes.put(id, phonStrs);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void readSyntaxes() {
		String lcode = l1.toString().charAt(0) + "S" + "L";
		String filename = "data/Celex/"+l1+"/"+lcode+"/"+lcode+".CD";
		syntaxCodes = new Hashtable<Integer, Syntax>(120,0.75f);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line=br.readLine()) != null) {
				String[] list = line.split("\\\\");
				Syntax syntaxClass = null;
				if (parameters.getFREQTYPE().equals("Lemma")) {
							String syntax = list[3];
							Integer syntaxNr = Integer.valueOf(syntax).intValue();
							syntaxClass = convertClass(syntaxNr);
				}
				Integer id = Integer.valueOf(list[0]).intValue();
				
				if (syntaxClass != null) {
					syntaxCodes.put(id, syntaxClass);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
