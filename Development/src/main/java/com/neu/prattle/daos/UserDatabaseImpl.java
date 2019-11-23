package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The JDBC implementation of User<-->database connection methods.
 */
public class UserDatabaseImpl extends AbstractJDBC implements UserDAO {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private static final String STATUS = "status";
  private static final String ISBOT = "is_bot";
  private static final String SEARCHABLE = "searchable";
  private static final String SELECTALLUSER = "SELECT * FROM `tautdb`.`users` WHERE `name`='";



  User returnUserQuery(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(string)) {
      try (ResultSet results = statement.executeQuery()) {
        if (results.next()) {
          int id = results.getInt("id");
          String name = results.getString("name");
          String status = results.getString(STATUS);
          boolean isBot = results.getBoolean(ISBOT);
          boolean searchable = results.getBoolean(SEARCHABLE);
          return User.userBuilder()
        		  .setName(name)
        		  .setBot(isBot)
        		  .setId(id)
        		  .setStatus(stringToUserStatus(status))
        		  .setSearchable(searchable)
        		  .build();
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Return User SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public void createUser(User user) {
      String createUserSQL = "INSERT INTO `tautdb`.`users` (`name`, `status`, `is_bot`, `searchable`)"
              + " VALUES ('" + user.getName() + "', '" + user.getStatus().toString() + "', "
              + user.userIsBot() + ", " + user.getSearchable() + ")";
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement preparedStatement = connection.
                 prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.executeUpdate();
      try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          user.setId((int) resultSet.getLong(1));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create User Query SQL blew up: " + e.toString());
    }
  }

  UserStatus stringToUserStatus(String string) {
    if (string.equalsIgnoreCase("donotdisturb")) {
      return UserStatus.DONOTDISTURB;
    } else if (string.equalsIgnoreCase("away")) {
      return UserStatus.AWAY;
    } else if (string.equalsIgnoreCase("idle")) {
      return UserStatus.IDLE;
    } else if (string.equalsIgnoreCase("offline")) {
      return UserStatus.OFFLINE;
    } else if (string.equalsIgnoreCase("online")) {
      return UserStatus.ONLINE;
    } else {
      return null;
    }
  }

  @Override
  public Collection<User> findAllUsers() {
    String findAllUsersSQL = "SELECT * FROM `tautdb`.`users`";
    ArrayList<User> users = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(findAllUsersSQL)) {
      try (ResultSet results = statement.executeQuery()) {
        while (results.next()) {
          int id = results.getInt("id");
          String name = results.getString("name");

          String status = results.getString(STATUS);
          boolean isBot = results.getBoolean(ISBOT);
          boolean searchable = results.getBoolean(SEARCHABLE);
          User user = new User.UserBuilder()
        		  .setName(name)
        		  .setBot(isBot)
        		  .setId(id)
        		  .setStatus(stringToUserStatus(status))
        		  .setSearchable(searchable)
        		  .build();
          users.add(user);
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Users SQL blew up: " + e.toString());
    }
    return users;
  }

  @Override
  public User findUserById(int userId) {
    String findAllUsersSQL = "SELECT * FROM `tautdb`.`users` WHERE `id`=" + userId;
    return returnUserQuery(findAllUsersSQL);
  }

  @Override
  public User findUserByUsername(String username) {
    String findAllUsersSQL = SELECTALLUSER + username + "'";
    return returnUserQuery(findAllUsersSQL);
  }

  @Override
  public boolean userExists(String username) {
    String userExistsSQL = SELECTALLUSER + username + "'";
    return executeBooleanQuery(userExistsSQL);
  }

  @Override
  public void updateUser(User user) {
    String updateMessageSQL = "UPDATE `tautdb`.`users` SET "
            + "`name`= '" + user.getName() + "', `status`= '" + user.getStatus().toString()
            + "', `is_bot` = " + user.userIsBot() + ", `searchable`=" + user.getSearchable()
            + " WHERE `id`=" + user.getId();
    executeUpdateHelper(updateMessageSQL);
  }

  @Override
  public void deleteUserById(int userId) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users` WHERE `id`=" + userId;
    executeUpdateHelper(deleteMessageSQL);
  }

  @Override
  public void deleteUserByUsername(String username) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users` WHERE `name`='" + username + "'";
    executeUpdateHelper(deleteMessageSQL);
  }

  @Override
  public void deleteAllUsers() {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users`";
    executeUpdateHelper(deleteMessageSQL);
  }

  @Override
  public boolean isBot(User user) {
    String userIsBotSQL = SELECTALLUSER + user.getName()
            + "' AND `is_bot` = 1";
    return executeBooleanQuery(userIsBotSQL);
  }

  @Override
  public boolean isSearchable(User user) {
    String userIsSearchableSQL = SELECTALLUSER + user.getName()
            + "' AND `searchable` = 1";
    return executeBooleanQuery(userIsSearchableSQL);
  }

  @Override
  public UserStatus retrieveStatus(User user) {
    String userStatusSQL = "SELECT `users`.`status` FROM `tautdb`.`users` WHERE `name`='"
            + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(userStatusSQL)) {
      try (ResultSet results = statement.executeQuery()) {
        if (results.next()) {
          String status = results.getString(STATUS);
          return stringToUserStatus(status);
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Retrieve status SQL blew up: " + e.toString());
    }
    return null;
  }
}