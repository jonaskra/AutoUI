package autoui.streams;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import autoui.data.structures.exceptions.IllegalDataFormatException;

public class FileStream implements Streamable {
	
	private File file;
	
	public FileStream(File file) {
		this.file = file;
	}

	@Override
	public void streamIn(boolean overwrite, String... serializedData) throws IllegalDataFormatException, IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, !overwrite));

		for(int i = 0; i < serializedData.length; i++) {
			writer.write(serializedData[i]);
			writer.newLine();
		}

		writer.close();
	}

	@Override
	public String[] streamOutAll() throws IOException {
		return readFile();
	}

	@Override
	public String[] streamOut(int... indices) throws IndexOutOfBoundsException, IOException {
		String[] lines = readFile();
		String[] output = new String[indices.length];
		
		int counter = 0;
		for(int index : indices) {
			if(index < 0 || index > lines.length)
				throw new IndexOutOfBoundsException("Line " + index + " does not exist in file " + file.getName() + "!");
			
			output[counter] = lines[index];
			counter++;
		}
		
		return output;
	}

	@Override
	public String mainDataFormat() {
		return "string";
	}

	@Override
	public boolean compatible(String dataFormat) {
		return true;
	}
	
	private String[] readFile() throws FileNotFoundException {
		List<String> lines = new LinkedList<String>();
		
		
		Scanner s = new Scanner(file);
		
		while(s.hasNextLine())
			lines.add(s.nextLine());
		
		s.close();
		
		String[] output = new String[lines.size()];
		lines.toArray(output);
		lines.clear();
		lines = null;
		
		return output;
	}

}
