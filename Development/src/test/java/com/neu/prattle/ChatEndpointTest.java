package com.neu.prattle;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.main.PrattleApplication;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class ChatEndpointTest {



	// Mock instance of userService
	private UserService userService;

	ChatEndpoint chatEndpoint1;
	ChatEndpoint chatEndpoint2;

	Session session1;
	Session session2;
	final String userName1 = "mockUser1";
	final String userName2 = "mockUser2";

	final String sessionID1 = "5";
	final String sessionID2 = "6";


	// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
	// to our mock object.
	ArgumentCaptor<Message> messageArgumentCaptor1;
	ArgumentCaptor<Message> messageArgumentCaptor2;

	/***
	 * Called up each test before invocation.
	 * @throws EncodeException 
	 * @throws IOException 
	 */
	@Before
	public void setUp() throws IOException, EncodeException {
		userService = mock(UserService.class);


		// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
		// to our mock object.
		messageArgumentCaptor1 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor2 = ArgumentCaptor.forClass(Message.class);

		chatEndpoint1 = new ChatEndpoint();
		chatEndpoint2 = new ChatEndpoint();

		session1 = generateMockSession(messageArgumentCaptor1, sessionID1);
		session2 = generateMockSession(messageArgumentCaptor2, sessionID2);

		chatEndpoint1.onOpen(session1, userName1);
		chatEndpoint2.onOpen(session2, userName2);

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
		messageArgumentCaptor1 = null;
		messageArgumentCaptor2 = null;
		chatEndpoint1 = null;
		chatEndpoint2 = null;
		session1 = null;
		session2 = null;
	}

	/***
	 *  This method tests getClasses function of PrattleApplication
	 * 
	 */
	@Test
	public void prattleAppGetClassesTest(){
		PrattleApplication prattleApp = new PrattleApplication();
		Set<Class<?>> resourceClasses = prattleApp.getClasses();

		assertEquals(1,resourceClasses.size());
		assertEquals(UserController.class, resourceClasses.iterator().next());

	}

	/***
	 *  This methods tests the onOpen function
	 * 
	 */
	@Test
	public void endPointChatOnOpenTest() {

		//verifiy ID was call twice
		Mockito.verify(session1, Mockito.times(2)).getId();

		Mockito.verify(session2, Mockito.times(2)).getId();

	}

	/***
	 *  This methods tests the broadcast message function
	 * @throws IOException 
	 * 
	 */
	@Test
	public void endPointChatBroadcastTest() throws IOException{



		String testMessage = "Test Message";
		Message messageToSend = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.BROADCAST_MESSAGE.label)
				.setMessageContent(testMessage)
				.build();

		chatEndpoint1.onMessage(session1, messageToSend);


		Message message1 = messageArgumentCaptor1.getAllValues().get(0);
		Message message2 = messageArgumentCaptor2.getAllValues().get(0);

		// Asserts the returned messaged.
		assertEquals(testMessage, message1.getContent());
		assertEquals(testMessage, message2.getContent());
		assertEquals(message1.getFrom(), message2.getFrom());
	}


	/***
	 *  This methods tests the direct message function
	 * @throws IOException 
	 * 
	 */
	@Test
	public void endPointChatDirectTest() throws IOException{

		String broadcastMessage = "Broadcast Message";
		Message messageToBroadcast = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.BROADCAST_MESSAGE.label)
				.setMessageContent(broadcastMessage)
				.build();

		chatEndpoint1.onMessage(session1, messageToBroadcast);

		Message message1 = messageArgumentCaptor1.getAllValues().get(0);
		Message message2 = messageArgumentCaptor2.getAllValues().get(0);

		String userID1 = message1.getFrom();

		chatEndpoint1.onMessage(session2, messageToBroadcast);

		message1 = messageArgumentCaptor1.getAllValues().get(1);
		message2 = messageArgumentCaptor2.getAllValues().get(1);

		String userID2 = message2.getFrom();


		String DirectMessage = "Broadcast Message";

		//send direct message 
		Message messageToDirectSend = Message.messageBuilder()
				.setFrom(userID1)
				.setTo(userID2)
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(DirectMessage)
				.build();

		chatEndpoint1.onMessage(session1, messageToDirectSend);


		message2 = messageArgumentCaptor2.getAllValues().get(2);

		assertEquals(messageArgumentCaptor1.getAllValues().size(), 2);
		assertEquals(DirectMessage, message2.getContent());
		assertEquals(userID1, message2.getFrom());

	}
	
	/***
	 *  This methods tests the login of session function
	 * @throws IOException 
	 * 
	 */
	@Test
	public void endPointChatSessionLoginTest() throws IOException{

		String broadcastMessage = "Broadcast Message";
		Message messageToBroadcast = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.BROADCAST_MESSAGE.label)
				.setMessageContent(broadcastMessage)
				.build();

		chatEndpoint1.onMessage(session1, messageToBroadcast);

		Message message = messageArgumentCaptor1.getAllValues().get(0);

		String userID1 = message.getFrom();
		
		ChatEndpoint.userLogin(userName1, userID1);
		
		chatEndpoint1.onMessage(session1, messageToBroadcast);

		message = messageArgumentCaptor1.getAllValues().get(1);

		assertEquals(userName1, message.getFrom());

	}

	
	/***
	 *  This methods tests the login of session  fails function
	 * @throws IOException 
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void endPointChatSessionLoginFailureTest() throws IOException{

		String broadcastMessage = "Broadcast Message";
		Message messageToBroadcast = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.BROADCAST_MESSAGE.label)
				.setMessageContent(broadcastMessage)
				.build();

		
		
		chatEndpoint1.onMessage(session1, messageToBroadcast);

		Message message = messageArgumentCaptor1.getAllValues().get(0);

		String userID1 = message.getFrom();
		
		chatEndpoint1.onClose(session1);
		chatEndpoint2.onClose(session2);
		
		ChatEndpoint.userLogin(userName1, userID1);

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
