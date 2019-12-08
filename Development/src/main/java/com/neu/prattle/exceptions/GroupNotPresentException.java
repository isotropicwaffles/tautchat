package com.neu.prattle.exceptions;

import com.neu.prattle.model.Group;

/***
 * An representation of an error which is thrown where a request has been made
 * for to find a group object that doesn't exist in the system.
 *
 * @author Richard Alexander Showalter-Bucher
 * @version dated 2019-11-11
 */
public class GroupNotPresentException extends RuntimeException {
  /**
   *
   */
  private static final long serialVersionUID = -4845176561270017896L;

  public GroupNotPresentException(String message) {
    super(message);
  }
}
