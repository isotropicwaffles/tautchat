package com.neu.prattle.messaging;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.neu.prattle.websocket.ChatEndpoint;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;
import com.neu.prattle.service.group.GroupServiceCommands;
import com.neu.prattle.service.group.GroupServiceImpl;
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
public class SendingMessagesTest {

	ChatEndpoint chatEndpoint1;
	ChatEndpoint chatEndpoint2;
	ChatEndpoint chatEndpoint3;
	ChatEndpoint chatEndpoint4;
	ChatEndpoint chatEndpoint5;
	ChatEndpoint chatEndpoint6;

	Session session1;
	Session session2;
	Session session3;
	Session session4;
	Session session5;
	Session session6;
	
	final String userName1 = "HellomockUser4";
	final String userName2 = "HellomockUser5";
	final String userName3 = "mockUser6";
	final String userName4 = "mockUser7";
	final String userName5 = "mockUser8";
	final String userName6 = "mockUser9";
	final String nonOverlappingUserName = "2134124";
	final String partialOverlappingTwoUserNames = "hello";

	final String groupName1 = "mockGroup1";
	final String groupName2 = "mockGroup2";
	final String groupName3 = "mockGroup3";
	final String groupName4 = "mockGroup4";
	final String groupName5 = "mockGroup5";
	final String groupName6 = "mockGroup6";

	final String sessionID1 = "8";
	final String sessionID2 = "9";
	final String sessionID3 = "10";
	final String sessionID4 = "11";
	final String sessionID5 = "12";
	final String sessionID6 = "13";
	
	final String failureResponse = GenericMessageResponses.FAILURE_RESPONSE.label;
	final String successResponse = GenericMessageResponses.SUCCESS_RESPONSE.label;

	//Create Group Messages 
	Message createGroup1 = createGroupMessage(groupName1,userName1);
	Message createGroup2 = createGroupMessage(groupName2,userName2);
	Message createGroup3 = createGroupMessage(groupName3,userName3);
	Message createGroup4 = createGroupMessage(groupName4,userName4);
	Message createGroup5 = createGroupMessage(groupName5,userName5);
	Message createGroup6 = createGroupMessage(groupName6,userName6);
	
	// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
	// to our mock object.
	ArgumentCaptor<Message> messageArgumentCaptor1;
	ArgumentCaptor<Message> messageArgumentCaptor2;
	ArgumentCaptor<Message> messageArgumentCaptor3;
	ArgumentCaptor<Message> messageArgumentCaptor4;
	ArgumentCaptor<Message> messageArgumentCaptor5;
	ArgumentCaptor<Message> messageArgumentCaptor6;

	String RESERVED_SEPERATOR = ",";
	/***
	 * Called up each test before invocation.
	 * @throws EncodeException  - an encodeexception
	 * @throws IOException - an ioexception
	 * @throws TimeoutException - a timeoutexception
	 */
	@Before
	public void setUp() throws IOException, EncodeException, TimeoutException {
	    UserServiceImpl.setEnableDBConnection(false);  

		UserServiceImpl.getInstance();
		
		UserServiceImpl.clearAllUsers();

		// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
		// to our mock object.
		messageArgumentCaptor1 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor2 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor3 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor4 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor5 = ArgumentCaptor.forClass(Message.class);
		messageArgumentCaptor6 = ArgumentCaptor.forClass(Message.class);
		
		chatEndpoint1 = new ChatEndpoint();
		chatEndpoint2 = new ChatEndpoint();
		chatEndpoint3 = new ChatEndpoint();
		chatEndpoint4 = new ChatEndpoint();
		chatEndpoint5 = new ChatEndpoint();
		chatEndpoint6 = new ChatEndpoint();

		session1 = generateMockSession(messageArgumentCaptor1, sessionID1);
		session2 = generateMockSession(messageArgumentCaptor2, sessionID2);
		session3 = generateMockSession(messageArgumentCaptor3, sessionID3);
		session4 = generateMockSession(messageArgumentCaptor4, sessionID4);
		session5 = generateMockSession(messageArgumentCaptor5, sessionID5);
		session6 = generateMockSession(messageArgumentCaptor6, sessionID6);
		
		chatEndpoint1.onOpen(session1, userName1);
		chatEndpoint2.onOpen(session2, userName2);
		chatEndpoint3.onOpen(session3, userName3);
		chatEndpoint4.onOpen(session4, userName4);
		chatEndpoint5.onOpen(session5, userName5);
		chatEndpoint6.onOpen(session6, userName6);
		//Remove the current users loaded from the database
		// Login Users
		userCreateAndLogin(userName1, chatEndpoint1, session1, messageArgumentCaptor1);
		userCreateAndLogin(userName2, chatEndpoint2, session2, messageArgumentCaptor2);
		userCreateAndLogin(userName3, chatEndpoint3, session3, messageArgumentCaptor3);
		userCreateAndLogin(userName4, chatEndpoint4, session4, messageArgumentCaptor4);
		userCreateAndLogin(userName5, chatEndpoint5, session5, messageArgumentCaptor5);
		userCreateAndLogin(userName6, chatEndpoint6, session6, messageArgumentCaptor6);


	}


	/***
	 * Called up each test after completed.
	 * 
	 */
	@After
	public void destroy() {
		UserServiceImpl.clear();
		GroupServiceImpl.clear();
		chatEndpoint1.onClose(session1);
		chatEndpoint2.onClose(session2);
		chatEndpoint3.onClose(session3);
		chatEndpoint4.onClose(session4);
		chatEndpoint5.onClose(session5);
		chatEndpoint6.onClose(session6);

		messageArgumentCaptor1 = null;
		messageArgumentCaptor2 = null;
		messageArgumentCaptor3 = null;
		messageArgumentCaptor4 = null;
		messageArgumentCaptor5 = null;
		messageArgumentCaptor6 = null;
		
		chatEndpoint1 = null;
		chatEndpoint2 = null;
		chatEndpoint3 = null;
		chatEndpoint4 = null;
		chatEndpoint5 = null;
		chatEndpoint6 = null;
		
		session1 = null;
		session2 = null;
		session3 = null;
		session4 = null;
		session5 = null;
		session6 = null;
	    UserServiceImpl.setEnableDBConnection(true);  


	}
	
	@Test
	public void sendDirectMessageTest() throws IOException, TimeoutException{


		String user1To2Content = "Hello User Number 2";
		Message messageFromUser1ToUser2 =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(userName2)
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(user1To2Content)
				.build();
		
		// Send Message from user 1 to user 2 but not any other user
		chatEndpoint1.onMessage(session1, messageFromUser1ToUser2);
		
		
		// Assert User 1 and User 3 received one less message than User 2
		// Note: The first to messages are the user creation and login confirmations
		
		assertEquals(messageArgumentCaptor2.getAllValues().size()-1, messageArgumentCaptor1.getAllValues().size());
		assertEquals(messageArgumentCaptor2.getAllValues().size()-1, messageArgumentCaptor3.getAllValues().size());

		Message messageReceived = messageArgumentCaptor2.getValue();
        
		// Assert that User 2 received a message from user 1
		assertEquals(userName1, messageReceived.getFrom());
		assertEquals(userName2, messageReceived.getTo());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, messageReceived.getType());
		assertEquals(user1To2Content, messageReceived.getContent());

		}
	
	@Test
	public void sendBroadcastMessageTest() throws IOException, TimeoutException{


		String user1ToAll = "Hello All Users";
		Message messageFromUser1ToAll =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo("")
				.setType(MessageAddresses.BROADCAST_MESSAGE.label)
				.setMessageContent(user1ToAll)
				.build();
		
		// Send Message from user 1 to user 2 but not any other user
		chatEndpoint1.onMessage(session1, messageFromUser1ToAll);
		
		
		Message messageReceived_1 = messageArgumentCaptor1.getValue();
		Message messageReceived_2 = messageArgumentCaptor2.getValue();
		Message messageReceived_3 = messageArgumentCaptor3.getValue();

		// Assert that User 2 received a message from user 1
		assertEquals(userName1, messageReceived_2.getFrom());
		assertEquals("", messageReceived_2.getTo());
		assertEquals(MessageAddresses.BROADCAST_MESSAGE.label, messageReceived_2.getType());
		assertEquals(user1ToAll, messageReceived_2.getContent());

		
		// Assert that User 3 received a message from user 1
		assertEquals(userName1, messageReceived_3.getFrom());
		assertEquals("", messageReceived_3.getTo());
		assertEquals(MessageAddresses.BROADCAST_MESSAGE.label, messageReceived_3.getType());
		assertEquals(user1ToAll, messageReceived_3.getContent());
		
		
		// Assert that User 3 received a message from user 1
		assertEquals(userName1, messageReceived_1.getFrom());
		assertEquals("", messageReceived_1.getTo());
		assertEquals(MessageAddresses.BROADCAST_MESSAGE.label, messageReceived_1.getType());
		assertEquals(user1ToAll, messageReceived_1.getContent());
		}
	
	
	@Test
	public void sendGroupMessageTest() throws IOException, TimeoutException{

		// Create group with a mod of user 3
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);

		// Add User 2 and 3 to the group
	
		Message addUserToGroup =  Message.messageBuilder()
				.setFrom(userName3)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_USER.label)
				.setMessageContent(userName2)
				.build();
		
		sendMessageAndWaitForResponse(session3, chatEndpoint3, addUserToGroup, messageArgumentCaptor3);
		
		
		// Send message to group 3 from user 1
		String user1ToGroup3 = "Hello Group 3";
		
		Message messageToGroup3 =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_MESSAGE.label)
				.setMessageContent(user1ToGroup3)
				.build();
		
		// Send Message from user 1 to user 2 and 3
		chatEndpoint3.onMessage(session1, messageToGroup3);
		
		
		Message messageReceived_1 = messageArgumentCaptor1.getValue();
		Message messageReceived_2 = messageArgumentCaptor2.getValue();
		Message messageReceived_3 = messageArgumentCaptor3.getValue();

		// Assert that user 1 did not receive the message
		assertEquals(messageArgumentCaptor1.getAllValues().size()+1, messageArgumentCaptor2.getAllValues().size());
		assertEquals(messageArgumentCaptor1.getAllValues().size()+3, messageArgumentCaptor3.getAllValues().size());
		assertNotEquals(MessageAddresses.GROUP_MESSAGE.label, messageReceived_1.getContentType());

		// Assert that User 2 received a message group message
		assertEquals(userName1, messageReceived_2.getFrom());
		assertEquals(userName2, messageReceived_2.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label, messageReceived_2.getContentType());
		assertEquals(user1ToGroup3, messageReceived_2.getContent());

		
		// Assert that User 3 received a message group message
		assertEquals(userName1, messageReceived_3.getFrom());
		assertEquals(userName3, messageReceived_3.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label,  messageReceived_3.getContentType());
		assertEquals(user1ToGroup3, messageReceived_3.getContent());

		}
	
	
	@Test
	public void sendGroupMessageToSubGroupTest() throws IOException, TimeoutException{

		// Create groups with a mod of user 3 and 4 respectively
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);
		sendMessageAndWaitForResponse(session4, chatEndpoint4, createGroup4, messageArgumentCaptor4);

		// Add User 2 to the group 3
	
		Message addUserToGroup =  Message.messageBuilder()
				.setFrom(userName3)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_USER.label)
				.setMessageContent(userName2)
				.build();
		
		sendMessageAndWaitForResponse(session3, chatEndpoint3, addUserToGroup, messageArgumentCaptor3);
		
		
		// Add User 5 to the group 4
		addUserToGroup.setFrom(userName4);
		addUserToGroup.setContent(userName6);
		addUserToGroup.setTo(groupName4);
		sendMessageAndWaitForResponse(session4, chatEndpoint4, addUserToGroup, messageArgumentCaptor4);
		
		// Add Group 4 as a subgroup to group 3
		Message addSubGroup =  Message.messageBuilder()
				.setFrom(userName3)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_GROUP.label)
				.setMessageContent(groupName4)
				.build();
		sendMessageAndWaitForResponse(session3, chatEndpoint3, addSubGroup, messageArgumentCaptor3);

		// Send message to group 3 from user 1
		String user1ToGroup3 = "Hello Group 3";
		
		Message messageToGroup3 =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_MESSAGE.label)
				.setMessageContent(user1ToGroup3)
				.build();
		
		// Send Message from user 1 to user 2 and 3
		chatEndpoint3.onMessage(session1, messageToGroup3);
		
		
		Message messageReceived_1 = messageArgumentCaptor1.getValue();
		Message messageReceived_2 = messageArgumentCaptor2.getValue();
		Message messageReceived_3 = messageArgumentCaptor3.getValue();
		Message messageReceived_4 = messageArgumentCaptor4.getValue();
		Message messageReceived_5 = messageArgumentCaptor5.getValue();
		Message messageReceived_6 = messageArgumentCaptor6.getValue();

		// Assert that user 1 and 5 did not receive the message
		assertNotEquals(MessageAddresses.GROUP_MESSAGE.label, messageReceived_1.getContentType());
		assertNotEquals(MessageAddresses.GROUP_MESSAGE.label, messageReceived_5.getContentType());

		// Assert that User 2 received a message group message
		assertEquals(userName1, messageReceived_2.getFrom());
		assertEquals(userName2, messageReceived_2.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label, messageReceived_2.getContentType());
		assertEquals(user1ToGroup3, messageReceived_2.getContent());

		
		// Assert that User 3 received a message group message
		assertEquals(userName1, messageReceived_3.getFrom());
		assertEquals(userName3, messageReceived_3.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label,  messageReceived_3.getContentType());
		assertEquals(user1ToGroup3, messageReceived_3.getContent());
		
		// Assert that User 4 received a message group message
		assertEquals(userName1, messageReceived_4.getFrom());
		assertEquals(userName4, messageReceived_4.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label,  messageReceived_4.getContentType());
		assertEquals(user1ToGroup3, messageReceived_4.getContent());
		
		
		// Assert that User 6 received a message group message
		assertEquals(userName1, messageReceived_6.getFrom());
		assertEquals(userName6, messageReceived_6.getTo());
		assertEquals(MessageAddresses.GROUP_MESSAGE.label,  messageReceived_6.getContentType());
		assertEquals(user1ToGroup3, messageReceived_6.getContent());

		}
	

	/**
	 * Tests if a user can send a group invite message
	 * @throws IOException - an ioexception
	 * @throws TimeoutException - a timeoutexception
	 */
	
	@Test
	public void sendGroupInviteToUserTest() throws IOException, TimeoutException{

		
		//Invite User to Group that doesn't exist 
		Message messageToQuery=  Message.messageBuilder()
				.setFrom(userName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.INVITE_USER.label)
				.setMessageContent(userName2)
				.setAdditionalInfo(groupName1)
				.build();
		
	
		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		Message messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.GROUP_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(GroupServiceCommands.INVITE_USER.label, messageReceived_1.getContentType());
		assertEquals(String.format("Group %s could not be found", groupName1) , messageReceived_1.getContent());
			
		//Invite NonExisting User to Group that does exist
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);

		messageToQuery.setContent(nonOverlappingUserName);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.GROUP_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(GroupServiceCommands.INVITE_USER.label, messageReceived_1.getContentType());
		assertEquals(String.format("User %s could not be found", nonOverlappingUserName) , messageReceived_1.getContent());
		
		
		
		//Invite user to group that they exist in
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);

		messageToQuery.setFrom(userName2);
		messageToQuery.setContent(userName1);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.GROUP_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(GroupServiceCommands.INVITE_USER.label, messageReceived_1.getContentType());
		assertEquals(failureResponse, messageReceived_1.getContent());
		
		
		//Invite new user group that they not a part of
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);

		messageToQuery.setFrom(userName1);
		messageToQuery.setContent(userName2);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		messageReceived_1 = messageArgumentCaptor1.getValue();
		Message messageReceived_2 = messageArgumentCaptor2.getValue();

		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.GROUP_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(GroupServiceCommands.INVITE_USER.label, messageReceived_1.getContentType());
		assertEquals(successResponse, messageReceived_1.getContent());
		
		
		assertEquals(userName2, messageReceived_2.getTo());
		assertEquals(userName1, messageReceived_2.getFrom());
		assertEquals(GroupServiceCommands.INVITE_USER.label, messageReceived_2.getContentType());
		assertEquals( messageReceived_2.getFrom() + " invites you to join the following group: " + messageReceived_2.getAdditionalInfo() + "!", messageReceived_2.getContent());
		assertEquals(groupName1, messageReceived_2.getAdditionalInfo() );


	}
	
	
	
	/**
	 * Test if you can search for users by partial names
	 * @throws IOException - an ioexception
	 * @throws TimeoutException - an timeout exception
	 */
	@Test
	public void userSearchMessageTest() throws IOException, TimeoutException{

		//Query No User from non-matching query
		Message messageToQuery=  Message.messageBuilder()
				.setFrom(userName1)
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.SEARCH_USERS_BY_NAME.label)
				.setMessageContent(nonOverlappingUserName)
				.build();
		
	
		String userNameReponses = "";

		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		Message messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.USER_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(UserServiceCommands.SEARCH_USERS_BY_NAME.label, messageReceived_1.getContentType());
		assertEquals(userNameReponses, messageReceived_1.getContent());

		
		
		//Query partial name of two users
		messageToQuery.setContent(partialOverlappingTwoUserNames);
		
		userNameReponses =  userName1 + RESERVED_SEPERATOR + userName2;
		
		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.USER_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(UserServiceCommands.SEARCH_USERS_BY_NAME.label, messageReceived_1.getContentType());
		assertEquals(userNameReponses, messageReceived_1.getContent());

			
		//Query All User from empty string

		messageToQuery.setContent("");
		
		userNameReponses =  userName3 + RESERVED_SEPERATOR + userName4 + RESERVED_SEPERATOR + 
    			userName5 + RESERVED_SEPERATOR + userName6 + RESERVED_SEPERATOR +
    			userName1 + RESERVED_SEPERATOR + userName2;;
		
		sendMessageAndWaitForResponse(session1, chatEndpoint1, messageToQuery, messageArgumentCaptor1);
		messageReceived_1 = messageArgumentCaptor1.getValue();
		assertEquals(userName1, messageReceived_1.getTo());
		assertEquals(MessageAddresses.USER_SERVICE.label, messageReceived_1.getFrom());
		assertEquals(UserServiceCommands.SEARCH_USERS_BY_NAME.label, messageReceived_1.getContentType());
		assertEquals(userNameReponses, messageReceived_1.getContent());

	}
	
	/***
	 * Creates and logins in a given user to a given session
	 * 
	 * @param session - Session to log into
	 * @param userName - The user name to login
	 * @param chatEndpoint - endpoint to message
	 * @param messageArgumentCaptor - argumentCapture to wait for
	 * @throws TimeoutException	- a timeoutexception
 	 * @throws IOException	- an ioexception
	 */
	private void userCreateAndLogin(String userName, ChatEndpoint chatEndpoint, Session session, ArgumentCaptor<Message> messageArgumentCaptor) throws IOException, TimeoutException {

		// create User
		Message createUserRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.USER_CREATE.label)
				.setMessageContent(userName)
				.build();

		sendMessageAndWaitForResponse(session, chatEndpoint, createUserRequest, messageArgumentCaptor);
		
		Message loginRequest = Message.messageBuilder()
				.setFrom("")
				.setTo("")
				.setType(MessageAddresses.USER_SERVICE.label)
				.setContentType(UserServiceCommands.LOGIN.label)
				.setMessageContent(userName)
				.build();
		
		sendMessageAndWaitForResponse(session, chatEndpoint, loginRequest, messageArgumentCaptor);


	}
	/***
	 * Sends a message to a session/chat endpoint and wait for response
	 * 
	 * @param session - Session to log into
	 * @param chatEndpoint - endpoint to message
	 * @param message - the message to send
	 * @param messageArgumentCaptor - argumentCapture to wait for
	 * @throws TimeoutException	- an timeoutexception
 	 * @throws IOException	- an ioexception
	 */
	private void sendMessageAndWaitForResponse(Session session, ChatEndpoint chatEndpoint, Message message, ArgumentCaptor<Message> messageArgumentCaptor ) throws IOException, TimeoutException {
		float timeOut = 1; //seconds

		int messageCount = messageArgumentCaptor.getAllValues().size() + 1; // message counter to stop waiting

		chatEndpoint.onMessage(session, message);

		waitForMessage(messageArgumentCaptor, messageCount,  timeOut);
	}
	
	
	/***
	 * Create group message
	 * 
	 * @param userName - The user name to use as moderator
	 * @param groupName - name of group to create
	 * @return Message to create group
	 */
	private Message createGroupMessage(String groupName, String userName){
		
		// create User
		return Message.messageBuilder()
				.setFrom(userName)
				.setTo("")
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GROUP_CREATE.label)
				.setMessageContent(groupName)
				.build();
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
	 * @throws IOException -  an ioexception
	 * @throws EncodeException - an encodeexception
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
