package autoui.parser.exceptions;

public class IndentationException extends ParserException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3093700066120627228L;
	
	

	public IndentationException(int indentation, int expcetedIndentation, int line) {
		super("The indentation is " + indentation + " (" + expcetedIndentation + " expected)." , line);
	}

}
