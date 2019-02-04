package autoui.data.structures;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import autoui.data.structures.exceptions.IllegalDataFormatException;
import autoui.streams.Streamable;

/**
 * 
 * @author jonas
 *
 * @param <T> - The type of data this data structure stores.
 */
public abstract class DataStructure<T> implements DataInstance, Streamable {
	
	protected final String name;
	protected final int id;
	protected T type;
	protected Dictionary<String, String> settings;
	protected ArrayList<T> data;
	
	protected String mainDataFormat = "string";
	protected String[] compatibleDataFormats = {"string"};
	
	/**
	 * 
	 * @param name - The data's name.
	 * @param id - The data's id.
	 */
	public DataStructure(String name, int id, String mainDataFormat, String[] compatibleDataFormats) {
		this.name = name;
		this.id = id;
		this.data = new ArrayList<T>();
		this.mainDataFormat = mainDataFormat;
		this.compatibleDataFormats = compatibleDataFormats;
	}
	
	/**
	 * 
	 * @param name - The data's name.
	 * @param id - The data's id.
	 * @param settings - Settings that regulate how the data is handled.
	 */
	public DataStructure(String name, int id, Dictionary<String, String> settings, String mainDataFormat, String[] compatibleDataFormats) {
		this.name = name;
		this.id = id;
		this.settings = settings;
		this.data = new ArrayList<T>();
		this.mainDataFormat = mainDataFormat;
		this.compatibleDataFormats = compatibleDataFormats;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public T getType() {
		return type;
	}
	
	/**
	 * 
	 * @param settings
	 */
	public final void setSettings(Dictionary<String, String> settings) {
		this.settings = settings;
	}
	
	/**
	 * 
	 * @return Number of elements the data structure currently holds.
	 */
	public final int size() {
		return data.size();
	}
	
	/**
	 * This function can be used to get a single data point.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @return The data instance in at the index.
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public final T getData(int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkBounds(true, false, false, index);
		
		return data.get(index);
	}
	
	/**
	 * This function can be used to get a range of data points.
	 * @param startIndex - The starting index of the selection. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param endIndex - The starting index of the selection. Has to be between startIndex (inclusive) and {@link #size()} (inclusive).
	 * @return The list of data instances specified by the interval.
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public final List<T> getData(int startIndex, int endIndex) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkBounds(true, true, true, startIndex, endIndex);
		
		return data.subList(startIndex, endIndex);	
	}
	
	/**
	 * This function can be used to get an arbitrary (ordered) set of elements.
	 * @param indices - An array of requested data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @return A list of the elements specified by the indices array.
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 */
	public final List<T> getData(int... indices) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkBounds(true, false, false, indices);
		
		List<T> output = new ArrayList<T>(indices.length);
		int counter = 0;
		for(int i : indices) {
			output.set(counter, data.get(i));
			counter++;
		}
		
		return output;
	}
	
	/**
	 * Clears the entire data list.
	 */
	public final void removeData() {
		data.clear();
	}
	
	
	/**
	 * Removes a single data point from the list.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void removeData(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
		checkBounds(true, false, false, index);
		
		data.remove(index);
	}
	
	/**
	 * Removes an interval of data points from the list.
	 * @param startIndex - The starting index of the selection. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param endIndex - The starting index of the selection. Has to be between startIndex (inclusive) and {@link #size()} (inclusive).
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void removeData(int startIndex, int endIndex) throws IndexOutOfBoundsException, IllegalArgumentException {
		checkBounds(true, true, true, startIndex, endIndex);
		
		for(int i = endIndex; i > endIndex; i--) {
			removeData(i);
		}
	}
	
	/**
	 * Removes an arbitrary set of data points from the list. 
	 * @param indices - An array of requested data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void removeData(int... indices) throws IndexOutOfBoundsException, IllegalArgumentException {
		checkBounds(false, false, false, indices);
		
		for(int i = 0; i < indices.length; i++) {
			removeData(indices[i]);
			
			for(int j = i + 1; j < indices.length; j++)
				if(indices[j] > indices[i])
					indices[j]--;
		}
	}
	
	/**
	 * Sets a single data point from the list.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param data - The data point to put into the list.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void setData(int index, T data) throws IndexOutOfBoundsException, IllegalArgumentException {
		removeData(index);
		pushData(index, data);
	}
	
	/**
	 * Sets an interval of data points from the list.
	 * @param startIndex - The starting index of the selection. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param endIndex - The starting index of the selection. Has to be between startIndex (inclusive) and {@link #size()} (inclusive).
	 * @param data - The data points to put into the list.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public final void setData(int startIndex, int endIndex, T... data) throws IndexOutOfBoundsException, IllegalArgumentException {
		if(endIndex - startIndex != data.length)
			throw new IllegalArgumentException("The number of new data points has to be the same as the number of removed ones!");
		
		removeData(startIndex, endIndex);
		pushData(startIndex, data);
	}
	
	/**
	 * Sets an arbitrary set of data points in the list.
	 * @param indices - An array of data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * 	The array has to have the same length as the data array.
	 * @param data - An array of data points. The array has to have the same length as the indices array.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public final void setData(int[] indices, T... data) throws IndexOutOfBoundsException, IllegalArgumentException {
		if(indices.length != data.length)
			throw new IllegalArgumentException("The number of new data points has to be the same as the number of removed ones!");
		
		checkBounds(false, false, false, indices);
		for(int i = 0; i < data.length; i++) {
			removeData(indices[i]);
			pushData(indices[i], data[i]);
		}
	}
	
	/**
	 * Swaps two data points inside of the list.
	 * @param index1 - The index of the first data point. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param index2 - The index of the second data point. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void swapData(int index1, int index2) throws IndexOutOfBoundsException, IllegalArgumentException {
		T temp = clone(toArray(getData(index1)))[0];
		
		setData(index1, getData(index2));
		setData(index2, temp);
	}
	
	/**
	 * Swaps an array of data points with one another (i.e. swaps indices1[0] with indices2[0], indices1[1] with indices2[1], etc.).
	 * @param indices1 - An array of data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * 	The array has to have the same length as indices2 array.
	 * @param indices2 - An array of data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * 	The array has to have the same length as indices1 array.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void swapData(int[] indices1, int[] indices2) throws IndexOutOfBoundsException, IllegalArgumentException {
		if(indices1.length != indices2.length)
			throw new IllegalArgumentException("The length of both arrays has to be the same.");
		for(int i = 0; i < indices1.length; i++) {
			T temp = clone(toArray(getData(indices1[i])))[0];
			
			setData(indices1[i], getData(indices2[i]));
			setData(indices2[i], temp);
		}
	}
	
	/**
	 * Appends the data list with the given array of data points.
	 * @param data - Data points to be appended to the list.
	 * @return true if the operation was successful.
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public final boolean pushData(T... data) throws IllegalArgumentException {
		T[] validData = validateData(data);
		
		for(T t : validData) {
			this.data.add(t);
		}
		
		return true;
	}
	
	/**
	 * Inserts an array of data points into the list at the specified index.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param data - Data points to be inserted into the list.
	 * @return true if the operation was successful.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public final boolean pushData(int index, T... data) throws IndexOutOfBoundsException, IllegalArgumentException {
		checkBounds(true, false, true, index, index + data.length);
		
		T[] validData = validateData(data);
		
		for(T t : validData) {
			this.data.add(index, t);
			index++;
		}

		return true;
	}
	
	/**
	 * Loads the data from a string array and pushes it at the end of the list.
	 * @param input - An array of string representations of data points.
	 * @throws IllegalDataFormatException
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void loadData(String... input) throws IllegalDataFormatException, IndexOutOfBoundsException, IllegalArgumentException {
		T[] _data = valueOf(input);
		
		pushData(_data);
	}
	
	
	/**
	 * Loads the data from a string array and pushes it into the list at the specified index.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param input - An array of string representations of data points.
	 * @throws IllegalDataFormatException
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final void loadData(int index, String... input) throws IllegalDataFormatException, IndexOutOfBoundsException, IllegalArgumentException {
		T[] _data = valueOf(input);
		
		pushData(index, _data);
	}
	
	
	/**
	 * Serializes every data point currently in the list.
	 * @return A string array of serialized data points.
	 */
	public final String[] serializeData() {
		String[] output = new String[data.size()];
		
		int counter = 0;
		for(T t : data) {
			output[counter] = stringRepresentation(toArray(t))[0];
		}
		return output;
	}
	
	

	/**
	 * 
	 * Serializes a data point in the list.
	 * @param index - An index within the data list. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @return The serialized data point in form of a string.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final String serializeData(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
		return stringRepresentation(toArray(getData(index)))[0];
	}


	
	/**
	 * 
	 * Serializes every data point between startIndex (inclusive) and endIndex (exclusive).
	 * @param startIndex - The starting index of the selection. Has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @param endIndex - The starting index of the selection. Has to be between startIndex (inclusive) and {@link #size()} (inclusive).
	 * @return A string array of serialized data points.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final String[] serializeData(int startIndex, int endIndex) throws IndexOutOfBoundsException, IllegalArgumentException {
		String[] output = new String[data.size()];
		
		int counter = 0;
		for(T t : getData(startIndex, endIndex)) {
			output[counter] = stringRepresentation(toArray(t))[0];
		}
		
		return output;
	}
	
	
	
	/**
	 * 
	 * Serializes every data point specified by the (ordered) set of indices.
	 * @param indices - An array of data indices. Each one has to be between 0 (inclusive) and {@link #size()} (exclusive).
	 * @return A string array of serialized data points.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	public final String[] serializeData(int... indices) throws IndexOutOfBoundsException, IllegalArgumentException {
		String[] output = new String[data.size()];
		
		int counter = 0;
		for(T t : getData(indices)) {
			output[counter] = stringRepresentation(toArray(t))[0];
		}

		return output;
	}
	
	
	@Override
	public final void streamIn(boolean overwrite, String... data) throws IllegalDataFormatException, IOException {
		int end = this.data.size();
		
		loadData(data);
		
		//Remove old data after writing has been successful.
		if(overwrite)
			removeData(0, end);
	}
	
	@Override
	public final String[] streamOutAll() throws IOException {
		return serializeData();
	}
	
	@Override
	public final String[] streamOut(int... indices) throws IndexOutOfBoundsException, IOException {
		try {
			return serializeData(indices);
		} catch(IllegalArgumentException e) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	public final String mainDataFormat() {
		return mainDataFormat;
	}
	
	
	@Override
	public final boolean compatible(String dataFormat) {
		return equalsOne(dataFormat, compatibleDataFormats);
	}
	
	
	
	/**
	 * Converts an array of string representations of data points into said data points.
	 * @param input - An array of string representations of data points.
	 * @return An array of data points.
	 * @throws IllegalDataFormatException
	 */
	protected abstract T[] valueOf(String... input) throws IllegalDataFormatException;
	
	
	/**
	 * Converts an array of data points into their string representation.
	 * @param data - An array of data points.
	 * @return The array of string representations of data.
	 */
	protected abstract String[] stringRepresentation(T[] data);
	
	/**
	 * Clones the array of data points.
	 * @param data - The data points to be cloned.
	 * @return The cloned array of data.
	 */
	protected abstract T[] clone(T[] data);
	
	/**
	 * Processes the data points so they fit the specifications declared in the settings.
	 * @param data - The array of data points to be processed.
	 * @return An array of valid data points.
	 * @throws IllegalArgumentException
	 */
	protected abstract T[] validateData(T[] data) throws IllegalArgumentException;
	
	/**
	 * Serializes the data into an array of bytes.
	 * @return The data as a byte array.
	 */
	protected abstract byte[] getDataBytes();
	
	/**
	 * Generates the hash for the data.
	 * @return The hash in hexadecimal as a string.
	 * @throws IllegalArgumentException
	 */
	public final String getHash() throws IllegalArgumentException {
		String hashAlgorithm = getSetting("hash-func");
		
		MessageDigest obj = null;
		try {
			obj = MessageDigest.getInstance(hashAlgorithm);
		} catch(NoSuchAlgorithmException e) {
			try {
				obj = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
		}
		
		int hashLength = obj.getDigestLength();
		
        byte[] bytesHash = obj.digest(getDataBytes());
        BigInteger intHash = new BigInteger(1, bytesHash);
        StringBuilder stringBHash = new StringBuilder();
        stringBHash.append(intHash.toString(16));
        while (stringBHash.length() < hashLength * 2) {
            stringBHash.insert(0, 0);
        }
        
        if(getSetting("hex-format") == null || getSetting("hex-format").equals("lower"))
        	return stringBHash.toString().toLowerCase();
        
        else if(getSetting("hex-format").equals("upper"))
        	return stringBHash.toString().toUpperCase();
        
        else
        	throw new IllegalArgumentException("'" + getSetting("hex-format") + "' is not a valid hex format!");
	}
	
	
	public final String toString() {
		return getHash();
	}
	
	/**
	 * Returns the value of the requested setting.
	 * @param name - The name of the setting.
	 * @return The value of the setting.
	 */
	protected final String getSetting(String name) {
		if(settings == null)
			return null;
		
		return settings.get(name.toLowerCase()).toLowerCase();
	}
	
	/**
	 * Checks whether the given indices are valid.
	 * @param doubleAllowed - Can indices appear more than once in the array?
	 * @param mustBeAscending - Does each index have to be greater or equal to the one before it?
	 * @param lastCanBeOutside - Can the last index in the array point directly behind the list?
	 * @param indices - The array of indices to be checked.
	 * @return true if the indices fit the requirements. false otherwise.
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 */
	protected final boolean checkBounds(boolean doubleAllowed, boolean mustBeAscending, 
			boolean lastCanBeOutside, int... indices) throws IndexOutOfBoundsException, IllegalArgumentException{
		for(int i = 0; i < indices.length; i++) {
			//Check bounds
			if(indices[i] < 0 || indices[i] > data.size() || 
					(indices[i] == data.size() && !(i == indices.length - 1 && lastCanBeOutside)))
				throw new IndexOutOfBoundsException();
			
			//Check ascending
			if(mustBeAscending) {
				if(i >= 1 && indices[i] < indices[i-1])
					throw new IllegalArgumentException("The indices must be in ascending order!");
			}
			
			//Check for doubles
			if(!doubleAllowed) {
				for(int j = i + 1; j < indices.length; j++)
					if(indices[i] == indices[j])
						throw new IllegalArgumentException("Indices must not be represented twice!");
			}
		}
		
		
		return true;
	}
	
	
	protected static final boolean equalsOne(String str, String... compare) {
		for(String s : compare)
			if(str.equals(s))
				return true;
		
		return false;
	}
	
	@SafeVarargs
	private final T[] toArray(T... data) {
		return data;
	}

}
