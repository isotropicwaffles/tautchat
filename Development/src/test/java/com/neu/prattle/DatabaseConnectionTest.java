package com.neu.prattle;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Test
  public void getInstance() {
    DatabaseConnection databaseConnection;
    try {
      Connection connection = DatabaseConnection.getInstance().getConnection();
      assertNotNull(connection);
    } catch (SQLException e) {
      logging.log(Level.INFO, "DB connection Test fail" + e.toString());
    }
  }
}