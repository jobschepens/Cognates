package nl.ru.dcc.cognates.types;

public class Sex extends Type {

	public Sex(String string2) {
		super(string2);
		if (string2.equals("M")) {
			setString("m");
		} else if (string2.equals("v")) {
			setString("f");
		} 
	}
}
