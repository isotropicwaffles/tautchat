package com.neu.prattle.service;


/**
*
* This enumeration represents the different types of GroupService Message Content
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public enum GroupServiceCommands {
	

	/**
	* Represents Group Create Command
	*/
    GROUP_CREATE("GROUP_CREATE"),
    
	/**
	* Represents Group Create Command
	*/
    GROUP_DELETE("GROUP_DELETE"), 
    
	/**
	* Represents Group Add User Command
	*/
    APPROVE_ADD_USER("APPROVE_ADD_USER"),
    
	/**
	* Represents Group Add User Command
	*/
    APPROVE_ADD_GROUP("APPROVE_ADD_GROUP"),

	/**
	* Represents Remove USER from Group Command
	*/
    REMOVE_USER("REMOVE_USER"),
	
    /**
	* Represents Remove Group from Group Command
	*/
    REMOVE_GROUP("REMOVE_GROUP"),
    

	/**
	* Represents a Request to Add User to GROUP Command
	*/
    REQUEST_ADD_USER("ADD_USER"),
    
	/**
	* Represents a Request to Add Group to Group Command
	*/
    REQUEST_ADD_GROUP("ADD_GROUP"),
	/**
	* Represents a request to get the group users
	*/
    GET_GROUP_USERS("GET_GROUP_USERS"),
	/**
	* Represents a request to get the group moderator
	*/
    GET_GROUP_MODERATOR("GET_GROUP_MODERATOR");

	
	 /**
	 * Variable to store string associate with enum
	 * 
	 */
	 public final String label;
	 
	 /**
	 * This constructor associates the string values to each enumeration
	 * 
	 */
    private GroupServiceCommands(String label) {
        this.label = label;
    }
}
