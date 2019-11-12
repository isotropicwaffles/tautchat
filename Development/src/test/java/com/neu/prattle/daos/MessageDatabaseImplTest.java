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

	private UserDatabaseImpl userImplTest = new UserDatabaseImpl();

	@Before
	public void setUp() {
		testUser = new User();
		testUser.setName("Bender");
		testMessage = new Message();
		testMessage.setTo("HughMann");
		testMessage.setFrom(testUser.getName());
		testMessage.setDateSent(new Date());
		testMessage.setContent("This is a test");
		messageDatabase.createMessage(testMessage);
	}

	@After
	public void tearDown() {
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		for (Message message : testing) {
			messageDatabase.deleteMessage(message.getId());
		}
		testUser = null;
		testMessage = null;
	}

	@Test
	public void createMessageTest() {
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		for (Message message : testing) {
			assertTrue(message.getContent().equals(testMessage.getContent()));
		}
	}

	@Test
	public void findAllMessagesOfUserTest() {
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		for (Message message : testing) {
			assertTrue(message.getFrom().equals(testMessage.getFrom()));
		}
	}

	@Test
	public void findMessageByIdTest() {
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		int testId = 0;
		for (Message message : testing) {
			testId = message.getId();
		}
		Message rightMessage = messageDatabase.findMessageById(testId);
		assertTrue(rightMessage.getContent().equals(testMessage.getContent()));
	}

	@Test
	public void updateMessageTest() {
		testMessage.setContent("Updates have occurred");
		messageDatabase.updateMessage(testMessage.getId(), testMessage);
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		int testId = 0;
		for (Message message : testing) {
			testId = message.getId();
		}
		messageDatabase.updateMessage(testId, testMessage);
		Message rightMessage = messageDatabase.findMessageById(testId);
		assertTrue(rightMessage.getContent().equals(testMessage.getContent()));
	}

	@Test
	public void deleteMessageTest() {
		List<Message> testing = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		for (Message message : testing) {
			messageDatabase.deleteMessage(message.getId());
		}
		List<Message> testing2 = (List<Message>) messageDatabase.findAllMessagesOfUser(testUser);
		assertEquals(0, testing2.size());
	}

}