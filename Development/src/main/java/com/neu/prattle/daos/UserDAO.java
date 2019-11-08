package com.neu.prattle.daos;

import com.neu.prattle.model.User;

import java.util.Collection;

/**
 * User Data Access Object for connecting to MySQL server.
 */
public interface UserDAO {

  /**
   * Create user record in database.
   *
   * @param user the user
   */
  void createUser(User user);

  /**
   * Find all users saved in the db.
   *
   * @return the collection
   */
  Collection<User> findAllUsers();

  /**
   * Find user by id user.
   *
   * @param userId the user id
   * @return the user
   */
  User findUserById(int userId);

  /**
   * Find user by username user.
   *
   * @param username the username
   * @return the user
   */
  User findUserByUsername(String username);

  /**
   * Determine if User exists boolean.
   *
   * @param username the username
   * @return the boolean
   */
  boolean userExists(String username);

  /**
   * Update user data.
   *
   * @param user   the user
   * @return 0 denoting completion of method
   */
  int updateUser(User user);

  /**
   * DO EITHER OF THESE DELETES NEED TO EXIST Delete user by id.
   *
   * @param userId the user id
   * @return 0 denoting completion of method
   */
  int deleteUserById(int userId);

  /**
   * Deletes user by Username.
   *
   * @param username identifying name of User
   * @return 0 denoting completion of method
   */
  int deleteUserByUsername(String username);

  /**
   *
   */
  void deleteAllUsers();

}
