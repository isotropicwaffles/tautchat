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
public class GroupServiceQueryTest {


	ChatEndpoint chatEndpoint1;
	ChatEndpoint chatEndpoint2;
	ChatEndpoint chatEndpoint3;


	Session session1;
	Session session2;
	Session session3;

	final String userName1 = "mockUser4";
	final String userName2 = "mockUser5";
	final String userName3 = "mockUser6";
	
	final String groupName1 = "mockGroup1";
	final String groupName2 = "mockGroup2";
	final String groupName3 = "mockGroup3";

	final String sessionID1 = "8";
	final String sessionID2 = "9";
	final String sessionID3 = "10";

	final String failureResponse = GenericMessageResponses.FAILURE_RESPONSE.label;
	final String successResponse = GenericMessageResponses.SUCCESS_RESPONSE.label;

	//Create Group Messages 
	Message createGroup1 = createGroupMessage(groupName1,userName1);
	Message createGroup2 = createGroupMessage(groupName2,userName2);
	Message createGroup3 = createGroupMessage(groupName3,userName3);
	
	// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
	// to our mock object.
	ArgumentCaptor<Message> messageArgumentCaptor1;
	ArgumentCaptor<Message> messageArgumentCaptor2;
	ArgumentCaptor<Message> messageArgumentCaptor3;

	String RESERVED_SEPERATOR = ",";


	/***
	 * Called up each test before invocation.
	 * @throws EncodeException -an encodeexception
	 * @throws IOException - an ioexception
	 * @throws TimeoutException - a timeoutexception
	 */
	@Before
	public void setUp() throws IOException, EncodeException, TimeoutException {
	    UserServiceImpl.setEnableDBConnection(false);  

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

		// Login Users
		userCreateAndLogin(userName1, chatEndpoint1, session1, messageArgumentCaptor1);
		userCreateAndLogin(userName2, chatEndpoint2, session2, messageArgumentCaptor2);
		userCreateAndLogin(userName3, chatEndpoint3, session3, messageArgumentCaptor3);
		


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

		messageArgumentCaptor1 = null;
		messageArgumentCaptor2 = null;
		messageArgumentCaptor3 = null;

		chatEndpoint1 = null;
		chatEndpoint2 = null;
		chatEndpoint3 = null;

		session1 = null;
		session2 = null;
		session3 = null;
		
	    UserServiceImpl.setEnableDBConnection(false);  

	}
	
	@Test
	public void testGetModerator() throws IOException, TimeoutException{


		
		userCreateAndLogin(userName1, chatEndpoint1, session1, messageArgumentCaptor1);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		
		Message getModerators =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_GROUP_MODERATORS.label)
				.setMessageContent("")
				.build();
		
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getModerators, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_MODERATORS.label, userName1);


		}
	
	@Test
	public void testGetUsersTest() throws IOException, TimeoutException{

		// Invalid Group User
		
		Message getUsers =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_GROUP_USERS.label)
				.setMessageContent("")
				.build();
		
		// Get users
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getUsers, messageArgumentCaptor1);
		
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_USERS.label, (String.format("Group %s could not be found", groupName1)));

		
		// Single User
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);

		// Get users
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getUsers, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_USERS.label, userName1);

		// Two Users
		
		Message addUserToGroup =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_USER.label)
				.setMessageContent(userName2)
				.build();
		// Add user
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addUserToGroup, messageArgumentCaptor1);
	
		// Get users
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getUsers, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_USERS.label, 
				userName1 +  ReservedCharacters.LIST_SEPARATORS .label + userName2 );

		}
	
	
	@Test
	public void testGetSubGroupsTest() throws IOException, TimeoutException{


		// Invalid Group User
		
		Message getSubGroups =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_GROUP_SUBGROUPS.label)
				.setMessageContent("")
				.build();
		
		Message addSubGroup =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_GROUP.label)
				.setMessageContent(groupName2)
				.build();
		
		// Get subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getSubGroups, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_SUBGROUPS.label, (String.format("Group %s could not be found", groupName1)));

		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session2, chatEndpoint2, createGroup2, messageArgumentCaptor2);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);
		
		// Zero subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getSubGroups, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_SUBGROUPS.label,"");

		// One subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addSubGroup, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getSubGroups, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_SUBGROUPS.label, groupName2);

		// Two subgroup
		addSubGroup.setContent(groupName3);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addSubGroup, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getSubGroups, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_SUBGROUPS.label, groupName2 +  ReservedCharacters.LIST_SEPARATORS .label + groupName3);	
	
}
	
	@Test
	public void testGetSuperGroupsTest() throws IOException, TimeoutException{

		// Invalid Group User
		
		Message getSuperGroups =  Message.messageBuilder()
				.setFrom(userName3)
				.setTo(groupName3)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_GROUP_SUPERGROUPS.label)
				.setMessageContent("")
				.build();
		
		Message addSubGroup =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_GROUP.label)
				.setMessageContent(groupName3)
				.build();
		
		// Get subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getSuperGroups, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_SUPERGROUPS.label, (String.format("Group %s could not be found", groupName3)));

		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session2, chatEndpoint2, createGroup2, messageArgumentCaptor2);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);
		
		// Zero supergroups
		sendMessageAndWaitForResponse(session3, chatEndpoint3, getSuperGroups, messageArgumentCaptor3);
		commonGroupMessageAsserts(messageArgumentCaptor3.getValue(), userName3, GroupServiceCommands.GET_GROUP_SUPERGROUPS.label,"");

		
		// One subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addSubGroup, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, getSuperGroups, messageArgumentCaptor3);
		commonGroupMessageAsserts(messageArgumentCaptor3.getValue(), userName3, GroupServiceCommands.GET_GROUP_SUPERGROUPS.label, groupName1);

		// Two subgroup
		addSubGroup.setTo(groupName2);
		sendMessageAndWaitForResponse(session2, chatEndpoint2, addSubGroup, messageArgumentCaptor2);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, getSuperGroups, messageArgumentCaptor3);
		commonGroupMessageAsserts(messageArgumentCaptor3.getValue(), userName3, GroupServiceCommands.GET_GROUP_SUPERGROUPS.label, groupName1 + RESERVED_SEPERATOR + groupName2 );
	
		
		}
	@Test
	public void testGetPendingUserInvitesTest() throws IOException, TimeoutException{

		// Invalid Group User
		
		Message getPendingUserInvites =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_PENDING_USER_REQUESTS.label)
				.setMessageContent("")
				.build();
		
		Message requestUserAdd =  Message.messageBuilder()
				.setFrom(userName2)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.REQUEST_ADD_USER.label)
				.setMessageContent(userName2)
				.build();
		
		
		// Error case
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingUserInvites, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_USER_REQUESTS.label, (String.format("Group %s could not be found", groupName1)));

		//create Group
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingUserInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_USER_REQUESTS.label,"");

		// One Request
		sendMessageAndWaitForResponse(session1, chatEndpoint1, requestUserAdd, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingUserInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_USER_REQUESTS.label,userName2);

		// Two Request
		requestUserAdd.setFrom(userName3);
		requestUserAdd.setContent(userName3);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, requestUserAdd, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingUserInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_USER_REQUESTS.label, 
				userName2 +  ReservedCharacters.LIST_SEPARATORS .label + userName3);

	
		}
	
	@Test
	public void testGetPendingSubGroupsInvitesTest() throws IOException, TimeoutException{

		// Invalid Group User
		
		Message getPendingSubgroupInvites =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label)
				.setMessageContent("")
				.build();
		
		Message requestSubGroupAdd =  Message.messageBuilder()
				.setFrom(userName2)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.REQUEST_ADD_GROUP.label)
				.setMessageContent(groupName2)
				.build();
		
		
		// Error case
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingSubgroupInvites, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label, (String.format("Group %s could not be found", groupName1)));

		//create Groups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session2, chatEndpoint2, createGroup2, messageArgumentCaptor2);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);
		
		//zero requests
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingSubgroupInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label,"");

		// One Request
		sendMessageAndWaitForResponse(session1, chatEndpoint1, requestSubGroupAdd, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingSubgroupInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label,groupName2);

		// Two Requests
		requestSubGroupAdd.setFrom(userName3);
		requestSubGroupAdd.setContent(groupName3);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, requestSubGroupAdd, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getPendingSubgroupInvites, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label, 
				groupName2 +  ReservedCharacters.LIST_SEPARATORS .label + groupName3);

		
	
	}
	
	@Test
	public void testInvalidContentTypeTest() throws IOException, TimeoutException{

		// Create invalid commands
		String invalidCommand = "Junk";
		Message incorrect_message_1 =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(invalidCommand)
				.setMessageContent("")
				.build();
		
		Message incorrect_message_2 =  Message.messageBuilder()
				.setFrom(userName2)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(invalidCommand)
				.setMessageContent("")
				.build();
		IMessageProcessorFactory mpf = MessageProcessorFactory.getInstance();
		
		IMessageProcessor mp1 = mpf.getInstanceOf(TypeOfMessageProcessor.GROUP_SERVICE_GROUP_MANAGEMENT_PROCESSOR);
		IMessageProcessor mp2 = mpf.getInstanceOf(TypeOfMessageProcessor.GROUP_SERVICE_GROUP_QUERY_PROCESSOR);

		mp1.processMessage(incorrect_message_1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, invalidCommand, GenericMessageResponses.UNKNOWN_COMMAND.label);

		
		mp2.processMessage(incorrect_message_2);
		commonGroupMessageAsserts(messageArgumentCaptor2.getValue(), userName2, invalidCommand,  GenericMessageResponses.UNKNOWN_COMMAND.label);

	
	}

	
	/***
	 * Asserts common expected contents of group messages
	 * 
	 * @param message - message received
	 * @param userName - The user name that sent query
	 * @param contentType - content type of message
	 * @param response  - content response in message
	 * 
	 * */
	
	private void commonGroupMessageAsserts(Message message, String userName, String contentType, String response) {
		
		assertEquals(MessageAddresses.GROUP_SERVICE.label, message.getFrom());
		assertEquals(contentType, message.getContentType());
		assertEquals(response, message.getContent());
		assertEquals(MessageAddresses.DIRECT_MESSAGE.label, message.getType());
		assertEquals(userName, message.getTo());
		
	}

	
	
	/***
	 * Creates and logins in a given user to a given session
	 * 
	 * @param session - Session to log into
	 * @param userName - The user name to login
	 * @param chatEndpoint - endpoint to message
	 * @param messageArgumentCaptor - argumentCapture to wait for
	 * @throws TimeoutException	 - a timeout exception
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
	 * @param message -  a message to send
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
	 * @throws IOException - an ioexception
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
