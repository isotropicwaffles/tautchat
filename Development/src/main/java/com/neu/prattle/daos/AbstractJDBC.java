package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

abstract class AbstractJDBC {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  void executeUpdateHelper(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(string)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      logging.log(Level.INFO, "Execute Update SQL blew up: " + e.toString());
    }
  }

  boolean executeBooleanQuery(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(string)) {
      try (ResultSet results = statement.executeQuery()) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Execute Boolean Query SQL blew up: " + e.toString());
    }
    return false;
  }
}
