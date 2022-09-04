package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import sutom.BestFirstWordsSutom;
import sutom.Sutom;
import wordle.BestFirstWordsWordle;
import wordle.Wordle;

public class PlayGame {

	public static void main(String[] args) throws IOException {
		
		/*for (BestFirstWordsWordle country : BestFirstWordsWordle.values()) {
			playWordle(country.name());
		}*/
		
		//playWordle("EN");
		//playSutom();
	}
	
	public static void playSutom() throws IOException {
		
		FileWriter fileResults = new FileWriter(new File("src/simulation/results.txt"));
	    BufferedWriter fileWriter = new BufferedWriter(fileResults);
	    StringBuilder contentFile = new StringBuilder();
	    
	    HashMap<Integer, Integer> results = new HashMap<>();
		for (int i = 1; i <= 9; i++) {
			results.put(i, 0);
		}
		int numberWords = 0;
	    
	    for (BestFirstWordsSutom typeWord : BestFirstWordsSutom.values()) {
	    	HashMap<Integer, Integer> resultsTypeWord = new HashMap<>();
			for (int i = 1; i <= 9; i++) {
				resultsTypeWord.put(i, 0);
			}
	    	
	    	String typeWordName = typeWord.name();
	    	String firstLetter = Character.toString(typeWordName.charAt(0));
	    	int wordLength = Integer.parseInt(Character.toString(typeWordName.charAt(1)));
	    	ArrayList<String> allowedWords = Sutom.fetchWordsFromDictionary(wordLength, firstLetter);
	    	ArrayList<String> possibleWordsFile = new ArrayList<>(allowedWords);
	    	int possibleWordsFileSize = possibleWordsFile.size();
	    	numberWords += possibleWordsFileSize;
			ArrayList<String> combinations = Sutom.generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, wordLength-1);
			String bestFirstWord = typeWord.getBestFirstWord().substring(1);
			HashMap<String, String> bestSecondWords = Sutom.fetchBestSecondWords("sutom/bestWords/" + firstLetter + bestFirstWord);
			String wordFoundCombination = computeCombination(bestFirstWord, bestFirstWord);
			
			for (String possibleWordFile : possibleWordsFile) {
				contentFile.append(firstLetter + possibleWordFile + " : ");
				ArrayList<String> possibleWords = new ArrayList<>(possibleWordsFile);
				String bestWord = "";
				String combination = "";
				int numberAttempts = 0;
				while (possibleWords.size() > 2) {
					if (bestWord == "") {
						bestWord = bestFirstWord;
					} else if (bestWord == bestFirstWord) {
						bestWord = bestSecondWords.get(combination).substring(1);
					} else {
						bestWord = Sutom.findBestWord(allowedWords, combinations, possibleWords);
					}
					combination = computeCombination(possibleWordFile, bestWord);
					possibleWords = Sutom.fetchPossibleWords(bestWord, combination, possibleWords);
					contentFile.append(firstLetter + bestWord + "(2" + combination + ") ");
					numberAttempts++;
				}
				if (possibleWords.size() == 1) {
					if (!bestWord.equals(possibleWordFile)) {
						contentFile.append(firstLetter + possibleWordFile + "(2" + wordFoundCombination + ")");
						numberAttempts++;
					}
				} else {
					bestWord = possibleWords.get(0);
					if (bestWord.equals(possibleWordFile)) {
						contentFile.append(firstLetter + possibleWordFile + "(2" + wordFoundCombination + ")");
						numberAttempts++;
					} else {
						combination = computeCombination(possibleWordFile, bestWord);
						contentFile.append(firstLetter + bestWord + "(2" + combination + ") " + firstLetter + possibleWordFile  + "(2" + wordFoundCombination + ")");
						numberAttempts+=2;
					}
				}
				results.put(numberAttempts, results.get(numberAttempts)+1);
				resultsTypeWord.put(numberAttempts, resultsTypeWord.get(numberAttempts)+1);
				contentFile.append("\n");
			}
			
			System.out.println(wordLength + firstLetter);
			double averageTypeWord = 0;
			for (int i = 1; i <= 9; i++) {
				int resultTypeWord = resultsTypeWord.get(i);
				if (resultTypeWord > 0) {
					System.out.print(i + " : " + resultTypeWord + " | ");
					averageTypeWord += i * resultTypeWord;
				}
			}
			averageTypeWord /= possibleWordsFileSize;
			System.out.println("\nMoyenne = " + averageTypeWord);
	    }
	    contentFile.append("\n");
		
		double average = 0;
		for (int i = 1; i <= 9; i++) {
			int result = results.get(i);
			if (result > 0) {
				contentFile.append(i + " : " + result + " | ");
				average += i * result;
			}
		}
		average /= numberWords;
		contentFile.append("\nMoyenne = " + average);
		
		fileWriter.write(contentFile.toString());
		fileWriter.close();
	}
	
	public static void playWordle(String language) throws IOException {
		
		FileWriter fileResults = new FileWriter(new File("src/simulation/results" + language + ".txt"));
	    BufferedWriter fileWriter = new BufferedWriter(fileResults);
	    StringBuilder contentFile = new StringBuilder();
	    
	    ArrayList<String> allowedWords = Wordle.fetchWordsFromFile(language + "/allowedWords");
		ArrayList<String> possibleWordsFile = Wordle.fetchWordsFromFile(language + "/possibleWords");
		ArrayList<String> combinations = Sutom.generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, 5);
		
		String bestFirstWord = Enum.valueOf(BestFirstWordsWordle.class, language).getBestFirstWord();
		HashMap<String, String> bestSecondWords = Sutom.fetchBestSecondWords("wordle/" + language + "/" + bestFirstWord);
			
		HashMap<Integer, Integer> results = new HashMap<>();
		for (int i = 1; i <= 9; i++) {
			results.put(i, 0);
		}
		for (String possibleWordFile : possibleWordsFile) {
			System.out.println(language + " " + possibleWordFile);
			contentFile.append(possibleWordFile + " : ");
			ArrayList<String> possibleWords = new ArrayList<>(possibleWordsFile);
			String bestWord = "";
			String combination = "";
			int numberAttempts = 0;
			while (possibleWords.size() > 2) {
				if (bestWord == "") {
					bestWord = bestFirstWord;
				} else if (bestWord == bestFirstWord) {
					bestWord = bestSecondWords.get(combination);
				} else {
					bestWord = Sutom.findBestWord(allowedWords, combinations, possibleWords);
				}
				combination = computeCombination(possibleWordFile, bestWord);
				possibleWords = Sutom.fetchPossibleWords(bestWord, combination, possibleWords);
				contentFile.append(bestWord + "(" + combination + ") ");
				numberAttempts++;
			}
			if (possibleWords.size() == 1) {
				if (!bestWord.equals(possibleWordFile)) {
					contentFile.append(possibleWordFile + "(22222)");
					numberAttempts++;
				}
			} else {
				bestWord = possibleWords.get(0);
				if (bestWord.equals(possibleWordFile)) {
					contentFile.append(possibleWordFile + "(22222)");
					numberAttempts++;
				} else {
					combination = computeCombination(possibleWordFile, bestWord);
					contentFile.append(bestWord + "(" + combination + ") " + possibleWordFile + "(22222)");
					numberAttempts+=2;
				}
			}
			results.put(numberAttempts, results.get(numberAttempts)+1);
			contentFile.append("\n");
		}
		contentFile.append("\n");
		
		double average = 0;
		for (int i = 1; i <= 9; i++) {
			int result = results.get(i);
			if (result > 0) {
				contentFile.append(i + " : " + result + " | ");
				average += i * result;
			}
		}
		average /= possibleWordsFile.size();
		contentFile.append("\nAverage = " + average);
		
		fileWriter.write(contentFile.toString());
		fileWriter.close();
	}
	
	public static String computeCombination(String answer, String attempt) {
		
		String combination = "";
		
		int wordLength = answer.length();
		for (int i = wordLength-1; i >= 0; i--) {
			if (answer.charAt(i) == attempt.charAt(i)) {
				combination = "2" + combination;
				answer = answer.substring(0, i) + answer.substring(i+1);
			} else {
				combination = "0" + combination;
			}
		}
		
		for (int i = 0; i < wordLength; i++) {
			if (combination.charAt(i) == '0') {
				String letter = Character.toString(attempt.charAt(i));
				if (answer.contains(letter)) {
					combination = combination.substring(0, i) + "1" + combination.substring(i+1);
					answer = answer.replaceFirst(letter, "");
				}
			}
		}
		
		return combination;
	}

}
