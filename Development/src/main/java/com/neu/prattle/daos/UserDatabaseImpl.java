package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.User;

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
    String createUserSQL = "INSERT INTO `tautdb`.`users` (`name`) VALUES ('"
            + user.getName() + "');";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createUserSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create User SQL blew up: " + e.toString());
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
          User user = new User();
          user.setIdNumber(id);
          user.setName(name);
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
          User user = new User();
          user.setIdNumber(id);
          user.setName(name);
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
          User user = new User();
          user.setIdNumber(id);
          user.setName(name);
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
    String findAllUsersSQL = "SELECT * FROM `tautdb`.`users` WHERE `name`='" + username + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllUsersSQL)) {
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
            + "`name`= '" + user.getName()
            + "' WHERE `id`=" + user.getIdNumber();
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
}