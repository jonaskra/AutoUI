package autoui.parser.exceptions;

public class UndefinedVariableException extends ParserException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4866285344621191859L;
	
	public UndefinedVariableException(String varName, int line) {
		super("'" + varName + "' was not defined in this context!", line);
	}

}
