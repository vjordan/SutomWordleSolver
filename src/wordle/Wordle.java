package wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import sutom.Sutom;

public class Wordle {
	
	public static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		
		// FIND BEST FIRST WORD
		
		/*ArrayList<String> combinations = Sutom.generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, 5);
		for (BestFirstWordsWordle country : BestFirstWordsWordle.values()) {
			String language = country.name();
			ArrayList<String> allowedWords = fetchWordsFromFile(language + "/allowedWords");
			ArrayList<String> possibleWords = fetchWordsFromFile(language + "/possibleWords");
			System.out.println(language + " " + Sutom.findBestWord(allowedWords, combinations, possibleWords));
		}*/
		
		// SAVE BEST SECOND WORDS
		
		/*String language = "EN";
		String bestFirstWord = Enum.valueOf(BestFirstWordsWordle.class, language).getBestFirstWord();
		ArrayList<String> allowedWords = fetchWordsFromFile(language + "/allowedWords");
		ArrayList<String> possibleWords = fetchWordsFromFile(language + "/possibleWords");
		ArrayList<String> combinations = Sutom.generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, 5);
		for (String combination : combinations) {
			if (Sutom.checkIfCombinationIsValid(bestFirstWord, combination)) {
				ArrayList<String> newPossibleWords = Sutom.fetchPossibleWords(bestFirstWord, combination, possibleWords);
				int newPossibleWordsSize = newPossibleWords.size();
				System.out.print(combination + " " + newPossibleWordsSize);
				if (newPossibleWordsSize > 2) {
					System.out.print(" " + Sutom.findBestWord(allowedWords, combinations, newPossibleWords));
				}
			} else {
				System.out.print(combination + " 0");
			}
			System.out.println();
		}*/
		
		//System.out.println("The answer is " + giveAnswer("EN"));
		for (BestFirstWordsWordle country : BestFirstWordsWordle.values()) {
			System.out.println("The answer is " + giveAnswer(country.name()) + "\n");
		}
	}
	
	public static String giveAnswer(String language) throws FileNotFoundException {
		
		ArrayList<String> allowedWords = fetchWordsFromFile(language + "/allowedWords");
		ArrayList<String> possibleWords = fetchWordsFromFile(language + "/possibleWords");
		ArrayList<String> combinations = Sutom.generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, 5);
		
		String bestFirstWord = Enum.valueOf(BestFirstWordsWordle.class, language).getBestFirstWord();
		
		String bestWord = "";
		String combination = "";
		while (possibleWords.size() > 2) {
			if (bestWord == "") {
				bestWord = bestFirstWord;
			} else if (bestWord == bestFirstWord) {
				HashMap<String, String> bestSecondWords = Sutom.fetchBestSecondWords("wordle/" + language + "/" + bestFirstWord);
				bestWord = bestSecondWords.get(combination);
			} else {
				bestWord = Sutom.findBestWord(allowedWords, combinations, possibleWords);
			}
			System.out.println(bestWord);
			combination = scanner.next();
			possibleWords = Sutom.fetchPossibleWords(bestWord, combination, possibleWords);
		}
		
		bestWord = possibleWords.get(0);
		if (possibleWords.size() == 1) {
			return bestWord;
		}
		System.out.println(bestWord);
		combination = scanner.next();
		if (!combination.contains("0") && !combination.contains("1")) {
			return bestWord;
		}
		return possibleWords.get(1);
	}
	
	public static ArrayList<String> fetchWordsFromFile(String filePath) throws FileNotFoundException {
		
		ArrayList<String> words = new ArrayList<>();
		
		File file = new File("src/wordle/" + filePath + ".txt");
		Scanner wordsFile = new Scanner(file);
		
        while (wordsFile.hasNextLine()) {
        	String word = wordsFile.nextLine();
    		words.add(word);
        }
        
        wordsFile.close();
		
		return words;
	}

}
