package nl.ru.dcc.cognates.semantics.dictionary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import nl.ru.nici.euroglot.dictionary.Concept;
import nl.ru.nici.euroglot.dictionary.DictionaryDocument;
import nl.ru.nici.euroglot.dictionary.Reading;
import nl.ru.nici.euroglot.dictionary.Word;
import nl.ru.nici.euroglot.dictionary.DictionaryDocument.Dictionary;
import nl.ru.nici.rosetta.encrypt.EncryptedDictionaryReader;
import nl.ru.nici.rosetta.encrypt.EncryptedDictionaryReaderException;

/**
 * @author sparky
 * Wrapper class for Dictionaries
 */
public class MyDictionary
{
  private Dictionary dictionary;
  private Map<String,Word> wordByExpression;
  private Map<String,List<Word>> wordsByConcept;
  private Concept[] conceptArray;
  private Map<String,Word> wordsByReading;
  /**
   * Creates a dictionary from a encoded file
   * @param filename filename of encoded dictionary
 * @param wordSource 
 * @param validate 
 * @throws DictionaryReaderException 
   */
  public MyDictionary(String filename, String wordSource, boolean validate) throws DictionaryReaderException
  {
    /*
     * Read file
     */
	if (!wordSource.equals("limited")) {
	    try
	    {
	      dictionary=EncryptedDictionaryReader.read(filename,false);
	    }
	    catch(EncryptedDictionaryReaderException e)
	    {
	      e.printStackTrace();
	      System.exit(1);
	    }
	}
	else {
		DictionaryDocument dictionaryDocument = readTestDatabase(filename, validate);
		dictionary=dictionaryDocument.getDictionary();
	}
    
    /*
     * Make some mappings for easy access
     */
    wordByExpression=new HashMap<String,Word>();
    wordsByConcept=new HashMap<String,List<Word>>();
    wordsByReading=new HashMap<String,Word>();
    /*
     * Build quick indexed access tables
     */
    Word[] words=dictionary.getWordArray();
    for(int i=0;i<words.length;i++)
    {
      String expression=words[i].getExpression();
      if(wordByExpression.containsKey(expression))
        throw new RuntimeException("Duplicate expression '"+expression+"'");
      wordByExpression.put(expression,words[i]);
      Reading[] reading=words[i].getReadingArray();
      for(int j=0;j<reading.length;j++)
      {
        wordsByReading.put(reading[j].getId(),words[i]);
        String concept=reading[j].getConcept();
        if(!wordsByConcept.containsKey(concept))
          wordsByConcept.put(concept,new ArrayList<Word>());
        List<Word> list=wordsByConcept.get(concept);
        if (!list.contains(words[i])) {
        	list.add(words[i]);
        }
      }
    }
    conceptArray=dictionary.getConceptArray();
  }

private DictionaryDocument readTestDatabase(String filename, boolean validate)
		throws DictionaryReaderException {
	DictionaryDocument dictionaryDocument;
	try
	{
	  ArrayList<Object> errorList=new ArrayList<Object>();
	  XmlOptions parseOptions=new XmlOptions();
	  parseOptions.setErrorListener(errorList);
	  parseOptions.setLoadLineNumbers();
	  File file=new File(filename);
	  dictionaryDocument=DictionaryDocument.Factory.parse(file,parseOptions);
	  XmlOptions validateOptions=new XmlOptions();
	  validateOptions.setErrorListener(errorList);
	  validateOptions.setLoadLineNumbers();
	  /*
	   * Validate and read data
	   */
	  if(validate)
	  {
	    if(!dictionaryDocument.validate(validateOptions))
	    {
	      /*
	       * Error, report
	       */
	      StringBuffer stringBuffer=new StringBuffer();
	      stringBuffer.append('\n');
	      for(int i=0;i<errorList.size();i++)
	      {
	        if(i>50)
	        {
	          stringBuffer.append("More then 10 errors, skipping the rest\n");
	          break;
	        }
	        XmlError error=(XmlError)errorList.get(i);
	        stringBuffer.append("XML error, line: ");
	        stringBuffer.append(error.getLine());
	        stringBuffer.append(": ");
	        stringBuffer.append(error.getMessage().replaceAll("@http://www.nici.ru.nl/euroglot/dictionary.xsd",""));
	        stringBuffer.append('\n');
	      }
	      throw new DictionaryReaderException(new String(stringBuffer));
	    }
	  }
	}
	catch(XmlException e)
	{
	  throw new DictionaryReaderException("XML error while reading '"+filename+"': "+e.getError());
	}
	catch(IOException e)
	{
	  throw new DictionaryReaderException("I/O error while reading '"+filename+"': "+e.getMessage());
	}
	return dictionaryDocument;
}
    
  /**
   * @return the wordByExpression
   */
  public Map<String, Word> getWordByExpression()
  {
    return wordByExpression;
  }
  /**
   * @param concept concept to translate
   * @return list of words
   */
  public List<Word> getTranslation(String concept)
  {
    return wordsByConcept.get(concept);
  }
  /**
   * @return concept array
   */
  public Concept[] getConceptArray()
  {
    return conceptArray;
  }
  /**
   * @return words by reading
   */
  public Map<String, Word> getWordsByReading()
  {
    return wordsByReading;
  }
}
