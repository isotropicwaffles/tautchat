package com.neu.prattle.websocket;


import java.io.IOException;
import java.util.Optional;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;



/**
*
* This is a user service message processor
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public class SessionServiceMessageProcessor implements IMessageProcessor {
    	
    //(TODO) COMPLETE COMMITS
	private static IMessageProcessor instance = new SessionServiceMessageProcessor();
	
	
    //(TODO) COMPLETE COMMITS
	public static IMessageProcessor getInstance()  {
			
		return SessionServiceMessageProcessor.instance;
	}

	
	 /**
	 * This processes the received message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	@Override
	public void processMessage(Message message) throws IOException {	
		
		  if (message.getContent().contains(SessionServiceCommands.LOGIN.label)) {
			  processLogin(message);
		  
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
		return message.getTo().contentEquals(MessageAddresses.SESSION_SERVICE.label);
	}
	
	
	 /**
	 * This processes the login message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processLogin(Message message) {	
		String userName = message.getContent().substring((SessionServiceCommands.LOGIN.label).length() + 1);

  		ChatEndpoint.userLogin(userName, message.getFrom());
	}



	
}
