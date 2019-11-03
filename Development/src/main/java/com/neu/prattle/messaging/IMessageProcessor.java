package com.neu.prattle.messaging;

import java.io.IOException;

import com.neu.prattle.model.Message;

/**
*
* This is a message processor interface
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/03/2019
*/
public interface IMessageProcessor {

	 /**
	 * This processes the given message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	public void processMessage(Message message) throws IOException;
	
	
	 /**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - true if message can be processed by this processor
	 */
	public boolean canProcessMessage(Message message);

}
