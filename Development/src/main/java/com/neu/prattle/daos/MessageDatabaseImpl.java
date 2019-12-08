package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * The JDBC implementation of Message--database connection methods.
 */
public class MessageDatabaseImpl implements MessageDAO {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private void executeUpdateQueryHelper(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(string)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update Message Query SQL blew up: " + e.toString());
    }
  }

  @Override
  public void createMessage(Message message) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String createMessageSQL = DatabaseConnection.formatStatement("INSERT INTO `tautdb`.`messages` (`content`, `sender_username`, "
            + "`recipient_username`, `date_sent`) VALUES ('" + message.getContent() + "', '"
            + message.getFrom() + "', '" + message.getTo() + "', '"
            + simpleDateFormat.format(message.getDateSent()) + "')");
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement preparedStatement = connection.
                 prepareStatement(createMessageSQL, Statement.RETURN_GENERATED_KEYS)) {
      preparedStatement.executeUpdate();
      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          message.setId((int) resultSet.getLong(1));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create Message Query SQL blew up: " + e.toString());
    }
  }

  @Override
  public Collection<Message> findAllMessagesOfUser(User user) {
    String findAllUserMessagesSQL = DatabaseConnection.formatStatement("SELECT * FROM `tautdb`.`messages` "
            + "WHERE `messages`.`sender_username`='" + user.getName()
            + "' OR `messages`.`recipient_username`='" + user.getName() + "'");
    ArrayList<Message> messages = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(findAllUserMessagesSQL)) {
      try (ResultSet results = statement.executeQuery()) {
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
                  .setType("direct message")
                  .setContentType("text")
                  .build();

          messages.add(message);

        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Messages SQL blew up: " + e.toString());
    }
    return messages;
  }

  Message findMessage(Message message) {

    String findMessageSQL = DatabaseConnection.formatStatement("SELECT * FROM `tautdb`.`messages` WHERE  `id`=" + message.getId());
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(findMessageSQL)) {
      try (ResultSet results = statement.executeQuery()) {
        if (results.next()) {
          int id = results.getInt("id");
          String content = results.getString("content");
          String from = results.getString("sender_username");
          String to = results.getString("recipient_username");
          Date sendDate = results.getDate("date_sent");

          return Message.messageBuilder()
                  .setId(id)
                  .setMessageContent(content)
                  .setFrom(from)
                  .setTo(to)
                  .setDateSent(sendDate)
                  .setType("filler")
                  .setContentType("filler")
                  .build();
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find Message SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public void updateMessage(Message message) {
    String updateMessageSQL = DatabaseConnection.formatStatement("UPDATE `tautdb`.`messages` SET "
            + "`content`= '" + message.getContent()
            + "' WHERE  `id`= " + message.getId());
    executeUpdateQueryHelper(updateMessageSQL);
  }

  @Override
  public void deleteMessage(Message message) {
    String deleteMessageSQL = DatabaseConnection.formatStatement("DELETE FROM `tautdb`.`messages` WHERE  `id`= " + message.getId());
    executeUpdateQueryHelper(deleteMessageSQL);
  }

  void truncateMessages() {
    String truncateMessagesSQL = DatabaseConnection.formatStatement("DELETE FROM `tautdb`.`messages`");
    executeUpdateQueryHelper(truncateMessagesSQL);
  }
}