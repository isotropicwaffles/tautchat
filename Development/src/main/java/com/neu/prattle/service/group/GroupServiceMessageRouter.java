package com.neu.prattle.service.group;

import com.neu.prattle.messaging.AbstractMessageRouter;
import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.messaging.MessageProcessorFactory;
import com.neu.prattle.messaging.TypeOfMessageProcessor;
import com.neu.prattle.model.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a general message processor it will route messages to sub processorss
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/11/2019
 */
public class GroupServiceMessageRouter extends AbstractMessageRouter {

  /**
   * An instance of this message processor
   */
  private static IMessageProcessor instance = new GroupServiceMessageRouter();

  /**
   * Constructor for object
   */
  private GroupServiceMessageRouter() {

    List<IMessageProcessor> subMessageProcessors = new ArrayList<>();


    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GROUP_SERVICE_GROUP_MANAGEMENT_PROCESSOR));
    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GROUP_SERVICE_GROUP_QUERY_PROCESSOR));


    super.setSubMessageProcessors(subMessageProcessors);

  }

  /**
   * Returns an instance of this message processor
   *
   * @returns instance - an instance of this object
   */
  public static IMessageProcessor getInstance() {

    return GroupServiceMessageRouter.instance;
  }

  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @returns boolean - Returns true if it can process message
   */
  @Override
  public boolean canProcessMessage(Message message) {

    return message.getType().contentEquals(MessageAddresses.GROUP_SERVICE.label)
            && super.canProcessMessage(message);

  }


}
