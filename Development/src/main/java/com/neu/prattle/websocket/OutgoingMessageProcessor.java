package com.neu.prattle.websocket;

import java.io.IOException;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;


/**
*
* This is a general message processor that sends the message to the appropriate function
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public class OutgoingMessageProcessor implements IMessageProcessor {
	
	/**
	 * This constructor does nothing
	 * 
	 */
	public OutgoingMessageProcessor(){
				
	}
	
	
	 /**
	 * This processes the message and sends the message to the appropreiate message processor
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	@Override
	public void processMessage(Message message) throws IOException {	
        
        if (message.getTo().contentEquals(MessageAddresses.BROADCAST.label)) {
        	ChatEndpoint.broadcast(message);
        }
        else {
        	//ChatEndpoint.directedMessage(message);
        }

	}
	
	 /**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - Always true at this time since it will be expected to send direct message and be the last evaluted processor.
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		return message.getTo().contentEquals(MessageAddresses.BROADCAST.label) || true;
	}

}
