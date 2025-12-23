package org.unibl.etf.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class XMLSerialization {
	public static <T> void serializeXML(ArrayList<T> data, String path) throws FileNotFoundException {
		try(XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File(path)))) {
			encoder.writeObject(data);
		}
	}

	public static <T> ArrayList<T> deserializeXML(String path) throws FileNotFoundException {
		try(XMLDecoder decoder = new XMLDecoder(new FileInputStream(path))) {
			return (ArrayList<T>) decoder.readObject();
		}
	}
	
	public static String generateId() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @param fileWithExtension file name with xml format (example.xml)
	 * @return file path on Tomcat server
	 */
	public static String getDataPath(String fileWithExtension) {
	    String base = System.getProperty("catalina.base");
	    return base + File.separator + Config.get("file.root.path")
	                + File.separator + fileWithExtension;
	}
	
	public static <T> ArrayList<T> readAll(String xmlPath) throws FileNotFoundException {
		try {
			return deserializeXML(xmlPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to read data from file, but file not found at " + xmlPath);
		}
	}
	
	public static <T> void writeAll(ArrayList<T> entities, String xmlPath) throws FileNotFoundException {
		try {
			XMLSerialization.serializeXML(entities, xmlPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Trying to write data in directory, but directory not found at " + xmlPath);
		}
	}
}
