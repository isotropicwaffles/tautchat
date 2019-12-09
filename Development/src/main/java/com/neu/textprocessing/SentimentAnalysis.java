package com.neu.textprocessing;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.vader.sentiment.analyzer.SentimentAnalyzer;

/**
* This code utilizes the vadar sentiment analyzer that is MIT licensed 
* This class provides methods to perform sentiment analysis on a string
* This string can be a string of one or more sentences.
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 10/29/2019
*/
public class SentimentAnalysis {

	/**
	* Private constructor for SentimentAnalysis to hide public constructor
	* 
	*/
	private SentimentAnalysis() {}
	
	
	/**
	* Parses given text into sentences and analysizes text for sentiment
	* 
	* @param text- a string to perform sentiment analysis on
	*
	* @return Pair - a pair of string arrays representing distinct sentences and their respective sentiments
 	* @throws IOException - an ioexception
	*/
	public static Pair<String[],Sentiment[]> analyzeText(String text) throws IOException {
		
		String[] sentences = parseIntoSentences(text);
		
		return Pair.of(sentences, getSentiments(sentences));
		
	}
	
	
	/**
	* Returns character by character sentiment content
	* 
	* @param text- a string to perform sentiment analysis on
	*
	* @return charSentiments - character by character sentiments of the string
	*/
	public static String characterByCharacterSentiment(String text) {
		
		try {
		Pair<String[],Sentiment[]> sentimentPair = analyzeText(text);
		
		StringBuilder charSentimentBuilder = new StringBuilder();
		
		 for (int i = 0; i < sentimentPair.getLeft().length; i++) {
			 
			 
			 for (int j = 0; j <  sentimentPair.getLeft()[i].length(); j++){
				 
				 charSentimentBuilder.append(sentimentPair.getRight()[i].label);
			 }
		
		 }

			return charSentimentBuilder.toString();
			
		}catch(IOException e){
			return "";
		}
		
	}
	
	/**
	* Parses text into sentences
	* 
	* @param text- a string to parse into sentences
	*
	* @return String[] - a pair of string arrays representing distinct sentences
	*/
	private static String[] parseIntoSentences(String text) {
		return text.split("(?<=\\.)|(?<=\\!)|(?<=\\?)");
		
	}
	
		
	/**
	* Determine sentiment of each elements of a string array
	* 
	* @param sentences- a string array each element representing a sentence
	*
	* @return Sentiment[] - sentiments w.r.t. each string array element
	* 
	* @throws IOException - an ioexception
	*/
	private static Sentiment[] getSentiments(String[] sentences) throws IOException{
		
		Sentiment[] sentiments =  new Sentiment[sentences.length];
	    	for (int i = 0; i <sentences.length ; i++) {
	    		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer(sentences[i]);
	    		sentimentAnalyzer.analyze();
	    		sentiments[i] = identifySentiment(sentimentAnalyzer.getPolarity());
	    	}
	    	
	    return sentiments;
	}
	
	/**
	* identification of the overall compound sentiment of the polarities
	* 
	* @param polarity- a structure representing polarity 
	*
	* @return Sentiment - compound sentiement of the polarities
	*/
	private static Sentiment identifySentiment(Map<String, Float> polarity){
			
		Sentiment overallSentiment;
		
		if (polarity.get("compound") > POSITIVE_THRESHOLD) {
			overallSentiment = Sentiment.POSITIVE;	
			
		}else if(polarity.get("compound") < NEGATIVE_THRESHOLD){
			overallSentiment = Sentiment.NEGATIVE;
			
		}else {
			overallSentiment = Sentiment.NEUTRAL;	
		}
		
		return overallSentiment;
	}
	
	/**
	* Threshold to consider compound polarity positive
	* 
	*/
	public static final float POSITIVE_THRESHOLD = 0.05f;
	
	/**
	* Threshold to consider compound polarity negative
	* 
	*/
	public static final float NEGATIVE_THRESHOLD = -0.05f;


	
	
	

}
