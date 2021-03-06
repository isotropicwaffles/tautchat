package com.neu.prattle.messaging;


/**
 * This enumeration represents special message addresses
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public enum MessageAddresses {

  /**
   * Represents User Service Address
   */
  USER_SERVICE("USER_SERVICE"),
  /**
   * Represents Session Service Address
   */
  SESSION_SERVICE("SESSION_SERVICE"),
  /**
   * Represents Group Service Address
   */
  GROUP_SERVICE("GROUP_SERVICE"),
  /**
   * Represents group message address
   */
  GROUP_MESSAGE("GROUP_MESSAGE"),
  /**
   * Represents a direct message address
   */
  DIRECT_MESSAGE("DIRECT_MESSAGE"),
  /**
   * Represents a sentiment analyses message address
   */
  SENTIMENT_ANALYSIS("SENTIMENT_ANALYSIS"),
  /**
   * Represents a broadcast message address
   */
  BROADCAST_MESSAGE("BROADCAST_MESSAGE");

  /**
   * Variable to store string associate with enum
   */
  public final String label;

  /**
   * This constructor associates the string values to each enumeration
   * @param label -enum string
   */
  MessageAddresses(String label) {
    this.label = label;
  }


}
