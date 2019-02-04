package autoui.streams;

import java.io.IOException;

import autoui.data.structures.exceptions.IllegalDataFormatException;

public interface Streamable {
	
	public void streamIn(boolean overwrite, String... serializedData) throws IllegalDataFormatException, IOException;

	public String[] streamOutAll() throws IOException;
	
	public String[] streamOut(int... indices) throws IndexOutOfBoundsException, IOException;
	
	public String mainDataFormat();
	
	public boolean compatible(String dataFormat);

}
