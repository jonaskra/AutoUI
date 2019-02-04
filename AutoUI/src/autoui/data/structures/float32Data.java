package autoui.data.structures;

import java.util.Dictionary;

import autoui.data.structures.exceptions.IllegalDataFormatException;

public class float32Data extends DataStructure<Float> {
	
	private static final String format = "float32";
	private static final String[] compatibleFormats = {
			"string",
			"float32",
			"int32",
	};
	
	public float32Data(String name, int id) {
		super(name, id, format, compatibleFormats);
	}

	public float32Data(String name, int id, Dictionary<String, String> settings) {
		super(name, id, settings, format, compatibleFormats);
	}

	@Override
	protected Float[] valueOf(String... input) throws IllegalDataFormatException {
		Float[] _data = new Float[input.length];
		
		for(int i = 0; i < input.length; i++) {
			try {
				_data[i] = Float.valueOf(input[i]);
			} catch(NumberFormatException e) {
				throw new IllegalDataFormatException("'" + input[i] + "' is not a valid float!", i);
			}
		}
		
		return _data;
	}

	@Override
	protected String[] stringRepresentation(Float[] data) {
		String[] output = new String[data.length];
		
		for(int i = 0; i < data.length; i++) {
			output[i] = Float.toString(data[i]);
		}
			
		return output;
	}

	@Override
	protected Float[] clone(Float[] data) {
		Float[] output = new Float[data.length];
		
		for(int i = 0; i < data.length; i++) {
			output[i] = (float) data[i];
		}
		
		return output;
	}

	@Override
	protected Float[] validateData(Float[] data) throws IllegalArgumentException {
		return data;
	}

	@Override
	protected byte[] getDataBytes() {
		//TODO
		return null;
	}
	
	

}
