package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The JDBC implementation of Message<-->database connection methods.
 */
public class MessageDatabaseImpl implements MessageDAO {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  @Override
  public void createMessage(Message message) {
    String createMessageSQL = "INSERT INTO `tautdb`.`messages` (`content`, `sender_username`, "
            + "`recipient_username`, `date_sent`) VALUES ('contentPH', 'senderIdPH', 'recipientIdPH'"
            + ", 'datePH')";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      String sql = createMessageSQL.replace("contentPH", message.getContent());
      sql = sql.replace("senderIdPH", message.getFrom());
      sql = sql.replace("recipientIdPH", message.getTo());
      sql = sql.replace("datePH", simpleDateFormat.format(new Date()));
      int newId = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
      message.setId(newId);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create Message SQL blew up: " + e.toString());
    }
  }

  @Override
  public Collection<Message> findAllMessagesOfUser(User user) {
    String findAllUserMessagesSQL = "SELECT * FROM `tautdb`.`messages` "
            + "WHERE `messages`.`sender_username`='" + user.getName()
            + "' OR `messages`.`recipient_username`='" + user.getName() + "'";
    ArrayList<Message> messages = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllUserMessagesSQL)) {
        while (results.next()) {
          int id = results.getInt("id");
          String content = results.getString("content");
          String from = results.getString("sender_username");
          String to = results.getString("recipient_username");
          Date sendDate = results.getDate("date_sent");
          Message message = new Message();
          message.setId(id);
          message.setContent(content);
          message.setFrom(from);
          message.setTo(to);
          message.setDateSent(sendDate);
          messages.add(message);
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Messages SQL blew up: " + e.toString());
    }
    return messages;
  }

  @Override
  public Message findMessageById(int messageId) {
    String findMessageByIdSQL = "SELECT * FROM `tautdb`.`messages` WHERE `id` =" + messageId;

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(findMessageByIdSQL)) {
        if (resultSet.next()) {
          int id = resultSet.getInt("id");
          String content = resultSet.getString("content");
          String from = resultSet.getString("sender_username");
          String to = resultSet.getString("recipient_username");
          Date sendDate = resultSet.getDate("date_sent");
          Message message = new Message();
          message.setId(id);
          message.setContent(content);
          message.setFrom(from);
          message.setTo(to);
          message.setDateSent(sendDate);
          return message;
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find Message by ID SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public int updateMessage(int messageId, Message message) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    String updateMessageSQL = "UPDATE `tautdb`.`messages` SET "
            + "`content`= '" + message.getContent()
            + "', `sender_username`= '" + message.getFrom()
            + "', `recipient_username`= '" + message.getTo()
            + "', `date_sent`= '" + simpleDateFormat.format(message.getDateSent())
            + "' WHERE `id`=" + messageId;

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(updateMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update Message SQL blew up: " + e.toString());
    }
    return 0;
  }

  @Override
  public int deleteMessage(int messageId) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`messages` WHERE `id`=" + messageId;
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteMessageSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete Message SQL blew up: " + e.toString());
    }
    return 0;
  }
}