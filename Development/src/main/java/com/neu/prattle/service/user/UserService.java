package com.neu.prattle.service.user;

import com.neu.prattle.messaging.ReservedCharacters;
import com.neu.prattle.model.User;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on user accounts.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 *
 */
public interface UserService {


  /***
   * Returns a string representation of a list of users
   * @param users - generates string list of users
   * @return string list of user objects names.
   */
  static String generateUserList(Set<User> users) {

    StringJoiner list = new StringJoiner(ReservedCharacters.LIST_SEPARATORS.label);

    for (User user : users) {
      list.add(user.getName());
    }

    return list.toString();
  }

  /***
   * Returns an optional object which might be empty or wraps an object
   * if the System contains a {@link User} object having the same name
   * as the parameter.
   *
   * @param name The name of the user
   * @return Optional object.
   */
  Optional<User> findUserByName(String name);

  /***
   * Returns a list of users that partially match the given nameobject having the same name
   * as the parameter.
   *
   * @param partialName The partial name of the user
   * @return List which is empty if there are no partial matches
   */
  Set<User> findUserByPartialName(String partialName);

  /***
   * Attempts to return the user associated with the name and throws and error if the user doesn't exist
   *
   * @param name - The name of the user.
   * @return The associated user.
   */
  User protectedfindUserByName(String name);

  /***
   * Tries to add a user in the system
   * @param user User object
   * @throws IOException when fileWriter blows up.
   *
   */
  void addUser(User user) throws IOException;


}
