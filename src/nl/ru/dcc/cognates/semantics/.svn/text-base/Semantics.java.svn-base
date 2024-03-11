/**
 * 
 */
package nl.ru.dcc.cognates.semantics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.cognates.AbstractLinguisticData;
import nl.ru.dcc.cognates.exceptions.TooManyTranslationsException;
import nl.ru.dcc.cognates.semantics.dictionary.MyDictionary;
import nl.ru.dcc.cognates.types.AbcTranslation;
import nl.ru.dcc.cognates.types.SemanticInfo;
import nl.ru.dcc.cognates.types.Syntax;
import nl.ru.nici.euroglot.dictionary.Reading;
import nl.ru.nici.euroglot.dictionary.Relation;
import nl.ru.nici.euroglot.dictionary.Word;

/**
 * @author lpxjjs1
 *
 */
public abstract class Semantics extends AbstractLinguisticData {

	protected DictionaryStats dicStat;
	protected MyDictionary sourceLanguage, destinationLanguage;

	/**
	 * @param l1
	 * @param l2
	 * @param maxTranslationPairs
	 * @param threshold
	 * @param minLength
	 * @param maxLength
	 * @param minFrequency
	 * @param freqType
	 * @param countMatches 
	 */
	public Semantics(Language l1, Language l2, int maxTranslationPairs,
			double threshold, int minLength, int maxLength,
			double minFrequency, String freqType, boolean countMatches, double simRange) {
		super(l1, l2, maxTranslationPairs, threshold, 0.0, minLength, maxLength,
				minFrequency, freqType,countMatches, simRange);
		dicStat = new DictionaryStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	/**
	 * @param id
	 * @return
	 */
	public SemanticInfo getInfo(Integer id) {
		if (linguisticData.get(id) == null){
			throw new RuntimeException("Missing link '"+id+"'");
		}
		if (linguisticData.get(id) instanceof SemanticInfo)  
			return (SemanticInfo) linguisticData.get(id);
		return null;
	}

	/* (non-Javadoc)
	 * @see nl.ru.dcc.cognates.semantics.Translations#getStats()
	 */
	public DictionaryStats getStats() {
		return dicStat;
	}

	/**
	 * @throws TooManyTranslationsException 
	 * 
	 */
	public abstract void obtainData() throws TooManyTranslationsException;



	/**
	 * @return
	 */
	public Set<Integer> getKeySet() {
		return linguisticData.keySet();
	}
	
	/**
	 * input: two relations of two readings of two words that refer to the same concept
	 * @param relation
	 * @param trRelation
	 * @return
	 */
	protected AbcTranslation setABC(Relation relation, Relation trRelation) {
		AbcTranslation abcTranslation = new AbcTranslation("c");
		//identical relation number & type is a translation
		if (relation.getNumber() == trRelation.getNumber()) {
			if (relation.getType() == trRelation.getType()) {
				abcTranslation.set("a");
				dicStat.setAtr(dicStat.getAtr() + 1);
			}
			else {
				//identical number, different type is b translation
				abcTranslation.set("b");
				dicStat.setBtr(dicStat.getBtr() + 1);
			}
		}
		//different number & type is c translation
		else dicStat.setCtr(dicStat.getCtr() + 1);
		return abcTranslation;
	}
	
	protected Syntax getSyntax(Reading reading) {
		Syntax syntax = new Syntax("");
//		if (reading.getPos().getCategory().toString() != null) {
//			syntax.set(reading.getPos().getCategory().toString());
//		}
		return syntax;
	}

	protected List<String> findRelatedMeaningInSource(Relation thisRelation, String concept, String expression) {
		
        List<Word> alternatives=sourceLanguage.getTranslation(concept);
        List<String> aMeanings = new ArrayList<String>();
        List<String> bMeanings = new ArrayList<String>();
        List<String> cMeanings = new ArrayList<String>();
        for (Word alternative:alternatives) {
        	//find an alternative that shares the same relation and is not the source word
        	if (!alternative.getExpression().equals(expression)) {
	        	Reading[] altReading = alternative.getReadingArray();
				for(int z=0;z<altReading.length; z++) {
					if (altReading[z].getConcept().equals(concept)) {
		        		Relation[] altRelation=altReading[z].getRelationArray();													
	        			for (int y=0;y<altRelation.length; y++) {
							if (thisRelation.getNumber() == altRelation[y].getNumber()) {
	        					if (thisRelation.getType() == altRelation[y].getType()) {
	        						aMeanings.add(alternative.getExpression());
	        					}
	        					else {
	        						bMeanings.add(alternative.getExpression());
	        					}
	        				}
							else cMeanings.add(alternative.getExpression());
	        			}
					}
				}
        	}
        }
        bMeanings.addAll(cMeanings);
        aMeanings.addAll(bMeanings);
        return aMeanings;
	}

	protected boolean existingTranslationPair(String expression, String translation, 
			Syntax sourceSyntax, Syntax destinationSyntax, AbcTranslation abc, Relation thisRelation, String concept) {
		
		for (Integer id:linguisticData.keySet()) {
			SemanticInfo info = (SemanticInfo) linguisticData.get(id);
			if (info.getExpression().equals(expression) && info.getTranslation().equals(translation)) {
				if (info.getSourceSyntax().toString().equals(sourceSyntax.toString()) 
						&& info.getDestinationSyntax().toString().equals(destinationSyntax.toString())) {
					//this translation pair has multiple shared meanings in different syntactic categories
					List<String> meanings = findRelatedMeaningInSource(thisRelation, concept, expression);
					Iterator<String> it = meanings.iterator();
					while (it.hasNext()) {
						String m = it.next();
						if (!info.meaningsContain(m)) {
							info.addMeaning(m);
							break;
						} 
					}
					info.overwriteABC(abc);
					info.updateNrMeanings();
					return true;
				}
			}
		}
		return false;
	}
}
