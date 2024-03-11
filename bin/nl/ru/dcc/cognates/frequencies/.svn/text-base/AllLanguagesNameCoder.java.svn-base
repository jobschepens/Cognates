package nl.ru.dcc.cognates.frequencies;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.ru.dcc.client.Language;

//from http://www.javalobby.org/java/forums/t16936.html
//codes only the main phonetic characterisitcs of a word, 
//generally this means coding only for consonants and discarding vowels, 
//except where they might make a difference
//The one presented here attempts to group similar sounding consonants 
//and if a name starts with a vowel sound it will code for that too.

class AllLanguagesNameCoder {
 
  // regexs used to match the start of words
  static final String[] spanishStartCodes = {
    "\\Aw?[uh]?([aeiou])"
  };
 
  // regexs used to match the rest of the word
  static final String[][] spanishLoopCodes = {
    { "\\A(c[eiιν]|z|ll|sh|ch|sch|cc|y[aeiouαινσϊ]|ps|bs|x|j|g[eiιν])", "s" },
    { "\\A([aeiouhwαινσϊό]+)", "" },
    { "\\A(y)", "" },
    { "\\A(ρ|gn)", "n" },
    { "\\A([dpc]t)", "t" },
    { "\\A(c[aouασϊ]|ck|q)", "k" },
    { "\\A(v)", "b" }
  };
 
  // regexs used to match the start of words
  static final String[] italianStartCodes = {
	  //TODO italian regexps
  };
 
  // regexs used to match the rest of the word
  static final String[][] italianLoopCodes = {

  };
  
  static Pattern[] starters;
  static Pattern[][] loopers;
 
/**
 * @param spanish
 */
public AllLanguagesNameCoder(Language language) {
	switch (language) {
		case Spanish:
			starters = new Pattern[ spanishStartCodes.length ];
		    for ( int i = 0; i < starters.length; i++ )
		      starters[ i ] = Pattern.compile( spanishStartCodes[i] );
		    loopers = new Pattern[ spanishLoopCodes.length ][ 2 ];
		    for ( int i = 0; i < loopers.length; i++ ) {
		      loopers[ i ][ 0 ] = Pattern.compile( spanishLoopCodes[i][0] );
		      loopers[ i ][ 1 ] = Pattern.compile( spanishLoopCodes[i][1] );
		    }
		case Italian:
			starters = new Pattern[ italianStartCodes.length ];
		    for ( int i = 0; i < starters.length; i++ )
		      starters[ i ] = Pattern.compile( italianStartCodes[i] );
		    loopers = new Pattern[ italianLoopCodes.length ][ 2 ];
		    for ( int i = 0; i < loopers.length; i++ ) {
		      loopers[ i ][ 0 ] = Pattern.compile( italianLoopCodes[i][0] );
		      loopers[ i ][ 1 ] = Pattern.compile( italianLoopCodes[i][1] );
		    }
	}
}

// codes a single word
  public static String codeWord( String v ) {
    final int vl = v.length();
    v = v.toLowerCase();
    StringBuilder sb = new StringBuilder( vl );
 
    int i = 0;
 
    for ( int j = 0; j < starters.length; j++ ) {
      Matcher m = starters[ j ].matcher( v );
      if ( m.lookingAt() ) {
        i += m.end(1)-1;
        sb.append( m.group(1) );
        break;
      }
    }
 
    while ( i < vl ) {
      String s = v.substring( i, vl );
      boolean found = false;
      for ( int j = 0; j < loopers.length; j++ ) {
        Matcher m = loopers[ j ][ 0 ].matcher( s );
        if ( m.lookingAt() ) {
          found = true;
          i += m.end(1)-1;
          sb.append( loopers[j][1]);
          break;
        }
      }
      if ( ! found ) {
        char c = v.charAt( i );
        sb.append( c );
        int j = i + 1;
        while ( j < vl ) {
          if ( c != v.charAt( j ) )
            break;
          j++;
        }
        i = j - 1;
      }
      i++;
    }
    if ( v.charAt( vl-1 ) == 'd' ) sb.setCharAt( sb.length()-1, 't' );
    return sb.toString();
  }
 
  // codes a word discarding punctuation
  public String codeSentence( String v ) {
    v = v.replaceAll( "\\p{Punct}+\\s*|\\s+", " " ).toLowerCase().trim();
    v = codeWord( v );
    return v;
  }
 
}

