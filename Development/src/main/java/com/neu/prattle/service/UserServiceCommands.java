package com.neu.prattle.service;


/**
*
* This enumeration represents the different types of UserService Message Content
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public enum UserServiceCommands {
	
	
    LOGIN("LOGIN"),
    USERCREATE("USERCREATE"),
    SUCCESS_RESPONSE("SUCCESS"),
    FAILURE_RESPONSE("FAILURE");

	
	 /**
	 * Variable to store string associate with enum
	 * 
	 */
	 public final String label;
	 
	 /**
	 * This constructor associates the string values to each enumeration
	 * 
	 */
    private UserServiceCommands(String label) {
        this.label = label;
    }
}
