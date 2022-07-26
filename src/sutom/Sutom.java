package sutom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
//import java.io.BufferedWriter;
//import java.io.FileWriter;

public class Sutom {
	
	public static double LOG2 = Math.log(2);
	public static double bestEntropy = 0;
	public static String bestWord;

	public static void main(String[] args) throws IOException {
		
		// FIND BEST FIRST WORD
		
		/*String letters = "ABCDEFGHIJLMNOPRSTUV";
		for (int wordLength = 6; wordLength <= 9; wordLength++) {
			ArrayList<String> combinations = generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, wordLength-1);
			for (int positionLetter = 0; positionLetter < letters.length(); positionLetter++) {
				String firstLetter = Character.toString(letters.charAt(positionLetter));
				ArrayList<String> wordsDictionary = fetchWordsFromDictionary(wordLength, firstLetter);
				String bestWord = findBestWord(wordsDictionary, combinations, wordsDictionary);
				System.out.println(wordLength + firstLetter + " " + firstLetter + bestWord);
			}
		}*/
		
		// SAVE BEST SECOND WORDS
		
		/*String letters = "ABCDEFGHIJLMNOPRSTUV";
		for (int wordLength = 6; wordLength <= 9; wordLength++) {
			ArrayList<String> combinations = generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, wordLength-1);
			for (int positionLetter = 0; positionLetter < letters.length(); positionLetter++) {
				String firstLetter = Character.toString(letters.charAt(positionLetter));		
				String bestFirstWord = Enum.valueOf(BestFirstWordsSutom.class, firstLetter + wordLength).getBestFirstWord().substring(1);
				FileWriter fileBestSecondsWords = new FileWriter(new File("src/sutom/bestWords/" + firstLetter + bestFirstWord + ".txt"));
			    BufferedWriter fileWriter = new BufferedWriter(fileBestSecondsWords);
			    StringBuilder contentFile = new StringBuilder();
				ArrayList<String> allowedWords = fetchWordsFromDictionary(wordLength, firstLetter);
				ArrayList<String> possibleWords = new ArrayList<>(allowedWords);
				for (String combination : combinations) {
					if (checkIfCombinationIsValid(bestFirstWord, combination)) {
						ArrayList<String> newPossibleWords = fetchPossibleWords(bestFirstWord, combination, possibleWords);
						int newPossibleWordsSize = newPossibleWords.size();
						contentFile.append(combination + " " + newPossibleWordsSize);
						if (newPossibleWordsSize > 2) {
							contentFile.append(" " + firstLetter + findBestWord(allowedWords, combinations, newPossibleWords));
						}
					} else {
						contentFile.append(combination + " 0");
					}
					contentFile.append("\n");
				}
				fileWriter.write(contentFile.deleteCharAt(contentFile.length()-1).toString());
				fileWriter.close();
				System.out.println(firstLetter + bestFirstWord);
			}
		}*/
		
		System.out.println("La réponse est " + giveAnswer(6, "A"));
	}
	
	public static String giveAnswer(int wordLength, String firstLetter) throws FileNotFoundException {
		
		Scanner scanner = new Scanner(System.in);
		
		ArrayList<String> allowedWords = fetchWordsFromDictionary(wordLength, firstLetter);
		ArrayList<String> possibleWords = new ArrayList<>(allowedWords);
		ArrayList<String> combinations = generateCombinations(new ArrayList<>(Arrays.asList("0", "1", "2")), 1, wordLength-1);
		
		String bestFirstWord = Enum.valueOf(BestFirstWordsSutom.class, firstLetter + wordLength).getBestFirstWord().substring(1);
		HashMap<String, String> bestSecondWords = fetchBestSecondWords("sutom/bestWords/" + firstLetter + bestFirstWord);
		
		String bestWord = "";
		String combination = "";
		while (possibleWords.size() > 2) {
			if (bestWord == "") {
				bestWord = bestFirstWord;
			} else if (bestWord == bestFirstWord) {
				bestWord = bestSecondWords.get(combination).substring(1);
			} else {
				bestWord = findBestWord(allowedWords, combinations, possibleWords);
			}
			System.out.println(firstLetter + bestWord);
			System.out.print("2");
			combination = scanner.next();
			possibleWords = fetchPossibleWords(bestWord, combination, possibleWords);
		}
		
		bestWord = possibleWords.get(0);
		if (possibleWords.size() == 1) {
			scanner.close();
			return firstLetter + bestWord;
		}
		System.out.println(firstLetter + bestWord);
		System.out.print("2");
		combination = scanner.next();
		scanner.close();
		if (!combination.contains("0") && !combination.contains("1")) {
			return firstLetter + bestWord;
		}
		return firstLetter + possibleWords.get(1);
	}
	
	public static ArrayList<String> fetchPossibleWords(String word, String combination, ArrayList<String> possibleWords) {
		
		ArrayList<String> newPossibleWords = new ArrayList<>();
		
		for (String possibleWord : possibleWords) {
			if (checkIfWordIsValid(word, combination, possibleWord)) {
				newPossibleWords.add(possibleWord);
			}
		}
		
		return newPossibleWords;
	}
	
	public static String findBestWord(ArrayList<String> allowedWords, ArrayList<String> combinations, ArrayList<String> possibleWords) {
		
		int possibleWordsSize = possibleWords.size();
		allowedWords.parallelStream().forEach(allowedWord -> {
			double entropy = 0;
			for (String combination : combinations) {
				if (checkIfCombinationIsValid(allowedWord, combination)) {
					int numberValidWords = 0;
					for (String possibleWord : possibleWords) {
						if (checkIfWordIsValid(allowedWord, combination, possibleWord)) {
							numberValidWords++;
						}
					}
					if (numberValidWords > 0 && numberValidWords < possibleWordsSize) {
						double ratioValidWords = (double) numberValidWords / possibleWordsSize;
						entropy += -ratioValidWords * (Math.log(ratioValidWords)/LOG2);
					}
				}
			}
			if (entropy > bestEntropy) {
				bestEntropy = entropy;
				bestWord = allowedWord;
			}
		});
		bestEntropy = 0;
		
		return bestWord;
	}
	
	public static boolean checkIfCombinationIsValid(String word, String combination) {
		
		ArrayList<Character> lettersToCheck = new ArrayList<>();
		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			char type = combination.charAt(i);
			if (type == '0') {
				lettersToCheck.add(letter);
			} else if (type == '1' && lettersToCheck.contains(letter)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean checkIfWordIsValid(String allowedWord, String combination, String possibleWord) {
		
		int combinationLength = combination.length();
		for (int i = 0; i < combinationLength; i++) {
			char letterAllowedWord = allowedWord.charAt(i);
			char letterPossibleWord = possibleWord.charAt(i);
			if (combination.charAt(i) == '2') {
				if (letterAllowedWord != letterPossibleWord) {
					return false;
				}
			} else if (letterAllowedWord == letterPossibleWord) {
				return false;
			}
		}
		
		HashMap<Integer, ArrayList<Integer>> combinationDistribution = new HashMap<>();
		for (int i = combinationLength-1; i >= 0; i--) {
			int type = Integer.parseInt(Character.toString(combination.charAt(i)));
			ArrayList<Integer> previousPositions = combinationDistribution.get(type);
			if (previousPositions == null) {
				combinationDistribution.put(type, new ArrayList<>(Arrays.asList(i)));
			} else {
				previousPositions.add(i);
				combinationDistribution.put(type, previousPositions);
			}
		}
		
		for (int i = 2; i >= 0; i--) {
			ArrayList<Integer> positions = combinationDistribution.get(i);
			if (positions == null) {
				continue;
			}
			for (int position : positions) {
				switch (i) {
				case 2 :
					possibleWord = possibleWord.substring(0, position) + possibleWord.substring(position + 1);
					break;
				case 1 :
					String letter = Character.toString(allowedWord.charAt(position));
					if (!possibleWord.contains(letter)) {
						return false;
					}
					possibleWord = possibleWord.replaceFirst(letter, "");
					break;
				case 0 :
					if (possibleWord.contains(Character.toString(allowedWord.charAt(position)))) {
						return false;
					}
					break;
				}
			}
		}
		
		return true;
	}
	
	public static HashMap<String, String> fetchBestSecondWords(String filePath) throws FileNotFoundException {
		
		HashMap<String, String> bestSecondWordsByCombinations = new HashMap<>();
		
		File bestFirstWordFile = new File("src/" + filePath + ".txt");
		Scanner secondWords = new Scanner(bestFirstWordFile);
		
		while (secondWords.hasNextLine()) {
			String[] lineFile = secondWords.nextLine().split(" ");
			if (Integer.parseInt(lineFile[1]) > 2) {
				bestSecondWordsByCombinations.put(lineFile[0], lineFile[2]);
			}
        }
		
		secondWords.close();
		
		return bestSecondWordsByCombinations;
	}
	
	public static ArrayList<String> generateCombinations(ArrayList<String> combinations, int combinationLength, int expectedLength) {
		
		ArrayList<String> newCombinations = new ArrayList<>();
		for (String combination : combinations) {
			for (int i = 0; i <= 2; i++) {
				newCombinations.add(combination + i);
			}
		}
		
		combinationLength++;
		if (combinationLength == expectedLength) {
			return newCombinations;
		}
		return generateCombinations(newCombinations, combinationLength, expectedLength);
	}
	
	public static ArrayList<String> fetchWordsFromDictionary(int wordLength, String firstLetter) throws FileNotFoundException {
		
		ArrayList<String> words = new ArrayList<>();
		
		File file = new File("src/sutom/words.txt");
		Scanner wordsFile = new Scanner(file);

		boolean firstWordFound = false;
        while (wordsFile.hasNextLine()) {
        	String word = wordsFile.nextLine();
        	if (word.length() == wordLength && word.startsWith(firstLetter)) {
        		words.add(word.substring(1));
        		if (!firstWordFound) {
        			firstWordFound = true;
        		}
        	} else if (firstWordFound) {
        		break;
        	}
        }
        
        wordsFile.close();
		
		return words;
	}

}
