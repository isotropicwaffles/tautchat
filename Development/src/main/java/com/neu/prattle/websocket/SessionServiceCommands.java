package com.neu.prattle.websocket;


/**
 * This enumeration represents the different types of Session Service Message Content
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/04/2019
 */
public enum SessionServiceCommands {


  LOGIN("LOGIN");

  /**
   * Variable to store string associate with enum
   */
  public final String label;

  /**
   * This constructor associates the string values to each enumeration
   * 
   * @param label for enum
   */
  SessionServiceCommands(String label) {
    this.label = label;
  }


}
