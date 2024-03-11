package nl.ru.dcc.cognates;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.client.Main;
import nl.ru.dcc.cognates.distance.EditDistance;
import nl.ru.dcc.cognates.distance.MinimalEditingDistance;
import nl.ru.dcc.cognates.types.Info;

import org.apache.log4j.Logger;

public abstract class AbstractLinguisticData implements LinguisticData {

	protected static final Logger LOGGER=Logger.getLogger(Main.class);
	protected static LinguisticDataParameters parameters;
	protected Language l1;
	protected Language l2;
	protected Hashtable<Integer,Info> linguisticData;
	private double simRange;
	
	/**
	 * @param l1
	 * @param l2
	 * @param maxCognates
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 * @param countMatches 
	 * @param simRange2 
	 */
	public AbstractLinguisticData(Language l1, Language l2, int maxCognates, 
			double threshold, double threshold2, int minLength, int maxLength, double minFrequency, 
			String freqType, boolean countMatches, double simRange2) {
		//200000, 0.0, 3, 8, 32.00, "Lemma"
		
		parameters = new LinguisticDataParameters(maxCognates, threshold, threshold2,
				minLength, maxLength, minFrequency, freqType, countMatches);
		linguisticData = new Hashtable<Integer, Info>(120,0.75f); 
		this.l1 = l1;
		this.l2 = l2;
		simRange = simRange2;
	}
	
	/**
	 * @param l
	 * @param object
	 * @param maxCognates
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 */
	public AbstractLinguisticData(Language l1, Language l2, int maxCognates,
			double threshold, int minLength, int maxLength, double minFrequency, 
			String freqType) {
		parameters = new LinguisticDataParameters(maxCognates, threshold, 0.0,
				minLength, maxLength, minFrequency, freqType, false);
		linguisticData = new Hashtable<Integer, Info>(120,0.75f); 
		this.l1 = l1;
		this.l2 = l2;
	}

	public Language getL1() {
		return l1;
	}
	
	public Language getL2() {
		return l2;
	}
	
	protected boolean validateSpelling(String expression, Language l) {
		if (expression.length()>=parameters.getMINLENGTH() && expression.length()<=parameters.getMAXLENGTH()) {
			if (expression.length()!=0) {
				if (!l.equals(Language.German) && Character.isLowerCase(expression.charAt(0))) {
					if(checkAllowedCharacters(expression)) {
						return true;  
	  			    } 
	  		    } 
				if (l.equals(Language.German)) {
	  		    	if(checkAllowedCharacters(expression)) {
						return true;  
	  			    }
	  		    }
			} else return false;
		} 
		return false;
	}
	
	protected boolean checkAllowedCharacters(String expression) {

        char content[] = new char[expression.length()];
        expression.getChars(0, expression.length(), content, 0);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '*':
            	return false;
            case ',':
            	return false;
            case '\'':
            	return false;
//            case '-':
//            	return false;
//            case ' ':
//            	return false;
            }
        }
        return true;
    }
	
	protected String roundDistance(double distance) {
//		System.out.println("double:" + distance);
		Locale loc = new Locale("en","US");
		NumberFormat nf = NumberFormat.getNumberInstance(loc);
		DecimalFormat df = (DecimalFormat)nf;
		String pattern = "#0.000";
		df.applyPattern(pattern);
//		System.out.println("string:" + distance);
		return df.format(distance);
	}

	protected double getODistance(String expression, String translation) {
		if (!(expression.equals("NA") || translation.equals("NA"))) {
			EditDistance minimalEditingDistance = new MinimalEditingDistance(simRange); 
			return minimalEditingDistance.calculateOScore(expression,translation);
		} else 
			return 0;
	}
	
	protected double getPDistance(String expression, String translation) {
		if (!(expression.equals("NA") || translation.equals("NA"))) {
			EditDistance minimalEditingDistance = new MinimalEditingDistance(simRange); 
			return minimalEditingDistance.calculatePScore(expression,translation);
		} else 
			return 0;
	}
	
	public void printParams(int simulationNumber) {
		LOGGER.info("This is sumulation number: " + simulationNumber );
		LOGGER.info("OTHRESHOLD: " + parameters.getOTHRESHOLD() );
		LOGGER.info("PTHRESHOLD: " + parameters.getPTHRESHOLD() );
		LOGGER.info("MINLENGTH: " + parameters.getMINLENGTH() );
		LOGGER.info("MAXLENGTH: " + parameters.getMAXLENGTH() );
		LOGGER.info("MINFREQUENCY: " + parameters.getMINFREQUENCY() );
		LOGGER.info("FREQTYPE: " + parameters.getFREQTYPE() + "\n");
	}


}
