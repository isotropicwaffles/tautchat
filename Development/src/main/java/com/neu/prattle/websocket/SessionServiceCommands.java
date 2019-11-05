package com.neu.prattle.websocket;


/**
*
* This enumeration represents the different types of Session Service Message Content
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/04/2019
*/
public enum SessionServiceCommands {
	
	
    LOGIN("LOGIN");
	
	 /**
	 * Variable to store string associate with enum
	 * 
	 */
	 public final String label;
	 
	 /**
	 * This constructor associates the string values to each enumeration
	 * 
	 */
    private SessionServiceCommands(String label) {
        this.label = label;
    }
    

    //(TODO) COMPLETE LOGIC/COMMENTS
   public String formLoginCommand(String preLoginId, String newUserName) {
       return LOGIN.label + " " + preLoginId + "~" + newUserName ;
   }
    
}
