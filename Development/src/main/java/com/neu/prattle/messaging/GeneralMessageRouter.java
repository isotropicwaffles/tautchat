package com.neu.prattle.messaging;

import java.util.ArrayList;
import java.util.List;


/**
 * This is a general message process router that routes messages to sub messsage processors
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GeneralMessageRouter extends AbstractMessageRouter {

  /**
   * The instance of this router
   */
  private static IMessageProcessor instance = new GeneralMessageRouter();

  /**
   * Private constructor of router
   */
  private GeneralMessageRouter() {

    List<IMessageProcessor> subMessageProcessors = new ArrayList<>();

    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.SYSTEM_MESSAGE_PROCESSOR));
    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GROUP_MESSAGE_PROCESSOR));
    subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.OUTGOING_MESSAGE_PROCESSOR));


    super.setSubMessageProcessors(subMessageProcessors);

  }

  /**
   * Returns instance of this router
   * 
   * @return an instance of this message processor
   */
  public static IMessageProcessor getInstance() {

    return GeneralMessageRouter.instance;
  }


}
