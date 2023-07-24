package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GenerateDictionary {

	public static void main(String[] args) throws IOException {
		
		File file = new File("src/utils/initialList.txt");
		Scanner initialWords = new Scanner(file);
		ArrayList<String> dictionary = new ArrayList<>();
		while (initialWords.hasNextLine()) {
			String[] lineFile = initialWords.nextLine().split("\",\"");
			for (String word : lineFile) {
				dictionary.add(word.toUpperCase());
			}
        }
		Collections.sort(dictionary);
		initialWords.close();
		
		FileWriter fileGeneratedDictionary = new FileWriter(new File("src/utils/generatedDictionary.txt"));
	    BufferedWriter fileWriter = new BufferedWriter(fileGeneratedDictionary);
	    StringBuilder contentFile = new StringBuilder();
		for (String word : dictionary) {
			contentFile.append(word + "\n");
		}
		fileWriter.write(contentFile.deleteCharAt(contentFile.length()-1).toString());
		fileWriter.close();
	}

}
