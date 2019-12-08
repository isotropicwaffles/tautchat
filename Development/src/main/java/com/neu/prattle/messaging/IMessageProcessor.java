package com.neu.prattle.messaging;

import com.neu.prattle.model.Message;

import java.io.IOException;

/**
 * This is a message processor interface
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/03/2019
 */
public interface IMessageProcessor {


  /**
   * Sends message to general router
   *
   * @param response - a message response being sent from the user service
   * @return boolean that represent whether the message was sent successfully
   */
  static boolean sendMessage(Message response) {

    boolean success = false;
    try {
      if (MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR).canProcessMessage(response)) {

        MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR).processMessage(response);
        success = true;

      }

      return success;

    } catch (IOException e) {
      return false;
    }
  }

  /**
   * This processes the given message
   *
   * @param message - a message to be processed
   * @throws IOException - an ioexception
   */
  void processMessage(Message message) throws IOException;

  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @return boolean - true if message can be processed by this processor
   */
  boolean canProcessMessage(Message message);
}
