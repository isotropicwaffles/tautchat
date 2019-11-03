package com.neu.prattle.messaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neu.prattle.model.Message;


/**
*
* This is a general message process router that routes messages to sub messsage processors
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public class GeneralMessageRouter implements IMessageProcessor {

	List<IMessageProcessor> subMessageProcessors = new ArrayList<>();
	
	/**
	 * This constructor sets the correct secondary message processors
	 * 
	 */
	GeneralMessageRouter(List<IMessageProcessor> subMessageProcessors){
		
		this.subMessageProcessors = subMessageProcessors;
	}
	
	
	 /**
	 * This processes the message and sends the message to the appropreiate message processor
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	@Override
	public void processMessage(Message message) throws IOException {	
		
		// iterating over an subprocess array 
        for (IMessageProcessor subMessageProcessor: subMessageProcessors) { 
  
        	if (subMessageProcessor.canProcessMessage(message)) {
        		subMessageProcessor.processMessage(message);
        		break;
        	}
        } 

	}
	
	 /**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - Returns true if it can process message
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		
		// iterating over an subprocess array 
        for (IMessageProcessor subMessageProcessor: subMessageProcessors) { 
  
        	if (subMessageProcessor.canProcessMessage(message)) {
        		return true;
        	}
        } 
		return false;
	}

}
