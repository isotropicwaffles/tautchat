package com.neu.prattle.messaging;

import com.neu.prattle.service.UserServiceMessageProcessor;
import com.neu.prattle.websocket.OutgoingMessageProcessor;
import com.neu.prattle.websocket.SessionServiceMessageProcessor;

/**
 * Factory class for creating general and user service message processors
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/03/2019
 */
public class MessageProcessorFactory implements IMessageProcessorFactory {


	/**
	* An instance of this message factory
	*/
	private static MessageProcessorFactory instance = new MessageProcessorFactory();

	/**
	* Private constructor
	*/	
	private MessageProcessorFactory() {
		
	}
	
	/**
	* Returns an instance of this message factory
	*/	
	public static MessageProcessorFactory getInstance() {
	
		return MessageProcessorFactory.instance;
	}
	
	/**
	*	Get an IMessageProcessor instance of specified by given TypeOfMessageProcessor
	*	
	*	@param messageProcessor: type of message processor desired
	*	@returns iMessageProcessor: an instance o the requested messageProcessor
	*/
	@Override
	public IMessageProcessor getInstanceOf(TypeOfMessageProcessor messageProcessor) {

		IMessageProcessor createdProcessor = null;

		switch(messageProcessor)
		{
		case GENERAL_MESSAGE_PROCESSOR:

			createdProcessor = GeneralMessageRouter.getInstance();
			break; 

		case SYSTEM_MESSAGE_PROCESSOR:

			createdProcessor = SystemMessageRouter.getInstance();

			break; 

		case OUTGOING_MESSAGE_PROCESSOR :

			createdProcessor =  OutgoingMessageProcessor.getInstance();
			break;	

		case USER_SERVICE_PROCESSOR :

			createdProcessor = UserServiceMessageProcessor.getInstance();

			break;
		
		case SESSION_SERVICE_MESSAGE_PROCESSOR :

			createdProcessor = SessionServiceMessageProcessor.getInstance();

			break;

		default : 
			throw new IllegalArgumentException("Invalid Message Processor");
		}

		return createdProcessor;

	}
}
