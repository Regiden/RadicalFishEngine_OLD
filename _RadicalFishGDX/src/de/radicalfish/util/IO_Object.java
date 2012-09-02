package de.radicalfish.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class serializes-, saves-, de-serializes- and loads objects. Not sure if this works well under Android, iOS, Web
 * and shit but Desktop will do.
 * 
 * @author Marco Knietzsch & Stefan Lange
 * @version 3.0.0
 * @since 27.05.2010
 */
public class IO_Object {
	
	/**
	 * Saves an Object to a specific dictionary.
	 * 
	 * @param o
	 *            the object which shall be saved
	 * @param path
	 *            where to save the object.
	 */
	public static void save(Object o, String path) throws RadicalFishException {
		ObjectOutputStream outStream;
		try {
			outStream = new ObjectOutputStream(new FileOutputStream(path));
			outStream.writeObject(o);
			outStream.flush();
			outStream.close();
		} catch (Exception ex) {
			throw new RadicalFishException(ex.getMessage(), ex.getCause());
		}
	}
	/**
	 * Loads and object from a given path.
	 * 
	 * @param path
	 *            Where to find the object.
	 * @return A object representing some class without cast.
	 */
	public static Object load(String path) throws RadicalFishException {
		Object o = new Object();
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(new FileInputStream(path));
			o = inStream.readObject();
			inStream.close();
			return o;
		} catch (Exception ex) {
			throw new RadicalFishException(ex.getMessage(), ex.getCause());
		}
	}
	/**
	 * Loads and object from a given path in the class path.
	 * 
	 * @param path
	 *            where to find the object.
	 * @return A object representing some class without cast.
	 */
	public static Object loadFromClassPath(String path) throws RadicalFishException {
		Object o = new Object();
		InputStream in = IO_Object.class.getClassLoader().getResourceAsStream(path);
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(in);
			o = inStream.readObject();
			inStream.close();
			return o;
		} catch (Exception ex) {
			throw new RadicalFishException(ex.getMessage(), ex.getCause());
		}
		
	}
	/**
	 * Loads an object from a given path as the left handed type.
	 * 
	 * @param path
	 *            the path to the object.
	 * @return A object representing a class automatically casted
	 */
	public static <T> T loadAs(String path) throws RadicalFishException {
		return loadAs(ResourceLoader.getResourceAsStream(path));
	}
	/**
	 * Loads an object from a given path as the left handed type.
	 * 
	 * @param stream
	 *            the stream from which we should load
	 * @return A object representing a class automatically casted
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadAs(InputStream stream) throws RadicalFishException {
		T object = null;
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(stream);
			object = (T) inStream.readObject();
			inStream.close();
			return object;
		} catch (Exception ex) {
			throw new RadicalFishException(ex.getMessage(), ex.getCause());
		}
	}
	
}