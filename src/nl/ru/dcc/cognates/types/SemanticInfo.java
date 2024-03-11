package nl.ru.dcc.cognates.types;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SemanticInfo implements Info {

	private String expression, translation;
	private int sourceId, nrMeanings;
	private Distance oDistance, pDistance, rawPDistance;
	private Syntax ss,ds;
	private AbcTranslation abc;
	private Set<String> meanings;
	
	/**
	 * @param expression
	 * @param translation
	 * @param sourceSyntax
	 * @param destinationSyntax
	 * @param abcTranslation
	 * @param meanings2
	 */
	public SemanticInfo(String expression, String translation,
			Syntax sourceSyntax, Syntax destinationSyntax,
			AbcTranslation abcTranslation, List<String> meanings2) {
		this.ss = sourceSyntax;
		this.ds = destinationSyntax;
		this.abc = abcTranslation;
		this.expression = expression;
		this.translation = translation;
		meanings = new HashSet<String>();
		if (meanings.size()!=0) { 
			meanings.add(meanings2.get(0));
		}  
		nrMeanings=1;
	}
	
	public String getPrintInfoWords() {
		return new String(expression + "\t" + translation);
	}
	public String getPrintInfoMeasures() {
		if (pDistance==null) pDistance = new Distance("NA");
		if (rawPDistance==null) rawPDistance = new Distance("NA");
		if (oDistance==null) oDistance = new Distance("NA");
		return new String(
				abc + "\t" + 
				oDistance.toString() + "\t" + 
				pDistance.toString() + "\t" +
				rawPDistance.toString() );
	}
	
//	public String getPrintInfoSyntax() {
//		return new String(ss + "\t" + ds);
//	}
	
	public String getPrintInfoMeanings() {
		String me = "";
		Iterator<String> it = meanings.iterator();
		if (!it.hasNext()) {
			me = "NA";
		}
		while (it.hasNext()) {
			String next = it.next();
			next = next.replace(" ", "_");
			if (!next.equals("")) {
				if (!it.hasNext()) {
					me = me + next;
				} else {
					me = me + next + ",";
				}
			}
		}
		return new String(nrMeanings + "\t" + me );
	}	
	
	public void addMeaning(String meaning2) {
		meanings.add(meaning2);
	}
	
	public boolean meaningsContain(String meaning2) {
		if (meanings.contains(meaning2)) {
			return true;
		} else return false;
	}
	
	public void overwriteABC(AbcTranslation abc2) {
		if (abc2.toString().equals("a")) {
			abc = abc2;
		} else if (abc2.toString().equals("b")) {
			abc = abc2;
		}
	}
	
	public void updateNrMeanings() {
		nrMeanings++;
	}
	
	public String getExpression() {
		return expression;
	}

	public String getTranslation() {
		return translation;
	}

	public void setSourceLexiconID(Integer sourceID) {
		sourceId = sourceID;
	}
	
	/**
	 * @param roundedPDistance
	 */
	public void setPDistance(Distance roundedPDistance) {
		this.pDistance = roundedPDistance; 
	}
	/**
	 * @param roundedRawPDistance
	 */
	public void setRawPDistance(Distance roundedRawPDistance) {
		this.rawPDistance = roundedRawPDistance;
	}	
	public void setODistance(Distance roundedODistance) {
		this.oDistance = roundedODistance;
	}
	public int getSourceLexiconID() {
		return sourceId;
	}

	public Syntax getSourceSyntax() {
		return ss;
	}
	public Syntax getDestinationSyntax() {
		return ds;
	}

	/**
	 * @return
	 */
	public AbcTranslation getAbcTranslation() {
		return abc;
	}

	/**
	 * @return
	 */
	public Distance getODistance() {
		if (pDistance==null) pDistance = new Distance("NA");
		return oDistance;
	}
	/**
	 * @return
	 */
	public Distance getPDistance() {
		return pDistance;
	}

	/**
	 * @return
	 */
	public Distance getRawPDistance() {
		return rawPDistance;
	}
}
