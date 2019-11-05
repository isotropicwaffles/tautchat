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

	//(TODO) COMPLETE COMMITS
	private static IMessageProcessor instance = new OutgoingMessageProcessor();

	//(TODO) COMPLETE COMMITS
	private OutgoingMessageProcessor(){

	}

    //(TODO) COMPLETE COMMITS
	public static IMessageProcessor getInstance()  {

		return OutgoingMessageProcessor.instance;
	}


	/**
	 * This processes the message and sends the message to the appropreiate message processor
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	@Override
	public void processMessage(Message message) throws IOException {	

		if (message.getTo().equals(MessageAddresses.BROADCAST_MESSAGE.label)) {
			ChatEndpoint.broadcast(message);
		}
		else {
			ChatEndpoint.directedMessage(message);
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
		return true;
	}


}
