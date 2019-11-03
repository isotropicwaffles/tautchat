package com.neu.prattle.messaging;


/**
*
* This enumeration represents special message addresses
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public enum MessageAddresses{
	
	
    USERSERVICE("~USER_SERVICE"),
    BROADCAST("");

	 /**
	 * Variable to store string associate with enum
	 * 
	 */
	 public final String label;
	 
	 /**
	 * This constructor associates the string values to each enumeration
	 * 
	 */
    private MessageAddresses(String label) {
        this.label = label;
    }
    

}
