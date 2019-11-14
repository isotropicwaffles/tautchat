package com.neu.prattle.service.user;


/**
*
* This enumeration represents the different types of UserService Message Content
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public enum UserServiceCommands {
	
	
	/**
	* Represents Login Command
	*/
    LOGIN("LOGIN"),
	/**
	* Represents User Create Command
	*/
    USER_CREATE("USER_CREATE"),
	/**
	* Represents Search for User by Name Command Command
	*/
    SEARCH_USERS_BY_NAME("SEARCH_USERS_BY_NAME");

	
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
