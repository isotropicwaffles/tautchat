package com.neu.prattle.messaging;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a system message process router that routes messages to sub messsage processors
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class SystemMessageRouter extends AbstractMessageRouter {

  /**
   * An instance of this message processor
   */
  private static IMessageProcessor instance = new SystemMessageRouter();

  /**
   * Constructor for object
   */
  private SystemMessageRouter() {

    List<IMessageProcessor> subMessageProcessors = new ArrayList<>();


    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.USER_SERVICE_PROCESSOR));
    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GROUP_SERVICE_ROUTER));
    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.SESSION_SERVICE_MESSAGE_PROCESSOR));


    super.setSubMessageProcessors(subMessageProcessors);

  }

  /**
   * Returns an instance of this message processor
   *
   * @return instance - an instance of this object
   */
  public static IMessageProcessor getInstance() {

    return SystemMessageRouter.instance;
  }


}
