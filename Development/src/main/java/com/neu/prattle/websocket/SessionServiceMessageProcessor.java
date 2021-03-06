package com.neu.prattle.websocket;


import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;

import java.io.IOException;


/**
 * This is a user service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class SessionServiceMessageProcessor implements IMessageProcessor {

  /**
   * The instance of this message processor
   */
  private static IMessageProcessor instance = new SessionServiceMessageProcessor();


  /**
   * Returns the instance of this message processor
   *
   * @return instance - an instance of this message processor
   */
  public static IMessageProcessor getInstance() {

    return SessionServiceMessageProcessor.instance;
  }


  /**
   * This processes the received message
   *
   * @param message - a message to be processed
   */
  @Override
  public void processMessage(Message message) throws IOException {

    if (message.getContentType().equals(SessionServiceCommands.LOGIN.label)) {
      processLogin(message);

    }

  }


  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @return boolean - True if the message was sent to the user services
   */
  @Override
  public boolean canProcessMessage(Message message) {
    return message.getType().contentEquals(MessageAddresses.SESSION_SERVICE.label);
  }


  /**
   * This processes the login message
   *
   * @param message - a message to be processed
   */
  private void processLogin(Message message) {
    String userName = message.getContent();

    ChatEndpoint.userLogin(userName, message.getTo());
  }


}
