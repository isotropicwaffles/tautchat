package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

public class DatabaseSupportFunctions {

	private static String sqlInitFile;

    /**
     * Database initialization for testing i.e.
     * <ul>
     * <li>Creating Table</li>
     * <li>Inserting record</li>
     * </ul>
     * 
     * @throws SQLException
     * @throws SqlToolError 
     * @throws IOException 
     */
    private static void initDatabase() throws SQLException, SqlToolError, IOException {
		DatabaseConnection.enableDBTestMode(true);
		
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
        		Statement statement = connection.createStatement();) {
        	
             // execute the commands in the .sql file
             SqlFile sf = new SqlFile(new File(sqlInitFile));
             sf.setConnection(connection);
             sf.execute();

             connection.commit();
        	
         }
    }
 
	public static void setUpTestDatabase(String sqlInitFileForTest) throws ClassNotFoundException, SQLException, SqlToolError, IOException {
		sqlInitFile = sqlInitFileForTest;
        // initialize database
        initDatabase();

	}
	
	public static void tearDownTestDatabase() throws SQLException {
		DatabaseConnection.enableDBTestMode(false);

	}
}