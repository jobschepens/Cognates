/**
 * 
 */
package nl.ru.dcc.cognates.distance;

import nl.ru.dcc.cognates.exceptions.phonemeNotFoundException;

/**
 * @author Job
 *
 */
public class Substituter {

	  private static double SIMRANGE;
	  private static final double ST_PEN = 0.1;
	  private static final int 
	  VWL = 10, NON_PULM = 2, LONG_AFF = 2, 
	  DIPHT = 2, LONG_VWL = 2, BRW_VWL = 2;
	  
      private static int consonantManner = 7;
	  private static int consonantPlace = 9;
	  private static int vowelHeight = 6;
	  private static int vowelBackness = 2;
	  
	  private static char[][][] pulmonicCons = {
	/*|......labial...........|	|...........coronal..........|		|.........dorsal.........|	radical	laryngeal*/
	/*bilabial		labiodental	dental		alveolar	palato		palatal	velar		uvular	pha+epi	glottal*/
	  {{'p','b'},	{},			{},			{'t','d'},	{},			{},		{'k','g'},	{},		{},		{}},	/*Plosives*/
	  {{'m'},		{},			{},			{'n'},		{},			{'µ'},	{},			{'N'},	{},		{}},	/*Nasals*/
	  {{},			{},			{},			{'r'},		{},			{},		{},			{'R'}, 	{},		{}},	/*Trills*/
	  {{},			{},			{},			{},			{},			{},		{},			{},		{},		{}},	/*Taps or Flaps*/
	  {{},			{'f','v'},	{'D','T'},	{'s','z'},	{'S','Z'},	{},		{'x','G'},	{},		{},		{'h'}},	/*Fricatives*/
	  {{},			{},			{},			{},			{},			{},		{},			{},		{},		{}},	/*Lateral fricatives*/
	  {{'w'},		{},			{},			{},			{},			{'j'},	{},			{},		{},		{}},	/*Approximants*/
	  {{},			{},			{'l'},		{},			{},			{},		{},			{},		{},		{}},	/*Lateral approximants*/
	  };  
	  private static char[][][] nonPulmonicCons = {
	  /*|......labial............................|		|...........coronal.......................|								|.........dorsal.............|	radical	laryngeal*/
	  /*bilabial				labiodental	dental		alveolar							palato								palatal		velar		uvular	pha+epi	glottal*/
	  {{'+'},					{},			{},			{'=','J','¡','_','¢','£','«','¦','¥'},	{},								{},			{'¤'},		{},		{},		{}},	/*Plosives*/
	  {{'F'},					{},			{},			{'H'},									{},								{'¨'},		{},			{'C'},	{},		{}},	/*Nasals*/
	  {{},						{},			{},			{'R'},									{},								{},			{},			{}, 	{},		{}},	/*Trills*/
	  {{},						{},			{},			{},										{},								{},			{},			{},		{},		{}},	/*Taps or Flaps*/
	  {{},						{'+'},		{},			{'=','_','¢','©'},						{'J','¡','£','ª','«','¦','¥'},	{},			{},			{},		{},		{}},	/*Fricatives*/
	  {{},						{},			{},			{},										{},								{},			{},			{},		{},		{}},	/*Lateral fricatives*/
	  {{'¤','­','®','¯','°'},	{},			{},			{},										{},								{'¬'},		{},			{},		{},		{}},	/*Approximants*/
	  {{},						{},			{'§','P'},	{},										{},								{},			{},			{},		{},		{}},	/*Lateral approximants*/
	  };
	  private static char[] longAfricates = 
	  {
		  '©','¡','¢','ª','«','¦'
	  };	
	  private static char[][][] vowelSpace = {
		  /*|...Front...|			|...Central...|	|...Back...|*/	
		  {{'i','y','!','('},		{'}'},			{'u'}},				/*Close*/
		  {{'I','Y'},				{},				{'U'}},				/*Close-Close-Mid*/
		  {{'e','|'},				{},				{'o'}},				/*Close-Mid*/
		  {{},						{'@'},			{}},				/*Close-Mid-Open-Mid*/
		  {{'E','*',')','/','^'},	{'3'},			{'V','$','O','~'}},	/*Open-Mid*/
		  {{'{','c','0'},			{},				{}},				/*Open-Mid-Open*/
		  {{'a','&','q'},			{},				{'#','A','Q','<'}}	/*Open*/
	  };	  
	  private static char[][][] diphthongs = {
		  /*|...Front...|			|...Central...|	|...Back...|*/	
		  {{'K','L','W','X'},		{},					{'M','B'}},		/*Close*/
		  {{'1','2','4','7'},		{},					{'5','6','9'}},	/*Close-Close-Mid*/
		  {{'1'},					{'5','7','8','9'},	{}},			/*Close-Mid*/
		  {{},						{},					{}},			/*Close-Mid-Open-Mid*/
		  {{'K','L','8'},			{},					{'4','X'}},		/*Open-Mid*/
		  {{},						{},					{}},			/*Open-Mid-Open*/
		  {{'M','2','6','W','B'},	{},					{}}				/*Open*/
	  };
	  private static char[] longVowels = 
	  {
		  'i','y','e','|','a','o','u','#','$','3',')','!','(',')','<','q','0','~','^'
	  };	  
	  private static char[] borrowedVowels = 
	  {
		  'c','q','0','~','^'
	  };  

	  /**
	 * @param simrange2
	 */
	public Substituter(double simrange2) {
		SIMRANGE = simrange2;
	}

	/**
	 * Computes the cost of substitution between 2 characters. 
	 * The cost is computed according to the distance between the positions of the characters in the IPA. 
	 * @param c
	 * @param d
	 * @return
	 * @throws phonemeNotFoundException 
	 */
	public double computeCost(char c, char d) throws phonemeNotFoundException 
	{
		int cRow = -1, cCol = -1, dRow = -1, dCol = -1;
		
		//determine position of both consonants
		//1. lookup the consonant in the consonant table
		//2. set positions according to the IPA table
		//3. distance between consonants is determined according to the distance between these positions, 
		//   distance = 0 for consonants in the same cell
		//4. substitution with vowels is not possible
		for (int i=0;i<=consonantManner;i++) {
			for (int j=0;j<=consonantPlace;j++) {
				for (int k=0; k<pulmonicCons[i][j].length; k++) {
					if (pulmonicCons[i][j][k] == c) {
						cRow = i;
						cCol = j;
					}
					if (pulmonicCons[i][j][k] == d) {
						dRow = i;
						dCol = j;
					}
				}
			}
		}
		//for some non-pulmonic consonants (affricates etc.) a position cannot be easily determined
		//1. lookup the special consonant in the other consonants table
		//2. set the positions according to the minimal distance between the components of the consonants  
		// or between a non-pulmonic consonant and the already found pulmonic consonant
		boolean nonPulmConsSource   = false;
		boolean nonPulmConsDest     = false;
		double dif=100;
		for (int i=0;i<=consonantManner;i++) {
			for (int j=0;j<=consonantPlace;j++) {
				for (int k=0; k<nonPulmonicCons[i][j].length; k++) {
					if (nonPulmonicCons[i][j][k] == c) {
						nonPulmConsSource = true;
						if (dRow == -1 || dCol == -1) {
							cRow = i;
							cCol = j;
						}
						else if (dis(i,dRow,j,dCol)<dif) {
							cRow = i;
							cCol = j;
							dif = dis(cRow,dRow,cCol,dCol);
						}
					}
					if (nonPulmonicCons[i][j][k] == d) {
						nonPulmConsDest = true;
						if (cRow == -1 || cCol == -1) {
							dRow = i;
							dCol = j;
						}
						else if (dis(cRow,i,cCol,j)<dif) {
							dRow = i;
							dCol = j;
							dif = dis(cRow,dRow,cCol,dCol);
						}
					}
				}
			}
		}
		

		
		//1. lookup vowel in vowel table to check if it exists
		//2. set vowel position according to its location in vowel space
		boolean vowelSource = false;
		boolean vowelDest   = false;	
		for (int i=0;i<=vowelHeight;i++) {
			for (int j=0;j<=vowelBackness;j++) {
				for (int k=0; k<vowelSpace[i][j].length; k++) {
					if (vowelSpace[i][j][k] == c) {
						vowelSource = true;
						cRow = i;
						cCol = j;
					}
					if (vowelSpace[i][j][k] == d) {
						vowelDest = true;
						dRow = i;
						dCol = j;
					}
				}
			}
		}
		//for diphthongs a position cannot be easily determined
		//1. lookup the diphthong in the diphthongs table
		//2. set the positions according to the minimal distance between the components of the diphthongs  
		// or between a diphthong and the already found vowel
		boolean diphthongSource   = false;
		boolean diphthongDest     = false;
		dif=1000;
		for (int i=0;i<=vowelHeight;i++) {
			for (int j=0;j<=vowelBackness;j++) {
				for (int k=0; k<diphthongs[i][j].length; k++) {
					if (diphthongs[i][j][k] == c) {
						vowelSource = true;
						diphthongSource = true;
						if (dRow == -1 || dCol == -1) {
							cRow = i;
							cCol = j;
						}
						else if (dis(i,dRow,j,dCol)<dif) {
							cRow = i;
							cCol = j;
							dif = dis(cRow,dRow,cCol,dCol);
						}
					}
					if (diphthongs[i][j][k] == d) {
						vowelDest = true;
						diphthongDest = true;
						if (cRow == -1 || cCol == -1) {
							dRow = i;
							dCol = j;
						}
						else if (dis(cRow,i,cCol,j)<dif) {
							dRow = i;
							dCol = j;
							dif = dis(cRow,dRow,cCol,dCol);
						}
					}
				}
			}
		}
		
		boolean longAfricateSource  = false;
		boolean longAfricateDest    = false;
		if (nonPulmConsSource) {
			for (int i=0; i<longAfricates.length; i++) {
				if (longAfricates[i] == c) {
					longAfricateSource = true;
				}
			}
		}
		if (nonPulmConsDest) {
			for (int i=0; i<longAfricates.length; i++) {
				if (longAfricates[i] == d) {
					longAfricateDest= true;
				}
			}
		}
		
		boolean longVowelSource = false;
		boolean longVowelDest   = false;
		if (vowelSource) {
			for (int i=0; i<longVowels.length; i++) {
				if (longVowels[i] == c) {
					longVowelSource = true;
				}
			}
		}
		if (vowelDest) {
			for (int i=0; i<longVowels.length; i++) {
				if (longVowels[i] == d) {
					longVowelDest= true;
				}
			}
		}
		boolean borrowedVowelSource = false;
		boolean borrowedVowelDest   = false;
		if (vowelSource) {
			for (int i=0; i<borrowedVowels.length; i++) {
				if (borrowedVowels[i] == c) {
					borrowedVowelSource = true;
				}
			}
		}
		if (vowelDest) {
			for (int i=0; i<borrowedVowels.length; i++) {
				if (borrowedVowels[i] == d) {
					borrowedVowelDest= true;
				}
			}
		}
		
		//the algorithm will throw an exception when a phoneme was not found
		if (cRow==-1 || cCol==-1) { 
			throw new phonemeNotFoundException(String.valueOf(c));
		}
		if (dRow==-1 || dCol==-1) {
			throw new phonemeNotFoundException(String.valueOf(d));
		}
		

		double penalty = 0;
		//give a huge penalty if a vowel is compared to a consonant
		//ass. 0: vowels and consonants cannot be compared
		if (vowelSource != vowelDest) {
			penalty=penalty+(VWL*ST_PEN);
		}
		/*|.....consonants....|*/
		//give a penalty if one or both characters are non-pulmonic, 
			//ass. 1: non-pulmonic consonants are less similar to each other and to pulmonic consonants  
		if (nonPulmConsSource || nonPulmConsDest) {
			penalty=penalty+(NON_PULM*ST_PEN);
		}
		//give long affricates an extra penalty
			//ass. 2: long affricates are less similar to each other and to other consonants in general
		if (longAfricateSource != longAfricateDest) {
			penalty=penalty+(LONG_AFF*ST_PEN);
		}
		/*|.....vowels....|*/
		//give a penalty if one or both characters are diphthongs, 
			//ass. 3: diphthongs are less similar to each other and to single-component vowels
		if (diphthongSource || diphthongDest) {
			penalty=penalty+(DIPHT*ST_PEN);
		}
		//give long vowels an extra penalty
			//ass. 4: long vowels are less similar to each other and to other vowels in general 
		if (longVowelSource != longVowelDest) {
			penalty=penalty+(LONG_VWL*ST_PEN);
		}
		//give borrowed vowels an extra penalty
			//ass. 5: borrowed vowels are less similar to each other and to other vowels in general
		if (borrowedVowelSource || borrowedVowelDest) {
			penalty=penalty+(BRW_VWL*ST_PEN);
		}
	
		//compute raw phoneme distances 
		int rowDif = abs(dRow-cRow);
		int colDif = abs(dCol-cCol);
		//compute Euclidian distance between both coordinates
		double euclidianDistance = Math.sqrt(rowDif*rowDif+colDif*colDif);
		//0->0
		//1,1->1.2	2,0->2
		//2,2->2.8	3,1->3.1	4,0->4
		//3,3->4.3	4,2->4.5
		//4,4->5.8	5,3->5.9
		//5,5->7.0
		
		//normalize 
		//TODO use Euclidean? what is SIMRANGE? 4? 
		double distance = euclidianDistance/SIMRANGE;
		
		//apply penalty
		double cost = distance + penalty;
		
//		System.out.println(
//				"\nvowel XOR: " + (vowelSource != vowelDest) + 
//				"\tnon-pulm OR: " + (nonPulmConsSource || nonPulmConsDest) +
//				"\tlong non-pulm XOR: " + (longAfricateSource != longAfricateDest) +
//				"\tdiphthong OR: " + (diphthongSource || diphthongDest) + 
//				"\tlong vowel XOR: " + (longVowelSource != longVowelDest) +
//				"\tborr vowel OR: " + (borrowedVowelSource || borrowedVowelDest));
//				
//		System.out.println(
//				"L1: " + c + "\tL2: " + d +  
//				"\t(" + cRow + "," + cCol + ")" + "\t(" + dRow + "," + dCol + ")" + 
//				"\trow: " + rowDif + "\tcol: " + colDif + 
//				"\td=" + distance + "\tp=" + penalty + "\tx=" + cost);
//		
		//substitution should be smaller than deletion + insertion
		if (cost > 2) cost = 2; 
		return cost;
	}
	
	/**
	 * @param row
	 * @param row2
	 * @param col
	 * @param col2
	 * @return
	 */
	private double dis(int row, int row2, int col, int col2) {
		return Math.sqrt(abs(row2-row)*abs(row2-row)+abs(col2-col)*abs(col2-col));
	}

	/**
	 * @param i
	 * @return
	 */
	private int abs(int i) {
		if (i<0) return -i;
		else return i;
	}
//  private static char[] otherConsonants =  
//  {
//		  '+','=','J','_','¡','¢','£','§','¨','©','ª','«','¦','¤','¥'	//Affricates and double articulation
//		  'C','F','H','P','R',											//Syllabic consonants 
//		  '¬','­','®','¯','°',											//Co-articulated consonants
//  };
	  
//	  private static char[] vowels = 
//	  {
//		  'i','y','e','|','a','o','u','#','$','3',')', 				//long
//		  'I','Y','E','/','A','{','&','Q','O','}','V','U','@',		//short
//		  '!','(',')','*','<','c','q','0','~','^',					//borrowed
//		  'K','L','M','1','2','4','5','6','7','8','9','W','B','X'	//dipthtongs
//	  };
	
}
