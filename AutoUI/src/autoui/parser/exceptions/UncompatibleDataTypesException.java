package autoui.parser.exceptions;

public class UncompatibleDataTypesException extends ParserException {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2444537723960015881L;

	public UncompatibleDataTypesException(String var1, String var2, String type1, String type2, int line) {
		super("The data formats of '" + var1 + "' (" + type1 + ") and '" + var2 + "' (" + type2 + ") are incompatible!", line);
	}

}
