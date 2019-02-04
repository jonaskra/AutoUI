package autoui.data.structures;

import java.util.Dictionary;

import autoui.data.structures.exceptions.IllegalDataFormatException;

public class uint32Data extends DataStructure<Integer> {
	
	private static final String format = "uint32";
	private static final String[] compatibleFormats = {
			"string",
			"uint32",
	};
	
	public uint32Data(String name, int id) {
		super(name, id, format, compatibleFormats);
	}

	public uint32Data(String name, int id, Dictionary<String, String> settings) {
		super(name, id, settings, format, compatibleFormats);
	}

	@Override
	protected Integer[] validateData(Integer[] data) throws IllegalArgumentException {
		return data;
	}

	@Override
	protected byte[] getDataBytes() {
		byte[] output = new byte[data.size() * 4];
		
		for(int index = 0; index < data.size(); index++) {
			for(int byt = 0; byt < 4; byt++) {
				output[index * 4 + byt] = (byte) ((((int) data.get(index)) >> 8 * byt) & 0xFF);
			}
		}
		
		return output;
	}

	@Override
	protected Integer[] clone(Integer[] data) {
		Integer[] output = new Integer[data.length];
		
		for(int i = 0; i < data.length; i++) {
			output[i] = (int) data[i];
		}
		
		return output;
	}

	@Override
	protected Integer[] valueOf(String... input) throws IllegalDataFormatException {
		Integer[] output = new Integer[input.length];
		
		
		for(int i = 0; i < input.length; i++) {
			try {
				output[i] = Integer.parseUnsignedInt(input[i]);
			} catch(NumberFormatException e) {
				throw new IllegalDataFormatException("'" + input[i] + "' is not a valid 32-bit unsigned integer!", i);
			}
		}
		
		return output;
	}

	@Override
	protected String[] stringRepresentation(Integer[] data) {
		String[] output = new String[data.length];
		
		for(int i = 0; i < data.length; i++)
			output[i] = Integer.toUnsignedString(data[i]);
		
		return output;
	}

	
}
