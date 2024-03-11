package nl.ru.dcc.cognates.distance;
import nl.ru.dcc.cognates.exceptions.phonemeNotFoundException;
/**
 * @author lpxjjs1
 * Calculates the minimal number of insertions deletions 
 * and substitutions needed to transform word1 into word2. 
 * Minimal editing distance is another name for the Levenshtein distance.
 *
 */
public class MinimalEditingDistance extends AbstractDistance
{
  private static Substituter substitution;
  
  /**
  * @param simrange2
  */
  public MinimalEditingDistance(double simrange2) {
	 substitution = new Substituter(simrange2);
  }
/* (non-Javadoc)
 * @see nl.ru.dcc.cognates.distance.EditDistance#calculatePScore(char[], char[])
 */
public double calculatePScore(char[] word1, char[] word2)
{
  double[][] table=new double[word1.length+1][word2.length+1];
  for(int i=0;i<word1.length+1;i++)
    table[i][0]=i;
  for(int j=1;j<word2.length+1;j++)
    table[0][j]=j;
  for(int i=1;i<word1.length+1;i++)
  {
    for(int j=1;j<word2.length+1;j++)
    {
      double cost = 0;
      if(word1[i-1]==word2[j-1])
        cost=0;
	  else {
		try { cost = substitution.computeCost(word1[i-1], word2[j-1]);
		} catch (phonemeNotFoundException e) {
			try { throw new phonemeNotFoundException("\nphoneme: "+e+"\tword1: "+String.valueOf(word1)+"\tword2: "+String.valueOf(word2));
			} catch (phonemeNotFoundException e1) {	e1.printStackTrace(); }	} }
      table[i][j]=Math.min(Math.min(table[i-1][j]+1,table[i][j-1]+1),table[i-1][j-1]+cost);
    }
  }
  double x = table[word1.length][word2.length];
  double maxLength = 2*Math.max(word1.length,word2.length);
  double similarity = 1-(x/maxLength);
//  System.out.println(String.valueOf(word1) + " <-> "+ String.valueOf(word2) + " = " + similarity + " (x=" + x + ")");
  return similarity;    
}





/* (non-Javadoc)
 * @see nl.ru.dcc.cognates.distance.EditDistance#calculateOScore(char[], char[])
 */
public double calculateOScore(char[] word1, char[] word2) {
    double[][] table=new double[word1.length+1][word2.length+1];
    for(int i=0;i<word1.length+1;i++)
      table[i][0]=i;
    for(int j=1;j<word2.length+1;j++)
      table[0][j]=j;
    for(int i=1;i<word1.length+1;i++)
    {
      for(int j=1;j<word2.length+1;j++)
      {
        double cost = 0;
        if(word1[i-1]==word2[j-1])
          cost=0;
		else
			cost = 1;
        table[i][j]=Math.min(Math.min(table[i-1][j]+1,table[i][j-1]+1),table[i-1][j-1]+cost);
      }
    }
    double x = table[word1.length][word2.length];
    
    double maxLength = Math.max(word1.length,word2.length);
    return ((maxLength-x)/maxLength);  
}
};


