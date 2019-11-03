package com.neu.prattle.messaging;

import java.util.ArrayList;
import java.util.List;

import com.neu.prattle.service.UserServiceMessageProcessor;
import com.neu.prattle.websocket.OutgoingMessageProcessor;

/**
* Factory class for creating general and user service message processors
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/03/2019
*/
public class MessageProcessorFactory implements IMessageProcessorFactory {


	/**
	* Generates a specific type of message processor from a more general Message processor template.
	* Options are: GENERAL_MESSAGE_PROCESSOR, SYSTEM_MESSAGE_PROCESSOR, OUTGOING_MESSAGE_PROCESSOR, and USER_SERVICE_PROCESSOR
	* 
	* @param TypeOfMessageProcessor messageRouter - the type of message router desired
	*
	* @return IMessageProcessor - A message router of the input TypeOfMessageProcessor 
	* @throws IllegalArgumentException if the specified TypeOfTravel is not valid
	*/
	@Override
	public IMessageProcessor createMessageProcessor(TypeOfMessageProcessor messageProcessor) {
		
		IMessageProcessor createdProcessor = null;
		
		switch(messageProcessor)
		{
			case GENERAL_MESSAGE_PROCESSOR:
				
				List<IMessageProcessor> generalMessageSubMessageProcessors = new ArrayList<>();
				
				generalMessageSubMessageProcessors.add(this.createMessageProcessor(TypeOfMessageProcessor.SYSTEM_MESSAGE_PROCESSOR));
				generalMessageSubMessageProcessors.add(this.createMessageProcessor(TypeOfMessageProcessor.OUTGOING_MESSAGE_PROCESSOR));
	
				createdProcessor = new GeneralMessageRouter(generalMessageSubMessageProcessors);
				break; 
			
			case SYSTEM_MESSAGE_PROCESSOR:
				
				List<IMessageProcessor> systemMessageSubMessageProcessors = new ArrayList<>();
				
				systemMessageSubMessageProcessors.add(this.createMessageProcessor(TypeOfMessageProcessor.USER_SERVICE_PROCESSOR));
	
				createdProcessor = new GeneralMessageRouter(systemMessageSubMessageProcessors);
				break; 
	
			case OUTGOING_MESSAGE_PROCESSOR :
				createdProcessor = new OutgoingMessageProcessor();
				break;	
				
			case USER_SERVICE_PROCESSOR :
				createdProcessor = new UserServiceMessageProcessor();
				break;
		
			default : 
				throw new IllegalArgumentException("Invalid Message Processor");
			}
		
		return createdProcessor;

	}
}
