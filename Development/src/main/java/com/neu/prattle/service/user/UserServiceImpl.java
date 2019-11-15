package com.neu.prattle.service.user;

import com.neu.prattle.daos.UserDatabaseImpl;
import com.neu.prattle.exceptions.GroupNotPresentException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/***
 * Implementation of {@link UserService}
 *
 * It stores the user accounts in-memory, which means any user accounts
 * created will be deleted once the application has been restarted.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class UserServiceImpl implements UserService {

  /**
   * Database instance for persistence.
   */
  private static UserDatabaseImpl userDatabase = new UserDatabaseImpl();

  /***
   * UserServiceImpl is a Singleton class.
   */
  private UserServiceImpl() {

  }

  /***
   * Variable to store the singleton instance
   *
   */
  private static UserService accountService = null;


  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static UserService getInstance() {
    
    if (accountService == null) {
      userSet = new HashSet<>();
      userSet.addAll(userDatabase.findAllUsers());
      accountService = new UserServiceImpl();
    }
    return accountService;
  }


  /**
   * Set of user objects
   */
  private static Set<User> userSet;

  /***
   *
   * @param name -> The name of the user.
   * @return An optional wrapper supplying the user.
   */
  @Override
  public Optional<User> findUserByName(String name) {
    final User user = new User(name);
    if (userSet.contains(user)) {
      return Optional.of(user);
    } else {
      return Optional.empty();
    }
  }

  /***
	 * Queries partial name and returns all searchable users that names partially match
	 *
	 *
	 * @param partialName -> The name of the user.
	 * @return Set of users with matching parital name.
	 */
	@Override
	public Set<User> findUserByPartialName(String partialName) {

		Set<User> partialMatches = new LinkedHashSet<>();
		for(User user : userSet) {

			if(user.getName().toLowerCase().contains(partialName.toLowerCase()) &&
					user.getSearchable()) {
				partialMatches.add(user);
			}
		}

		return partialMatches;
	}

  /***
   * Attempts to return the user associated with the name and throws and error if the user doesn't exist
   *
   * @param name -> The name of the user.
   * @return The associated user.
   * @throws GroupNotPresentException if user does not exist
   */
  @Override
  public User protectedfindUserByName(String name) {

    Optional<User> potentialGroup = findUserByName(name);

    if (potentialGroup.isPresent()) {
      return potentialGroup.get();
    } else {

      throw new GroupNotPresentException(String.format("User %s could not be found", name));
    }
  }


  /**
   * Adds users to Service
   *
   * @param user : user to add
   */
  @Override
  public synchronized void addUser(User user) {
    if (userSet.contains(user))
      throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getName()));

    userSet.add(user);
    user.setSearchable(true);
    user.setStatus(UserStatus.IDLE);
    if (!userDatabase.userExists(user.getName())) {
      userDatabase.createUser(user);
    }
  }

  /**
   * Call this method to clear the current instance of this service.
   */
  public static void clear() {
    accountService = null;
    userSet = null;
  }
  
  

  /**
   * Call this method to clear the current set of users stored
   */
  public static void clearAllUsers() {
    userSet = new HashSet<>();
  }



}
