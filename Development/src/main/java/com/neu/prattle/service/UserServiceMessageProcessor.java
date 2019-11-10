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
 * This is a user service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class UserServiceMessageProcessor implements IMessageProcessor {

	/**
	*	User Service instance
	*/
	private static UserService accountService = UserServiceImpl.getInstance();

	/**
	*	Singleton instance of this class
	*/
	private static IMessageProcessor instance = new UserServiceMessageProcessor();

	/**
	*	Factory for generating message processors
	*/
	private IMessageProcessorFactory mPF = MessageProcessorFactory.getInstance();

	/**
	*	Private constructor for class
	*/
	private UserServiceMessageProcessor(){
	}

	/**
	*  Returns singleton instance of this class
	*
	*  @returns instance - instance of this clas 
	*/
	public static IMessageProcessor getInstance()  {

		return UserServiceMessageProcessor.instance;
	}


	/**
	 * This processes the received message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	public void processMessage(Message message) throws IOException {	

		if (message.getContentType().equals(UserServiceCommands.LOGIN.label)) {
			processLogin(message);

		} else if (message.getContentType().equals(UserServiceCommands.USER_CREATE.label)) {
			processUserCreation(message); 
		}

	}


	/**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - True if the message was sent to the user services
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		return message.getType().contentEquals(MessageAddresses.USER_SERVICE.label);
	}


	/**
	 * This processes the login message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processLogin(Message message) throws IOException {	
		Message response;
		String userName = message.getContent();
		Optional<User> user = accountService.findUserByName(userName);

		if (!user.isPresent()) {
			response = generateResponseMessage(message.getFrom(),
					UserServiceCommands.LOGIN.label + " " +
							UserServiceCommands.FAILURE_RESPONSE.label); 
		}
		else {

			IMessageProcessor.sendMessage(generateSessionLoginRequest(message.getFrom(), userName));
			response = generateResponseMessage(userName,
					UserServiceCommands.LOGIN.label + " " +
							UserServiceCommands.SUCCESS_RESPONSE.label);
		}

		IMessageProcessor.sendMessage(response);

	}


	/**
	 * This processes the user create message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processUserCreation(Message message) throws IOException {	
		Message response;

		String userName = message.getContent();

		Optional<User> user = accountService.findUserByName(userName);

		if (!user.isPresent()) {

			accountService.addUser(new User(userName));

			response = generateResponseMessage(message.getFrom(),
					UserServiceCommands.USER_CREATE.label + " " +
							UserServiceCommands.SUCCESS_RESPONSE.label);
		}
		else {
			response = generateResponseMessage(message.getFrom(),
					UserServiceCommands.USER_CREATE.label + " " +
							UserServiceCommands.FAILURE_RESPONSE.label);    
		}

		IMessageProcessor.sendMessage(response);	
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

	/**
	 * This generates a login request to send to Session Service
	 * 
	 * @param userID - current user ID
	 * @param loginName - user Login Name
	 */
	private Message generateSessionLoginRequest(String userID, String loginName) {
		return Message.messageBuilder()
				.setFrom(MessageAddresses.USER_SERVICE.label)
				.setTo(userID)
				.setType(MessageAddresses.SESSION_SERVICE.label)
				.setContentType(SessionServiceCommands.LOGIN.label)
				.setMessageContent(loginName)
				.build();
	}





}
