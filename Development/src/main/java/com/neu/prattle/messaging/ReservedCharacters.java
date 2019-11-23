package com.neu.prattle.messaging;


/**
 * This enumeration represents the reserved characters that are used to represent special parsing in
 * messages
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/03/2019
 */
public enum ReservedCharacters {

  /**
   * Represents Seperators used for list
   */
  LIST_SEPARATORS(","),

  /**
   * USED to Determine special prefixes %PREFIX%
   */
  PREFIX_SYMBOL("%");

  /**
   * Variable to store string associate with enum
   */
  public final String label;

  /**
   * This constructor associates the string values to each enumeration
   */
  ReservedCharacters(String label) {
    this.label = label;
  }


}
