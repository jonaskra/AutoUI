package autoui.data.structures;

import java.util.Dictionary;

import autoui.data.structures.exceptions.IllegalDataFormatException;

public class StringData extends DataStructure<String> {
	
	private static final String format = "string";
	private static final String[] compatibleFormats = {
			"string",
			"int32",
			"uint32",
			"float32",
	};

	public StringData(String name, int id) {
		super(name, id, format, compatibleFormats);
	}
	
	public StringData(String name, int id, Dictionary<String, String> settings) {
		super(name, id, settings, format, compatibleFormats);
	}

	
	private String escapeString(String input) {
		return input;
	}

	@Override
	protected String[] validateData(String[] data) throws IllegalArgumentException {
		String[] clonedData = clone(data);
		String[] output = new String[data.length];
		
		int counter = 0;
		for(String s : clonedData) {
			output[counter] = escapeString(s);
		}
		
		return output;
	}
	
	private String concatenateData() {
		StringBuilder sb = new StringBuilder();
		
		for(String s : data)
			sb.append(s);
		
		return sb.toString();
	}

	@Override
	protected byte[] getDataBytes() {
		return concatenateData().getBytes();
	}

	@Override
	protected String[] clone(String[] data) {
		String[] output = new String[data.length];
		
		StringBuilder cloner = new StringBuilder();
		int counter = 0;
		for(String s : data) {
			cloner.delete(0, cloner.length());
			
			cloner.append(s);
			
			output[counter] = cloner.toString();
			counter++;
		}
		
		return output;
	}

	@Override
	protected String[] valueOf(String... input) throws IllegalDataFormatException {
		return clone(input);
	}

	@Override
	protected String[] stringRepresentation(String[] data) {
		return clone(data);
	}

}
