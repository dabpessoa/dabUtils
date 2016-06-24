package me.dabpessoa.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {

	public void teste() throws IOException {
		String filePath = "C:\\temp";
		String fileName = "contagemNotas.txt";
		Path path = Paths.get(filePath+"\\"+fileName);
		String newLine = System.getProperty("line.separator");
		
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		
		
		String s1 = "teste1";
		String s2 = "teste2";
		String s3 = "teste3";
		
		Files.write(path, s1.getBytes(), StandardOpenOption.APPEND);
		Files.write(path, newLine.getBytes(), StandardOpenOption.APPEND);
		Files.write(path, s2.getBytes(), StandardOpenOption.APPEND);
		Files.write(path, newLine.getBytes(), StandardOpenOption.APPEND);
		Files.write(path, s3.getBytes(), StandardOpenOption.APPEND);
		Files.write(path, newLine.getBytes(), StandardOpenOption.APPEND);
		
		System.out.println("FIM...");
	}
	
}
