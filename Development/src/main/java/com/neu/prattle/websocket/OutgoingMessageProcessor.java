package com.neu.prattle.websocket;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;

import java.io.IOException;


/**
 * This is a general message processor that sends the message to the appropriate function
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class OutgoingMessageProcessor implements IMessageProcessor {

  /**
   * An Instance of this object
   */
  private static IMessageProcessor instance = new OutgoingMessageProcessor();

  /**
   * Constructor for this object
   */
  private OutgoingMessageProcessor() {

  }

  /**
   * Returns instance of this object
   *
   * @return instance : an instance of this message processor
   */
  public static IMessageProcessor getInstance() {

    return OutgoingMessageProcessor.instance;
  }


  /**
   * This processes the message and sends the message to the appropreiate message processor
   *
   * @param message - a message to be processed
   */
  @Override
  public void processMessage(Message message) throws IOException {
	 
	//Process sentiment before sending
	  
	if (message.getType().equals(MessageAddresses.BROADCAST_MESSAGE.label)) {
      ChatEndpoint.broadcast(message);
    } else if (message.getType().equals(MessageAddresses.DIRECT_MESSAGE.label)) {
      ChatEndpoint.directedMessage(message);
    }

  }

  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @returns boolean - Always true at this time since it will be expected to send direct message
   * and be the last evaluated processor.
   */
  @Override
  public boolean canProcessMessage(Message message) {
    return message.getType().equals(MessageAddresses.BROADCAST_MESSAGE.label) ||
            message.getType().equals(MessageAddresses.DIRECT_MESSAGE.label);
  }


}
