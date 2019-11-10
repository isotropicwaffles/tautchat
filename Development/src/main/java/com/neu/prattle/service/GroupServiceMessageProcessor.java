package com.neu.prattle.service;


import java.io.IOException;
import java.util.Optional;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.IMessageProcessorFactory;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.messaging.MessageProcessorFactory;
import com.neu.prattle.messaging.TypeOfMessageProcessor;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.websocket.ChatEndpoint;
import com.neu.prattle.websocket.SessionServiceCommands;


/**
 *
 * This is a Groiup service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GroupServiceMessageProcessor implements IMessageProcessor {

	/**
	*	User Service instance
	*/
	private static UserService accountService = UserServiceImpl.getInstance();
	
	
	/**
	*	Group Service instance
	*/
	//private static GroupService groupAccountService = UserServiceImpl.getInstance();

	/**
	*	Singleton instance of this class
	*/
	private static IMessageProcessor instance = new GroupServiceMessageProcessor();

	/**
	*	Factory for generating message processors
	*/
	private IMessageProcessorFactory mPF = MessageProcessorFactory.getInstance();

	/**
	*	Private constructor for class
	*/
	private GroupServiceMessageProcessor(){
	}

	/**
	*  Returns singleton instance of this class
	*
	*  @returns instance - instance of this clas 
	*/
	public static IMessageProcessor getInstance()  {

		return GroupServiceMessageProcessor.instance;
	}


	/**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - True if the message was sent to the user services
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		return message.getType().contentEquals(MessageAddresses.GROUP_SERVICE.label);
	}


	
	/**
	 * This processes the received message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	public void processMessage(Message message) throws IOException {	

		if (message.getContentType().equals(GroupServiceCommands.GROUP_CREATE.label)) {
			
			processGroupCreate(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.GROUP_DELETE.label)) {
			
			processGroupDelete(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.APPROVE_ADD_USER.label)) {
			
			processApproveAddUser(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.APPROVE_ADD_GROUP.label)) {
			
			processApproveAddSubGroup(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.REMOVE_USER.label)) {
			
			processRemoveUser(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.REMOVE_GROUP.label)) {
			
			processRemoveSubGroup(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.REQUEST_ADD_USER.label)) {
			
			processRequestAddUser(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.REQUEST_ADD_GROUP.label)) {
			
			 processRequestAddSubGroup(message);
		} else if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_USERS.label)) {

			processGetUsersFromGroup(message);
			
		} else if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_MODERATOR.label)) {
			
			processGetModeratorFromGroup(message); 
		}
		

	}


	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processGroupCreate(Message message) {	

	}
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processGroupDelete(Message message) {	

	}
	
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processRequestAddUser(Message message) {	

	}
	
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processRequestAddSubGroup(Message message) {	

	}
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processApproveAddUser(Message message) {	

	}
	
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processApproveAddSubGroup(Message message) {	

	}
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processRemoveUser(Message message) {	

	}
	
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processRemoveSubGroup(Message message) {	

	}
	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processGetUsersFromGroup(Message message) {	

	}

	
	/**
	 * (TODO)
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processGetModeratorFromGroup(Message message) {	

	}


	/**
	 * Sends message to general router
	 * 
	 * @param response - a message response being sent from the user service
	 */
	private void sendMessage(Message response) throws IOException {
		if(mPF.getInstanceOf(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR).canProcessMessage(response)) {
			mPF.getInstanceOf(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR).processMessage(response);
		}
	}


	/**
	 * This generates a response message from the user servicese
	 * 
	 * @param receiver - to whom the message should be sent
	 * @param response - the response that is being sent
	 */
	private Message generateResponseMessage(String receiver, String response) {
		return Message.messageBuilder()
				.setFrom(MessageAddresses.USER_SERVICE.label)
				.setTo(receiver)
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(response)
				.build();
	}

	





}
