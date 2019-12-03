package com.neu.prattle;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * JDBC Database connection class for connecting to MySQL database.
 */
@SuppressWarnings("squid:S2068") // This is not a hard coded password.
public class DatabaseConnection {

  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String USER = "team5";
  private static final String PASSWORD = "tautchat";
  private static final String SCHEMA = "tautdb";
  private static final String DB_HOSTNAME = "tautdb.c6y6bex5zmy8.us-east-2.rds.amazonaws.com";
  private static final String URL = String.format("jdbc:mysql://%s/%s", DB_HOSTNAME, SCHEMA);
  private static final String TEST_URL = String.format("jdbc:hsqldb:mem:%s",SCHEMA);
  private static boolean testMode = false;
  private static DatabaseConnection instance;
  private java.sql.Connection connection;
  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private DatabaseConnection() throws SQLException {
    try {
    	if (testMode) {
          	this.connection = DriverManager.getConnection(TEST_URL, USER, PASSWORD);
    	}else {
    		Class.forName(DRIVER);
          	this.connection = DriverManager.getConnection(URL, USER, PASSWORD);	
    	}

    } catch (ClassNotFoundException ex) {
      logging.log(Level.INFO, "Database Connection Creation Failed : " + ex.getMessage());
    }
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
  
  
  /**
   * Reformats statements for testing mode
   *
   * @param statement - an sql statement
   * @return reformatted statement if the mode is set to testing mode
   */
  public static String formatStatement(String statement) {
	
	StringBuilder formattedStatement = new StringBuilder();
		
	for (int i = 0; i < statement.length(); i++){

		if(statement.charAt(i) != '`' ||  !testMode)// apostrophe
		{
			formattedStatement.append(statement.charAt(i));	
		}
	}			

	return formattedStatement.toString();
	  
  }
  
  /**
   * Enable/Disable testing mode for database 
   * 
   * @param enable - true enables test mode, false disables test mode
   * 
   */
  public static void enableDBTestMode(boolean enable){
	  
	  instance = null;
	  testMode = enable;
  }
 


  /**
   * Gets connection to database.
   *
   * @return the connection
   */
  public java.sql.Connection getConnection() {
    return connection;
  }
}
