package nl.ru.dcc.cognates.exceptions;

public class TooManyTranslationsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 12;
	
	public TooManyTranslationsException(String format, Object...args)
	{
		super(String.format(format, args));
	}

}
