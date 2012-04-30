package de.radicalfish.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

/**
 * This class serializes-, saves-, de-serializes- and loads objects.
 * Last update: 19.11.2011
 * 
 * @author Marco Knietzsch & Stefan Lange
 * @version 3.0.0
 * @since 27.05.2010
 */
public class IO_Object {

	/**
	 * Saves an Object to a specific dictionary.
	 * 
	 * @param o the object which shall be saved
	 * @param path where to save the object.
	 */
	public static void save(Object o, String path) {
		ObjectOutputStream outStream;
		try {
			outStream = new ObjectOutputStream(new FileOutputStream(path));
			
			outStream.writeObject(o);
			outStream.flush();
			outStream.close();
		}catch(InvalidClassException ex) {
			System.err.println(ex);
		}catch(OptionalDataException ex) {
			System.err.println(ex);
		}catch(FileNotFoundException ex) {
			System.err.println(ex);
		}catch(IOException ex) {
			System.err.println(ex);
		}
	}
	/**
	 * Loads and object from a given path.
	 * 
	 * @param path Where to find the object.
	 * @return A object representing some class without cast.
	 */
	public static Object load(String path) throws FileNotFoundException {
		Object o = new Object();
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(new FileInputStream(path));
			o = inStream.readObject();
			inStream.close();
			return o;
		}catch(InvalidClassException ex) {
			System.err.println(ex);
			return null;
		}catch(ClassNotFoundException ex) {
			System.err.println(ex);
			return null;
		}catch(OptionalDataException ex) {
			System.err.println(ex);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Loads and object from a given path in the class path.
	 * 
	 * @param path Where to find the object.
	 * @return A object representing some class without cast.
	 */
	public static Object loadFromClassPath(String path) throws FileNotFoundException {
		Object o = new Object();
		InputStream in = IO_Object.class.getClassLoader().getResourceAsStream(path);
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(in);
			o = inStream.readObject();
			inStream.close();
			return o;
		}catch(InvalidClassException ex) {
			System.err.println(ex);
			return null;
		}catch(ClassNotFoundException ex) {
			System.err.println(ex);
			return null;
		}catch(OptionalDataException ex) {
			System.err.println(ex);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * Loads an object from a given path as the left handed type.
	 * @param path the path to the object.
	 * @return A object representing a class automatically casted
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadAs(String path) throws IOException {
		T object = null;
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(new FileInputStream(path));
			object = (T) inStream.readObject();
			inStream.close();
			return object;
		} catch (InvalidClassException ex) {
			System.err.println(ex);
			return null;
		} catch (ClassNotFoundException ex) {
			System.err.println(ex);
			return null;
		} catch (OptionalDataException ex) {
			System.err.println(ex);
			return null;
		}
	}
	/**
	 * Loads an object from a given path int the class path as the left handed type.
	 * @param path the path to the object.
	 * @return A object representing a class automatically casted
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadFromClassPathAs(String path) throws FileNotFoundException {
		T o = null;
		InputStream in = IO_Object.class.getClassLoader().getResourceAsStream(path);
		ObjectInputStream inStream;
		try {
			inStream = new ObjectInputStream(in);
			o = (T) inStream.readObject();
			inStream.close();
			return o;
		}catch(InvalidClassException ex) {
			System.err.println(ex);
			return null;
		}catch(ClassNotFoundException ex) {
			System.err.println(ex);
			return null;
		}catch(OptionalDataException ex) {
			System.err.println(ex);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}