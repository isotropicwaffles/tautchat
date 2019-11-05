package com.neu.prattle.messaging;


/**
*
* This enumeration represents special message addresses
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public enum MessageAddresses{
	
	
    USER_SERVICE("~USER_SERVICE"),
    SESSION_SERVICE("~SESSION_SERVICE"),
    GROUP_SERVICE("~GROUP_SERVICE"),//(TODO) Hook up logic for using this
    GROUP_MESSAGE("~GROUP_MESSAGE"),//(TODO) Hook up logic for using this
    DIRECT_MESSAGE("~DIRECT_MESSAGE"), //(TODO) Hook up logic for using this
    BROADCAST_MESSAGE("~BROADCAST_MESSAGE"); //(TODO) Hook up logic for using this

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
