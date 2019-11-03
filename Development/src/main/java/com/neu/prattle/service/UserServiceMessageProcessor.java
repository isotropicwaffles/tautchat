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


/**
*
* This is a user service message processor
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public class UserServiceMessageProcessor implements IMessageProcessor {
    
	private static UserService accountService = UserServiceImpl.getInstance();
	final IMessageProcessorFactory messageProcessFactory = new MessageProcessorFactory();
	IMessageProcessor generalMessageProcessor;

	
	/**
	 * This constructor sets the correct secondary message processors
	 * 
	 */
	public UserServiceMessageProcessor(){
		
		generalMessageProcessor = messageProcessFactory.createMessageProcessor(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR);

	}
	
	 /**
	 * This processes the received message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	public void processMessage(Message message) throws IOException {	
        
        if (message.getContent().contains(UserServiceCommands.LOGIN.label)) {
        	processLogin(message);

        } else if (message.getContent().contains(UserServiceCommands.USERCREATE.label)) {
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
		return message.getTo().contentEquals(MessageAddresses.USERSERVICE.label);
	}
	
	
	 /**
	 * This processes the login message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processLogin(Message message) throws IOException {	
		Message response;
		String userName = message.getContent().substring((UserServiceCommands.LOGIN.label).length() + 1);
    	Optional<User> user = accountService.findUserByName(userName);
    	
    	if (!user.isPresent()) {
            response = generateResponseMessage(message.getFrom(),
            	UserServiceCommands.LOGIN.label + " " +
    			UserServiceCommands.FAILURE_RESPONSE.label); 
         	}
    	else {
    		
    		//ChatEndpoint.userLogin(userName, message.getFrom());
            response = generateResponseMessage(userName,
            	UserServiceCommands.LOGIN.label + " " +
    			UserServiceCommands.SUCCESS_RESPONSE.label);
            }
    	
    	if(generalMessageProcessor.canProcessMessage(response)) {
    		generalMessageProcessor.processMessage(response);
    	}

	}


	 /**
	 * This processes the user create message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processUserCreation(Message message) throws IOException {	
		Message response;

		String userName = message.getContent().substring((UserServiceCommands.USERCREATE.label).length() + 1);

    	Optional<User> user = accountService.findUserByName(userName);
    	
    	if (!user.isPresent()) {
    		
    		accountService.addUser(new User(userName));
    		
            response = generateResponseMessage(message.getFrom(),
            	UserServiceCommands.USERCREATE.label + " " +
        		UserServiceCommands.SUCCESS_RESPONSE.label);
        }
    	else {
            response = generateResponseMessage(message.getFrom(),
        		UserServiceCommands.USERCREATE.label + " " +
        		UserServiceCommands.FAILURE_RESPONSE.label);    
        }
    	
    	if(generalMessageProcessor.canProcessMessage(response)) {
    		generalMessageProcessor.processMessage(response);
    	}	}
	
	 /**
	 * This generates a response message from the user servicese
	 * 
	 * @param receiver - to whom the message should be sent
	 * @param response - the response that is being sent
	 */
	private static Message generateResponseMessage(String receiver, String response) {
		return Message.messageBuilder()
		        .setFrom(MessageAddresses.USERSERVICE.label)
		        .setTo(receiver)
				.setMessageContent(response)
		        .build();
	}


	
}
