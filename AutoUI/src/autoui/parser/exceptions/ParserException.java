package autoui.parser.exceptions;

public class ParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224229532570874642L;
	
	int line;
	
	public ParserException(String message, int line) {
		super(message + "\nLine: " + line);
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}

}
