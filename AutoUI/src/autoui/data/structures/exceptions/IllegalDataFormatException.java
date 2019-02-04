package autoui.data.structures.exceptions;

public class IllegalDataFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4252195088642559499L;
	
	private int index;
	
	public IllegalDataFormatException(String message, int index) {
		super(message);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}
