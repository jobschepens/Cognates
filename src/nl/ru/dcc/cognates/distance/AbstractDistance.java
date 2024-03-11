package nl.ru.dcc.cognates.distance;

public abstract class AbstractDistance implements EditDistance
{
  public double calculateOScore(String word1, String word2)
  {
    return calculateOScore(word1.toCharArray(),word2.toCharArray());
  }
  public double calculatePScore(String word1, String word2)
  {
    return calculatePScore(word1.toCharArray(),word2.toCharArray());
  }
}
