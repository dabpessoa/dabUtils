package me.dabpessoa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableUtils {
	
	public static <T> void writeObject(T object, File path) throws FileNotFoundException, IOException {
		try (FileOutputStream fout = new FileOutputStream(path);
			 ObjectOutputStream oos = new ObjectOutputStream(fout);){
			oos.writeObject(object);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readObject(File path) throws FileNotFoundException, IOException, ClassNotFoundException {
		try (FileInputStream fin = new FileInputStream(path);
			 ObjectInputStream ois = new ObjectInputStream(fin);){
			return (T) ois.readObject();
		}
	}
	
}
