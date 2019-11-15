package com.neu.prattle.daos;

import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MessageDatabaseImplTest {

  private MessageDatabaseImpl messageDatabase = new MessageDatabaseImpl();
  private Message testMessage;
  private User testUser;

  @Before
  public void setUp() {
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

  @After
  public void tearDown() {
    testUser = null;
    testMessage = null;
    messageDatabase.truncateMessages();
  }

  @Test
  public void createMessageTest() {
    assertEquals(testMessage, messageDatabase.findMessage(testMessage));
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
    List<Message> bunchOMessages = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
    Message rightMessage = new Message();
    for (Message message : bunchOMessages) {
      if (message.equals(testMessage))
        messageDatabase.deleteMessage(message);
      rightMessage = message;
    }
    assertTrue(rightMessage.getContent().equals(testMessage.getContent()));
  }

  @Test
  public void deleteMessageTest() {
    List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
    for (Message message : testing) {
      messageDatabase.deleteMessage(message);
    }
    List<Message> testing2 = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
    assertEquals(0, testing2.size());
  }

  @Test
  public void findMessage() {
    Message beingTested = messageDatabase.findMessage(testMessage);
    assertTrue(beingTested.getContent().equalsIgnoreCase(testMessage.getContent()));
  }

  @Test
  public void truncateMessages() {
    messageDatabase.truncateMessages();
    assertTrue(messageDatabase.findAllMessagesOfUser(testUser).isEmpty());
  }
}