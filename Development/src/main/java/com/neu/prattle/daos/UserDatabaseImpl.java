package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import java.sql.Connection;
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
public class UserDatabaseImpl implements UserDAO {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void createUser(User user) {
    String createUserSQL = "INSERT INTO `tautdb`.`users` (`name`, `status`, `is_bot`, `searchable`)"
            + " VALUES ('" + user.getName() + "', '" + user.getStatus().toString() + "', "
            + user.userIsBot() + ", " + user.getSearchable() + ")";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createUserSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create User SQL blew up: " + e.toString());
    }
  }

  private UserStatus stringToUserStatus(String string) {
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
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllUsersSQL)) {
        while (results.next()) {
          int id = results.getInt("id");
          String name = results.getString("name");
          String status = results.getString("status");
          boolean isBot = results.getBoolean("is_bot");
          boolean searchable = results.getBoolean("searchable");
          User user = new User(name, isBot);
          user.setId(id);
          user.setStatus(stringToUserStatus(status));
          user.setSearchable(searchable);
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

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllUsersSQL)) {
        while (results.next()) {
          int id = results.getInt("id");
          String name = results.getString("name");
          String status = results.getString("status");
          boolean isBot = results.getBoolean("is_bot");
          boolean searchable = results.getBoolean("searchable");
          User user = new User(name, isBot);
          user.setId(id);
          user.setStatus(stringToUserStatus(status));
          user.setSearchable(searchable);
          return user;
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find User by ID SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public User findUserByUsername(String username) {
    String findAllUsersSQL = "SELECT * FROM `tautdb`.`users` WHERE `name`='" + username + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllUsersSQL)) {
        while (results.next()) {
          int id = results.getInt("id");
          String name = results.getString("name");
          String status = results.getString("status");
          boolean isBot = results.getBoolean("is_bot");
          boolean searchable = results.getBoolean("searchable");
          User user = new User(name, isBot);
          user.setId(id);
          user.setStatus(stringToUserStatus(status));
          user.setSearchable(searchable);
          return user;
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find User by Username SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public boolean userExists(String username) {
    String userExistsSQL = "SELECT * FROM `tautdb`.`users` WHERE `name`='" + username + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(userExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does User exist SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public int updateUser(User user) {
    String updateMessageSQL = "UPDATE `tautdb`.`users` SET "
            + "`name`= '" + user.getName() + "', `status`= '" + user.getStatus().toString()
            + "', `is_bot` = " + user.userIsBot() + ", `searchable`=" + user.getSearchable()
            + " WHERE `id`=" + user.getId();
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(updateMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update User SQL blew up: " + e.toString());
    }
    return 0;
  }

  @Override
  public int deleteUserById(int userId) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users` WHERE `id`=" + userId;
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete User by ID SQL blew up: " + e.toString());
    }
    return 0;
  }

  @Override
  public int deleteUserByUsername(String username) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users` WHERE `name`='" + username + "'";
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete User by username SQL blew up: " + e.toString());
    }
    return 0;
  }

  @Override
  public void deleteAllUsers() {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`users`";
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete All Users SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean isBot(User user) {
    String userIsBotSQL = "SELECT * FROM `tautdb`.`users` WHERE `name`='" + user.getName()
            + "' AND `is_bot` = 1";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(userIsBotSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Is User a bot SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public boolean isSearchable(User user) {
    String userIsSearchableSQL = "SELECT * FROM `tautdb`.`users` WHERE `name`='" + user.getName()
            + "' AND `searchable` = 1";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(userIsSearchableSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Is User searchable SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public UserStatus retrieveStatus(User user) {
    String userStatusSQL = "SELECT `users`.`status` FROM `tautdb`.`users` WHERE `name`='"
            + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(userStatusSQL)) {
        if (results.next()) {
          String status = results.getString("status");
          return stringToUserStatus(status);
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Retrieve status SQL blew up: " + e.toString());
    }
    return null;
  }
}