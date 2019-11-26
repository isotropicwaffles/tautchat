package com.neu.prattle.messaging;

import com.neu.prattle.model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This is an message process router that routes messages to sub message processors
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public abstract class AbstractMessageRouter implements IMessageProcessor {

  /**
   * A list of subprocesses of this router
   */
  private List<IMessageProcessor> subMessageProcessors = new ArrayList<>();

  /**
   * Sets the sub processes of this router
   *
   * @param subMessageProcessors: list of message processors to associate with router
   */
  protected void setSubMessageProcessors(List<IMessageProcessor> subMessageProcessors) {

    this.subMessageProcessors = subMessageProcessors;

  }

  /**
   * This processes the message and sends the message to the appropriate message processor
   *
   * @param message - a message to be processed
   */
  @Override
  public void processMessage(Message message) throws IOException {

    // iterating over an subprocess array
    for (IMessageProcessor subMessageProcessor : subMessageProcessors) {

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
    for (IMessageProcessor subMessageProcessor : subMessageProcessors) {

      if (subMessageProcessor.canProcessMessage(message)) {
        return true;
      }
    }
    return false;
  }

}
