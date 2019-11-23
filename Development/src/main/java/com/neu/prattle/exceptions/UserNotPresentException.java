package com.neu.prattle.exceptions;

import com.neu.prattle.model.User;

/***
 * An representation of an error which is thrown where a request has been made
 * for to find of a user object that doesn't exist in the system.
 * Refer {@link com.neu.prattle.model.User#equals}
 * Refer {@link com.neu.prattle.service.UserService#addUser(User)}
 *
 * @author Richard Alexander Showalter-Bucher
 * @version dated 2019-11-11
 */
public class UserNotPresentException extends RuntimeException {
  /**
   *
   */
  private static final long serialVersionUID = -4845176561270017896L;

  public UserNotPresentException(String message) {
    super(message);
  }
}
