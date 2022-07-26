package wordle;

public enum BestFirstWordsWordle {

	EN("SOARE"),
	FR("RAIES"),
	GE("RATES"),
	SP("SERIA"),
	IT("SERIA");
	
	String bestFirstWord;
	
	private BestFirstWordsWordle(String bestFirstWord) {
		this.bestFirstWord = bestFirstWord;
	}
	
	public String getBestFirstWord(){
        return this.bestFirstWord;
    }
}
