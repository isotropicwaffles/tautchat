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

  private void executeUpdateQueryHelper(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(string);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update Message Query SQL blew up: " + e.toString());
    }
  }

  @Override
  public void createMessage(Message message) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String createMessageSQL = "INSERT INTO `tautdb`.`messages` (`content`, `sender_username`, "
            + "`recipient_username`, `date_sent`) VALUES ('" + message.getContent() + "', '"
            + message.getFrom() + "', '" + message.getTo() + "', '"
            + simpleDateFormat.format(message.getDateSent()) +"')";
    executeUpdateQueryHelper(createMessageSQL);
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
          
          Message message = Message.messageBuilder()
                  .setId(id)
                  .setMessageContent(content)
                  .setFrom(from)
                  .setTo(to)
                  .setDateSent(sendDate)
                  .build();
          
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
          return Message.messageBuilder()
                  .setId(id)
                  .setMessageContent(content)
                  .setFrom(from)
                  .setTo(to)
                  .setDateSent(sendDate)
                  .build();

        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find Message by ID SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public void updateMessage(int messageId, Message message) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String updateMessageSQL = "UPDATE `tautdb`.`messages` SET "
            + "`content`= '" + message.getContent()
            + "', `sender_username`= '" + message.getFrom()
            + "', `recipient_username`= '" + message.getTo()
            + "', `date_sent`= '" + simpleDateFormat.format(message.getDateSent())
            + "' WHERE `id`=" + messageId;
    executeUpdateQueryHelper(updateMessageSQL);
  }

  @Override
  public void deleteMessage(int messageId) {
    String deleteMessageSQL = "DELETE FROM `tautdb`.`messages` WHERE `id`=" + messageId;
    executeUpdateQueryHelper(deleteMessageSQL);
  }
}