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
public class GroupServiceManagementTest {

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

	final Message createGroup1 = createGroupMessage(groupName1,userName1);
	final Message createGroup2 = createGroupMessage(groupName2,userName2);
	final Message createGroup3 = createGroupMessage(groupName3,userName3);
	// Create an instance of argument captor. As the name goes, useful to capture argumemnts passed
	// to our mock object.
	ArgumentCaptor<Message> messageArgumentCaptor1;
	ArgumentCaptor<Message> messageArgumentCaptor2;
	ArgumentCaptor<Message> messageArgumentCaptor3;


	/***
	 * Called up each test before invocation.
	 * @throws EncodeException - an encode exception
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

	
	/***
	 * Test create group message processing
	 * @throws IOException - an ioexception
	 * @throws TimeoutException - a timeoutexception
	 * 
	 */
	@Test
	public void createDeleteGroupMessageTest() throws IOException, TimeoutException{


		
		userCreateAndLogin(userName1, chatEndpoint1, session1, messageArgumentCaptor1);
		userCreateAndLogin(userName2, chatEndpoint2, session2, messageArgumentCaptor2);

		//Should be successful
		Message createGroup = createGroupMessage(groupName1,userName1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup, messageArgumentCaptor1);
	
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1,  GroupServiceCommands.GROUP_CREATE.label, successResponse);
		
		//Should be a failure

		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1,  GroupServiceCommands.GROUP_CREATE.label, failureResponse );
		
		//Should be a failure
		Message deleteGroup = deleteGroupMessage(groupName2,userName1);
		
		sendMessageAndWaitForResponse(session1, chatEndpoint1, deleteGroup, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1,  GroupServiceCommands.GROUP_DELETE.label, String.format("Group %s could not be found", groupName2));

		//Should be succesful
		deleteGroup = deleteGroupMessage(groupName1,userName1);
		
		sendMessageAndWaitForResponse(session1, chatEndpoint1, deleteGroup, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GROUP_DELETE.label, successResponse );
		
		//Should be a failure
		sendMessageAndWaitForResponse(session1, chatEndpoint1, deleteGroup, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GROUP_DELETE.label, String.format("Group %s could not be found", groupName1) );
		
	}
	
	@Test
	public void testApproveAddUserTest() throws IOException, TimeoutException{


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
		Message createGroup = createGroupMessage(groupName1,userName1);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup, messageArgumentCaptor1);

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
		
		// Two Users
		
		Message invalidAddUserToGroup =  Message.messageBuilder()
				.setFrom(userName2)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_USER.label)
				.setMessageContent(userName3)
				.build();
		// Add user
		sendMessageAndWaitForResponse(session2, chatEndpoint2, invalidAddUserToGroup, messageArgumentCaptor2);
	

		commonGroupMessageAsserts(messageArgumentCaptor2.getValue(), userName2, GroupServiceCommands.APPROVE_ADD_USER.label, 
				failureResponse);
		// Get users
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getUsers, messageArgumentCaptor1);

		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_USERS.label, 
				userName1 +  ReservedCharacters.LIST_SEPARATORS .label + userName2 );
	}
		
	@Test
	public void testApproveRemoveUserTest() throws IOException, TimeoutException{
		// Invalid Group User
		
		Message getUsers =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GET_GROUP_USERS.label)
				.setMessageContent("")
				.build();
		
		Message removeUsers =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.REMOVE_USER.label)
				.setMessageContent(userName1)
				.build();
		
		Message addUserToGroup =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_USER.label)
				.setMessageContent(userName2)
				.build();
		
		// remove user from group that doesn't exist
		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeUsers, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_USER.label, (String.format("Group %s could not be found", groupName1)));

		// Create Group
		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);

		//Failure to remove mod
		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeUsers, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_USER.label, failureResponse);

		//Add 2 users
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addUserToGroup, messageArgumentCaptor1);
		addUserToGroup.setContent(userName3);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addUserToGroup, messageArgumentCaptor1);
		
		//Fail at removing user due to it being a third nonmodering user requesting it
		removeUsers.setFrom(userName3);
		removeUsers.setContent(userName2);

		sendMessageAndWaitForResponse(session3, chatEndpoint3, removeUsers, messageArgumentCaptor3);
		commonGroupMessageAsserts(messageArgumentCaptor3.getValue(), userName3, GroupServiceCommands.REMOVE_USER.label, failureResponse);

		//Succeed at user removing theirself
		removeUsers.setFrom(userName2);
		removeUsers.setContent(userName2);

		sendMessageAndWaitForResponse(session2, chatEndpoint2, removeUsers, messageArgumentCaptor2);
		commonGroupMessageAsserts(messageArgumentCaptor2.getValue(), userName2, GroupServiceCommands.REMOVE_USER.label, successResponse);
		
		//Succeed at mod removing theirself
		removeUsers.setFrom(userName1);
		removeUsers.setContent(userName3);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeUsers, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_USER.label, successResponse);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, getUsers, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.GET_GROUP_USERS.label,userName1);
		
	}

	@Test
	public void testApproveRemoveSubgroupTest() throws IOException, TimeoutException{

		// Invalid Group User
		
		Message removeSubGroups =  Message.messageBuilder()
				.setFrom(userName2)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.REMOVE_GROUP.label)
				.setMessageContent(groupName2)
				.build();
		
		Message addSubGroup =  Message.messageBuilder()
				.setFrom(userName1)
				.setTo(groupName1)
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.APPROVE_ADD_GROUP.label)
				.setMessageContent(groupName2)
				.build();
		
		// Get subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeSubGroups, messageArgumentCaptor1);
		
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_GROUP.label, (String.format("Group %s could not be found", groupName1)));

		
		//create Groups

		sendMessageAndWaitForResponse(session1, chatEndpoint1, createGroup1, messageArgumentCaptor1);
		sendMessageAndWaitForResponse(session2, chatEndpoint2, createGroup2, messageArgumentCaptor2);
		sendMessageAndWaitForResponse(session3, chatEndpoint3, createGroup3, messageArgumentCaptor3);
		
		// Remove group that's not a subgroup
		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeSubGroups, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_GROUP.label, failureResponse);

		// Add two subgroups
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addSubGroup, messageArgumentCaptor1);
		addSubGroup.setContent(groupName3);
		sendMessageAndWaitForResponse(session1, chatEndpoint1, addSubGroup, messageArgumentCaptor1);
		
		// Remove a subgroup by a non-authorized user
		sendMessageAndWaitForResponse(session3, chatEndpoint3, removeSubGroups, messageArgumentCaptor3);
		commonGroupMessageAsserts(messageArgumentCaptor3.getValue(), userName3, GroupServiceCommands.REMOVE_GROUP.label, failureResponse);
	
		// Sub a subgroup by a authorized user
		sendMessageAndWaitForResponse(session2, chatEndpoint2, removeSubGroups, messageArgumentCaptor2);
		commonGroupMessageAsserts(messageArgumentCaptor2.getValue(), userName2, GroupServiceCommands.REMOVE_GROUP.label, successResponse);
		
		// Sub a subgroup by a mod
		removeSubGroups.setContent(groupName3);
		removeSubGroups.setFrom(userName1);

		sendMessageAndWaitForResponse(session1, chatEndpoint1, removeSubGroups, messageArgumentCaptor1);
		commonGroupMessageAsserts(messageArgumentCaptor1.getValue(), userName1, GroupServiceCommands.REMOVE_GROUP.label, successResponse);
		
		
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
	 * @throws TimeoutException	- a timeout exception
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
	 * @param message - a message to send
	 * @param messageArgumentCaptor - argumentCapture to wait for
	 * @throws TimeoutException	- a timeoutexception
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
	 * Delete group message
	 * 
	 * @param userName - The user name trying to delete group
	 * @param groupName - name of group to delete
	 * @return Message to delete group
	 */
	private Message deleteGroupMessage(String groupName, String userName){
		
		// delete group
		return Message.messageBuilder()
				.setFrom(userName)
				.setTo("")
				.setType(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(GroupServiceCommands.GROUP_DELETE.label)
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
