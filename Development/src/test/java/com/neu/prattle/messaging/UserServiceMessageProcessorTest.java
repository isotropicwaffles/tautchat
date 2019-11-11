package com.neu.prattle.messaging;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.neu.prattle.websocket.ChatEndpoint;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;
import com.neu.prattle.service.user.UserServiceCommands;
import com.neu.prattle.service.user.UserServiceImpl;

import javax.websocket.EncodeException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceMessageProcessorTest {

	ChatEndpoint chatEndpoint1;
	ChatEndpoint chatEndpoint2;
	ChatEndpoint chatEndpoint3;


	Session session1;
	Session session2;
	Session session3;

	final String userName1 = "mockUser1";
	final String userName2 = "mockUser2";
	final String userName3 = "mockUser3";


	final String sessionID1 = "5";
	final String sessionID2 = "6";
	final String sessionID3 = "7";


	// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
	// to our mock object.
	ArgumentCaptor<Message> messageArgumentCaptor1;
	ArgumentCaptor<Message> messageArgumentCaptor2;
	ArgumentCaptor<Message> messageArgumentCaptor3;


	/***
	 * Called up each test before invocation.
	 * @throws EncodeException 
	 * @throws IOException 
	 */
	@Before
	public void setUp() throws IOException, EncodeException {
		// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
		// to our mock object.
		messageArgumentCaptor1 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor2 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor3 = ArgumentCaptor.forClass(Message.class);

		chatEndpoint1 = new ChatEndpoint();
		chatEndpoint2 = new ChatEndpoint();
		chatEndpoint3 = new ChatEndpoint();

		session1 = generateMockSession(messageArgumentCaptor1, sessionID1);
		session2 = generateMockSession(messageArgumentCaptor2, sessionID2);
		session3 = generateMockSession(messageArgumentCaptor3, sessionID3);

		chatEndpoint1.onOpen(session1, userName1);
		chatEndpoint2.onOpen(session2, userName2);
		chatEndpoint3.onOpen(session3, userName3);


	}


	/***
	 * Called up each test after completed.
	 * 
	 */
	@After
	public void destroy() {
		UserServiceImpl.clear();
		chatEndpoint1.onClose(session1);
		chatEndpoint2.onClose(session2);
		chatEndpoint3.onClose(session2);

		messageArgumentCaptor1 = null;
		messageArgumentCaptor2 = null;
		messageArgumentCaptor3 = null;

		chatEndpoint1 = null;
		chatEndpoint2 = null;
		chatEndpoint3 = null;

		session1 = null;
		session2 = null;
		session3 = null;


	}



	/***
	 * Test failed login message processing
	 * @throws IOException 
	 * @throws TimeoutException 
	 * 
	 */
	@Test
	public void failedLoginMessageTest() throws IOException, TimeoutException{
		
		Message loginRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.LOGIN.label)
				.setMessageContent(userName2)
				.build();
	
		chatEndpoint2.onMessage(session2, loginRequest);
		
		float timeOut = 1; //seconds
		int messageCount = 1; // message counter to stop waiting
		waitForMessage(messageArgumentCaptor2, messageCount,  timeOut);

		Message message = messageArgumentCaptor2.getAllValues().get(0);

		assertEquals(MessageAddresses.USER_SERVICE.label, message.getFrom());
		assertEquals(UserServiceCommands.LOGIN.label, message.getContentType());
		assertEquals(GenericMessageResponses.FAILURE_RESPONSE.label, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());

	}

	
	/***
	 * Test create user and successful login message processing
	 * @throws IOException 
	 * @throws TimeoutException 
	 * 
	 */
	@Test
	public void createUserAndSuccessfulLoginMessageTest() throws IOException, TimeoutException{
		
		// create User
		Message createUserRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.USER_CREATE.label)
				.setMessageContent(userName1)
				.build();
		
		chatEndpoint1.onMessage(session1, createUserRequest);

		float timeOut = 1; //seconds
		int messageCount = 1; // message counter to stop waiting
		waitForMessage(messageArgumentCaptor1, messageCount,  timeOut);

		Message message = messageArgumentCaptor1.getAllValues().get(0);

		assertEquals(MessageAddresses.USER_SERVICE.label, message.getFrom());
		assertEquals(UserServiceCommands.USER_CREATE.label, message.getContentType());
		assertEquals(GenericMessageResponses.SUCCESS_RESPONSE.label, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());
		
		// Successful Login
		Message loginRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.LOGIN.label)
				.setMessageContent(userName1)
				.build();
	
		chatEndpoint1.onMessage(session1, loginRequest);

		timeOut = 1; //seconds
		messageCount = 2; // message counter to stop waiting
		waitForMessage(messageArgumentCaptor1, messageCount,  timeOut);

		message = messageArgumentCaptor1.getAllValues().get(1);

		assertEquals(MessageAddresses.USER_SERVICE.label, message.getFrom());
		assertEquals(UserServiceCommands.LOGIN.label, message.getContentType());
		assertEquals(GenericMessageResponses.SUCCESS_RESPONSE.label, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());
		


	}
	
	
	/***
	 * Test failed user creation
	 * @throws IOException 
	 * @throws TimeoutException 
	 * 
	 */
	@Test
	public void createUserFailureMessageTest() throws IOException, TimeoutException{
		
		// create User
		Message createUserRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.USER_CREATE.label)
				.setMessageContent(userName3)
				.build();
		
		chatEndpoint3.onMessage(session3, createUserRequest);

		float timeOut = 1; //seconds
		int messageCount = 1; // message counter to stop waiting
		waitForMessage(messageArgumentCaptor3, messageCount,  timeOut);

		Message message = messageArgumentCaptor3.getAllValues().get(0);

		assertEquals(MessageAddresses.USER_SERVICE.label, message.getFrom());
		assertEquals(UserServiceCommands.USER_CREATE.label, message.getContentType());
		assertEquals(GenericMessageResponses.SUCCESS_RESPONSE.label, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());
		

		
		chatEndpoint3.onMessage(session3, createUserRequest);

		messageCount = 2; // message counter to stop waiting
		waitForMessage(messageArgumentCaptor3, messageCount,  timeOut);

		message = messageArgumentCaptor3.getAllValues().get(1);

		assertEquals(MessageAddresses.USER_SERVICE.label, message.getFrom());
		assertEquals(UserServiceCommands.USER_CREATE.label, message.getContentType());
		assertEquals(GenericMessageResponses.FAILURE_RESPONSE.label, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());
		

	}
	
	/***
	 * Waits for the message Arguement counter to reaech given count message
	 * 
	 * @param messageArgumentCaptor - the Arguement Capture to check
	 * @param messageCount - the message counter to stop waiting
	 * @param timeOut - a time out if getting messages takes to long, given in seconds
	 * @throws TimeoutException  - if timed out
	 */
	private void waitForMessage(ArgumentCaptor<Message> messageArgumentCaptor, int messageCount, float timeOut) throws TimeoutException {
		long startTime = System.currentTimeMillis();

		while(messageArgumentCaptor.getAllValues().size()< messageCount) {
			if ( (System.currentTimeMillis() - startTime) > timeOut*1000) {
				throw new TimeoutException("Timed out waiting for message");
			}
		}
	}



	/**
	 * Generates a mock session to for testing 
	 * 
	 * @param messageArgumentCaptor - a message arguement capture
	 * @param sessionID - an id to give for the session
	 * @return session - the mock session
	 * @throws IOException
	 * @throws EncodeException
	 */
	private Session generateMockSession(ArgumentCaptor<Message> messageArgumentCaptor, String sessionID)
			throws IOException, EncodeException {
		RemoteEndpoint.Basic remoteEndpoint = mock(RemoteEndpoint.Basic.class);
		if (messageArgumentCaptor != null) {
			doNothing().when(remoteEndpoint).sendObject(messageArgumentCaptor.capture());
		}
		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getBasicRemote()).thenReturn(remoteEndpoint);
		Mockito.when(session.getId()).thenReturn(sessionID);
		return session;
	}



}
