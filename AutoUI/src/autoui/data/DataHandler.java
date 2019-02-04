package autoui.data;

import java.util.Hashtable;

import autoui.data.structures.DataStructure;
import autoui.data.structures.StringData;
import autoui.data.structures.int32Data;
import autoui.data.structures.uint32Data;
import autoui.data.structures.exceptions.IllegalDataFormatException;

public class DataHandler {
	
	static int nextId = 1;
	
	@SuppressWarnings("rawtypes")
	private Hashtable<String, DataStructure> dataStructures;
	
	@SuppressWarnings("rawtypes")
	public DataHandler() {
		dataStructures = new Hashtable<String, DataStructure>();
	}
	
	@SuppressWarnings("rawtypes")
	public void add(DataStructure dataStructure) {
		dataStructures.put(dataStructure.getName(), dataStructure);
	}
	
	
	public void funnelIn(String dataStructure, boolean overwrite, int index,  String... data) 
			throws IndexOutOfBoundsException, IllegalDataFormatException, IllegalArgumentException {
		
		if(overwrite && index != 0)
			throw new IllegalArgumentException("Cannot both overwrite and not start at the beginning!");
		
		if(overwrite)
			dataStructures.get(dataStructure).removeData();
		
		dataStructures.get(dataStructure).loadData(index, data);
	}
	
	public String[] funnelOut(String dataStructure) {
		return dataStructures.get(dataStructure).serializeData();
	}
	
	@SuppressWarnings("rawtypes")
	public static DataStructure getSturctureByFormat(String format, String name) {
		format = format.toLowerCase();
		
		switch(format) {
		case "int": case "integer": case "int32":
			return new int32Data(name, nextId++);
		case "uint": case "unsigned int": case "unsigned integer": case "uint16":
			return new uint32Data(name, nextId++);
		default:
			return new StringData(name, nextId++);
		}
	}
	
	public static boolean validFormat(String format) {
		format = format.toLowerCase();
		
		switch(format) {
		case "int": case "integer": case "int32":
		case "uint": case "unsigned int": case "unsigned integer": case "uint16":
		case "string": case "str":
			return true;
		default:
			return false;
		}
	}
	
	public static boolean validSetting(String setting, String value) {
		setting = setting.toLowerCase();
		value = value.toLowerCase();
		
		switch(setting) {
		case "hash-func":
			return equalsOne(value, "md2", "md5", "sha-1", "sha-256", "sha-384", "sha-512");
		case "hex-format":
			return equalsOne(value, "upper", "lower");
		default:
			return false;
		}
	}
	
	private static boolean equalsOne(String str, String... compare) {
		for(String s : compare)
			if(str.equals(s))
				return true;
		
		return false;
	}

}
