package autoui.parser;

public class LineHandler {
	
	private boolean caseSensitive = true;
	private boolean toLowerCase = true;
	private String[] lines;
	private int lineNumber = -1;
	private int lineIndex = 0;
	
	public LineHandler(String code) {
		lines = code.split("\\r?\\n");
	}
	
	public LineHandler(String code, boolean caseSensitive, boolean toLowerCase) {
		lines = code.split("\\r?\\n");
		this.caseSensitive = caseSensitive;
		this.toLowerCase = toLowerCase;
	}
	
	public String getLine() {
		if(lineNumber < lines.length)
			return lines[lineNumber];
		
		return null;
	}
	
	public int getCurrentLineNumber() {
		return lineNumber;
	}
	
	public boolean endOfCode() {
		return lineNumber == lines.length;
	}
	
	public String nextTerm() {	
		while(lineIndex < getLine().length() && charType() == CharType.SPACES)
			lineIndex++;
		
		if(lineIndex == getLine().length())
			return null;
		
		if(charType() == CharType.COMMENT)
			return null;
		
		int start = lineIndex;
		CharType currentType = charType();
		
		while(lineIndex < getLine().length() && charType() == currentType)
			lineIndex++;
		
		lineIndex++;
		
		return convert(getLine().substring(start, lineIndex));
	}
	
	public int lineIndentation() {
		int numTabs = 0, numSpaces = 0;
		int _lineIndex = lineIndex;
		
		lineIndex = 0;
		
		while(currentChar() == ' ' || currentChar() == '\t') {
			if(currentChar() == ' ')
				numSpaces++;
			
			else
				numTabs++;
			
			lineIndex++;
		}
		
		lineIndex = _lineIndex;
		
		return numTabs + numSpaces / 4;
	}
	
	public boolean hasNextTerm() {
		int _lineIndex = lineIndex;
		
		if(nextTerm() == null)
			return false;
		
		lineIndex = _lineIndex;
		
		return true;
	}
	
	public void nextLine() {
		if(endOfCode())
			return;
		
		do {
			lineNumber++;
		} while(!endOfCode() && !hasNextTerm());
	}
	
	public void resetLineIndex() {
		lineIndex = 0;
	}
	
	private char currentChar() {
		return getLine().charAt(lineIndex);
	}
	
	private CharType charType() {		
		switch(currentChar()) {
		case '#':
			return CharType.COMMENT;
		case ' ': case '\t':
			return CharType.SPACES;
		case ':':
			return CharType.COLON;
		case '<':
			return CharType.INSERT;
		case '"':
			return CharType.QUOTES;
		case '(': case ')':
			return CharType.PARENTHESES;
		default:
			return CharType.LETTERS;
		}
	}
	
	private String convert(String term) {
		if(!caseSensitive) {
			if(toLowerCase)
				term.toLowerCase();
			else
				term.toUpperCase();
		}
		
		return term;
	}
	
	private enum CharType {
		COMMENT,
		SPACES,
		LETTERS,
		COLON,
		INSERT,
		QUOTES,
		PARENTHESES,
	}

}
