package com.neu.prattle.messaging;


/**
 * This enumeration represents the different types of generic messeage responses
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/11/2019
 */
public enum GenericMessageResponses {


  /**
   * Represents Success Response
   */
  SUCCESS_RESPONSE("SUCCESS"),
  /**
   * Represents Failure Reponse
   */
  FAILURE_RESPONSE("FAILURE"),
  /**
   * Represents an Unknown Command Reponse
   */
  UNKNOWN_COMMAND("UNKNOWN_COMMAND");


  /**
   * Variable to store string associate with enum
   */
  public final String label;

  /**
   * This constructor associates the string values to each enumeration
   */
  GenericMessageResponses(String label) {
    this.label = label;
  }
}
