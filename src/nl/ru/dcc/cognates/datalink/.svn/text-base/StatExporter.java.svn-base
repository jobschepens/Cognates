package nl.ru.dcc.cognates.datalink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import nl.ru.dcc.client.Language;
import nl.ru.dcc.cognates.frequencies.FrequencyStatistics;
import nl.ru.dcc.cognates.semantics.DictionaryStats;

public class StatExporter {

	public StatExporter() {}

	public void exportStats(Language l1, Language l2, 
			String params, int simulationNumber, int exportNumber, String date, 
			FrequencyStatistics sourceLexiconStats, FrequencyStatistics destinationLexiconStats, 
			DictionaryStats dictionaryStats, LinkedDataStatistics linkStats) throws IOException {
		
		String prefix = "stats";
	    String file ="results/"+date+"/"+prefix+".out";
		File results=new File(file);

		if(!results.exists())
		{
			BufferedWriter languageWriter=new BufferedWriter(new FileWriter(results,true));
		    writeHeader(languageWriter);
		    writeEntries(languageWriter, simulationNumber, exportNumber, params,
					dictionaryStats, linkStats, l1, sourceLexiconStats, l2, destinationLexiconStats);
		    languageWriter.close();
		}
		else {
			BufferedWriter languageWriter=new BufferedWriter(new FileWriter(results,true));
		    writeEntries(languageWriter, simulationNumber, exportNumber, params,
					dictionaryStats, linkStats, l1, sourceLexiconStats, l2, destinationLexiconStats);
			languageWriter.close();
		}
	}

	private void writeHeader(BufferedWriter resultsWriter) throws IOException {
		//parameters
		resultsWriter.write("" +
			    "simulation\t" +
			    "combination\t" +
	      		"languages\t" +
	      		"semantic_relation\t" +
	      		"l1_lexicon\t" +
	      		"l2_lexicon\t" +
	      		"frequency_type\t" +
	      		"min_frequency\t" +
	      		"o_threshold\t" + 
	      		"p_threshold\t");
		//combo
		/*
		resultsWriter.write("" +
	      		"#items_in_l1_dictionary\t" + 
	      		"#items_in_l1_lexicon\t" +
	      		"#items_in_l2_dictionary\t" +
	      		"#items_in_l2_lexicon\t" +
	      		
	      		"#valid_items_in_l1_dictionary\t" +
	      		"#valid_items_in_l1_lexicon\t" +
	      		"#valid_items_in_l2_dictionary\t" +
	      		"#valid_items_in_l2_lexicon\t" +
	      		"#valid_translations_in_dictionary\t" +
	      		
	      		"#unique_source_expressions\t" +
	      		"#unique_translations\t" +
	      		"#unique_translation_pairs\t" +
	      		
	      		"#unique_A_source_expressions\t" +
	      		"#unique_A_translations\t" +
	      		"#unique_A_pairs\t" +
	      		
	      		);
//		  		"AVG O\t" +
//				"AVG S\t" +
//				"AVG P\t"
		*/
		//l1 stats
		resultsWriter.write("" +
				"#cognates\t" +
				"#matches\t" +
	      		"#l1_conflicts\t" +
	      		"#l1_only\t");
//			 	"AVG F\t" +
//   			"AVG L\t" +
//   			"AVG C"
		//l2 stats
		resultsWriter.write("" +
	      		"#l2_conflicts\t" +
	      		"#l2_only\t");
//			 	"AVG F\t" +
//				"AVG L\t" +
//				"AVG C"
		//not found
		resultsWriter.write("" +
				"not_found");
		resultsWriter.newLine();
	}
	
	private void writeEntries(BufferedWriter resultsWriter, 
			int simulationNumber, int exportNumber, String params,
			DictionaryStats dictionaryStats, LinkedDataStatistics linkStats,
			Language l1, FrequencyStatistics sourceLexiconStats, 
			Language l2, FrequencyStatistics destinationLexiconStats) throws IOException {
		//parameters
		resultsWriter.write(simulationNumber + "\t"+ exportNumber + "\t" + params + "\t");
		//combo
		/*
		resultsWriter.write("" +
	      		dictionaryStats.getSourceDictionarySize() + "\t" + 
	      		sourceLexiconStats.getLexiconSize() + "\t" +
	      		dictionaryStats.getDestDictionarySize() + "\t" +
	      		destinationLexiconStats.getLexiconSize() + "\t" +
	      		
	      		
	      		dictionaryStats.getValidatedSrc() + "\t" +
	      		sourceLexiconStats.getIdentifiedFrequencies() + "\t" +
	      		dictionaryStats.getValidatedDest() + "\t" +
	      		destinationLexiconStats.getIdentifiedFrequencies() + "\t" +	      		
	      		dictionaryStats.getValidatedTrPairs() + "\t" +
	      		
	      		dictionaryStats.getUniqueSourceExpressions() + "\t" +
	      		dictionaryStats.getUniqueDestExpressions() + "\t" +
	      		dictionaryStats.getIdentifiedPairs() + "\t" +
	      		
	      		dictionaryStats.getUniqueASrc() + "\t" +
	      		dictionaryStats.getUniqueATr() + "\t" +
	      		dictionaryStats.getUniqueAPairs() + "\t");
		*/
		resultsWriter.write("" +
				linkStats.getCognates()+ "\t" + 
	      		linkStats.getDoubleMatches()+ "\t");
		//l1 stats
		resultsWriter.write("" +
	      		sourceLexiconStats.getConflicts() + "\t" +
	      		linkStats.getSrcMatches() + "\t");
		//l2 stats
		resultsWriter.write("" +
	      		destinationLexiconStats.getConflicts() + "\t" +
	      		linkStats.getDesMatches() + "\t");
		//not found
		resultsWriter.write("" +
				linkStats.getNoMatches() + "\t");
		resultsWriter.newLine();
	}
}
