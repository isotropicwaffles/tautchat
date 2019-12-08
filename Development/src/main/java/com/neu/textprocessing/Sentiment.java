package com.neu.textprocessing;

/**
* This class represents an enumerations of sentiments 
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 10/29/2019
*/
public enum Sentiment {
	POSITIVE("2"),
	NEUTRAL("1"),
	NEGATIVE("0");

	
	/**
	 * Variable to store string associate with enum
	 */
	public final String label;

	/**
	 * This constructor associates the string values to each enumeration
	 * 
     * @param label for enum
	 */
	Sentiment(String label) {
	  this.label = label;
	}
}



