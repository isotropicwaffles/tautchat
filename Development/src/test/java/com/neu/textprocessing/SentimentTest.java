package com.neu.textprocessing;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.textprocessing.SentimentAnalysis;

import org.apache.commons.lang3.tuple.Pair;


/**
* This class provides tests of the sentiment analysis text
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 10/29/2019
*/
public class SentimentTest {

	
	@Before
	public void setUp() {
	
	}
	
	@After
	public void destroy() {
	}
	
	// This tests positive Sentiments
	@Test
	public void testPositiveSentiment() throws IOException{
		String happy_text = "Happy Happy Joy Joy. Happy Happy Joy Joy!";

		Pair<String[],Sentiment[]> sentimentText = SentimentAnalysis.analyzeText(happy_text);
		
		
		assertEquals("Happy Happy Joy Joy.", sentimentText.getLeft()[0]);
		assertEquals(" Happy Happy Joy Joy!", sentimentText.getLeft()[1]);
		assertEquals(Sentiment.POSITIVE, sentimentText.getRight()[0]);
		assertEquals(Sentiment.POSITIVE, sentimentText.getRight()[1]);
				
	}
	
	// This tests negative Sentiments
	@Test
	public void testNegativeSentiment() throws IOException{
		String happy_text = "I hate everything";

		Pair<String[],Sentiment[]> sentimentText = SentimentAnalysis.analyzeText(happy_text);
		
		
		assertEquals(happy_text, sentimentText.getLeft()[0]);
		assertEquals(Sentiment.NEGATIVE, sentimentText.getRight()[0]);
				
	}
	
	// This tests neutral Sentiments
	@Test
	public void testNeutralSentiment() throws IOException{
		String happy_text = "In Sentiment analysis, the neutrality is handled in various ways.";

		Pair<String[],Sentiment[]> sentimentText = SentimentAnalysis.analyzeText(happy_text);
		
		
		assertEquals("In Sentiment analysis, the neutrality is handled in various ways.", sentimentText.getLeft()[0]);
		assertEquals(Sentiment.NEUTRAL, sentimentText.getRight()[0]);
				
	}
	
	
	
	// This tests mixed Sentiments
	@Test
	public void testMixedSentiment() throws IOException{
		String happy_text = "Happy Happy Joy Joy? I hate everything! In Sentiment analysis, the neutrality is handled in various ways.";

		Pair<String[],Sentiment[]> sentimentText = SentimentAnalysis.analyzeText(happy_text);
		
		assertEquals("Happy Happy Joy Joy?", sentimentText.getLeft()[0]);
		assertEquals(Sentiment.POSITIVE, sentimentText.getRight()[0]);
		assertEquals(" I hate everything!", sentimentText.getLeft()[1]);
		assertEquals(Sentiment.NEGATIVE, sentimentText.getRight()[1]);
		assertEquals(" In Sentiment analysis, the neutrality is handled in various ways.", sentimentText.getLeft()[2]);
		assertEquals(Sentiment.NEUTRAL, sentimentText.getRight()[2]);
				
	}
	
	// This tests empty text Sentiments
	@Test
	public void testEmptyText() throws IOException{
		String emptytext = "";

		Pair<String[],Sentiment[]> sentimentText = SentimentAnalysis.analyzeText(emptytext);
		
				
	}
	
}
