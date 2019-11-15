package com.neu.prattle;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * JDBC Database connection class for connecting to MySQL database.
 */
@SuppressWarnings("squid:S2068") // This is not an hard coded password.
public class DatabaseConnection {

  private static DatabaseConnection instance;
  private java.sql.Connection connection;
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

  private static final String USER = "team5";
  private static final String PASSWORD = "tautchat";
  private static final String SCHEMA = "tautdb";
  private static final String DB_HOSTNAME = "tautdb.c6y6bex5zmy8.us-east-2.rds.amazonaws.com";

  private static final String URL = String.format("jdbc:mysql://%s/%s", DB_HOSTNAME, SCHEMA);

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private DatabaseConnection() throws SQLException {
    try {
      Class.forName(DRIVER);
      this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException ex) {
      logging.log(Level.INFO, "Database Connection Creation Failed : " + ex.getMessage());
    }
  }


  /**
   * Gets connection to database.
   *
   * @return the connection
   */
  public java.sql.Connection getConnection() {
    return connection;
  }


  /**
   * Singleton implementation to avoid multiple connections to the db causing race conditions.
   *
   * @return the instance
   * @throws SQLException the sql exception
   */
  public static DatabaseConnection getInstance() throws SQLException {
    if (instance == null || instance.getConnection().isClosed()) {
      instance = new DatabaseConnection();
    }
    return instance;
  }
}
