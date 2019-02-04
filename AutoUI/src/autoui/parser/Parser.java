package autoui.parser;

import java.util.Hashtable;

import autoui.data.DataHandler;
import autoui.data.structures.DataStructure;
import autoui.parser.exceptions.ParserException;

public class Parser {
	
	private LineHandler code;
	
	private DataHandler dataHandler;
	
	public Parser(String code) {
		this.code = new LineHandler(code);
	}
	
	public void parse() throws ParserException {
		while(!code.endOfCode()) {
			code.nextLine();
			
			String term = term();
			
			switch(term) {
			case "data":
				dataHandler.add(dataStructure());
				break;
			}
		}		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DataStructure dataStructure() throws ParserException {		
		String name = null;
		String format = null;
		Hashtable<String, String> settings = new Hashtable<String, String>();
		
		int baseIndentation = code.lineIndentation();
		
		//data name:
		
		name = term();
		
		if(!term().equals(":"))
			throw new ParserException("':' expected after the name!", code.getCurrentLineNumber());
		
		
		endLine(":");
		
		
		// Now come some further settings
		//	format: int
		
		while(true) {
			// NEW LINE
			
			code.nextLine();
			
			if(code.lineIndentation() != baseIndentation + 1)
				break;
			
			String term = term();

			switch(term) {
			case "format":
				
				colon(term);
				
				format = term();
				
				String modifier = null;
				
				while(code.hasNextTerm()) {
					String temp = term();
					
					//Modifier
					if(temp.equals("("))
						modifier = arguments(format);
					
					format += " " + term();
				}

				if(!DataHandler.validFormat(format))
					throw new ParserException("'" + format + "' is not a valid format!", code.getCurrentLineNumber());
				
				settings.put("format-modifier", modifier);
				break;
			default:
				colon(term);
				
				String value = term();
				
				while(code.hasNextTerm()) {				
					value += " " + term();
				}

				if(!DataHandler.validSetting(term, value))
					throw new ParserException("Either the setting '" + term + "' does not exists, or the value '" + value + "' is invalid!",
							code.getCurrentLineNumber());

				settings.put(term, value);
				break;
			}
		}
		
		DataStructure dS = DataHandler.getSturctureByFormat(format, name);
		dS.setSettings(settings);
		
		
		return dS;
	}
	
	private String term() throws ParserException {
		if(!code.hasNextTerm())
			throw new ParserException("Term expected. But instead the line was over.", code.getCurrentLineNumber());
		
		return code.nextTerm();
	}
	
	private void colon(String before) throws ParserException {
		if(!term().equals(":"))
			throw new ParserException("':' expected after '"+ before + "'!", code.getCurrentLineNumber());
	}
	
	private void endLine(String end) throws ParserException {
		if(code.hasNextTerm())
			throw new ParserException("No further code expected after '" + end + "'!", code.getCurrentLineNumber());
	}
	
	private String arguments(String before) throws ParserException {
		String modifier = "";
		String term = null;
		
		while(!(term = term()).equals(")"))
			modifier += term;
		
		return modifier;
	}

}
