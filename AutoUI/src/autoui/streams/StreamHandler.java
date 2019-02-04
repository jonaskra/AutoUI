package autoui.streams;

import java.io.IOException;

import autoui.data.structures.exceptions.IllegalDataFormatException;
import autoui.streams.exceptions.StreamException;

public class StreamHandler {
	
	private Streamable out, in;
	
	public StreamHandler(Streamable out, Streamable in) {
		this.out = out;
		this.in = in;
	}
	
	public boolean valid() {
		return in.compatible(out.mainDataFormat());
	}
	
	public boolean reverseValid() {
		return out.compatible(in.mainDataFormat());
	}
	
	public void stream(boolean overwrite) throws IllegalDataFormatException, IOException {
		if(!valid())
			throw new StreamException("Incompatible formats!");
		
		in.streamIn(overwrite, out.streamOutAll());
	}
	
	public void stream(boolean overwrite, int... indices) throws IllegalDataFormatException, IOException, IndexOutOfBoundsException {
		if(!valid())
			throw new StreamException("Incompatible formats!");
		
		in.streamIn(overwrite, out.streamOut(indices));
	}

}
