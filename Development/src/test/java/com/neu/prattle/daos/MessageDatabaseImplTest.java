package com.neu.prattle.daos;

import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import org.hsqldb.cmdline.SqlToolError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

public class MessageDatabaseImplTest {

  private static MessageDatabaseImpl messageDatabase = new MessageDatabaseImpl();
  private static Message testMessage;
  private static User testUser;

  @BeforeClass
  public static void setUp() throws ClassNotFoundException, SqlToolError, SQLException, IOException {
	DatabaseSupportFunctions.setUpTestDatabase();

    testUser = new User.UserBuilder()
            .setName("Bender")
            .build();

    testMessage = Message.messageBuilder()
            .setMessageContent("This is a test")
            .setFrom(testUser.getName())
            .setTo("HughMann")
            .setDateSent(new Date())
            .build();

    messageDatabase.createMessage(testMessage);
  }

  @AfterClass
  public static void tearDown() throws SQLException {
    testUser = null;
    testMessage = null;
    messageDatabase.truncateMessages();
	DatabaseSupportFunctions.tearDownTestDatabase();

  }

  @Test
  public void findAllMessagesOfUserTest() {
    List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
    for (Message message : testing) {
      assertTrue(message.getFrom().equals(testMessage.getFrom()));
    }
  }

  @Test
  public void updateMessageTest() {
    testMessage.setContent("Updates have occurred");
    messageDatabase.updateMessage(testMessage);
    Message rightMessage = messageDatabase.findMessage(testMessage);
    assertTrue(rightMessage.getContent().equals(testMessage.getContent()));
  }

  @Test
  public void deleteMessageTest() {
    messageDatabase.deleteMessage(testMessage);
    assertNull(messageDatabase.findMessage(testMessage));
  }

  @Test
  public void truncateMessages() {
    messageDatabase.truncateMessages();
    assertTrue(messageDatabase.findAllMessagesOfUser(testUser).isEmpty());
  }
}