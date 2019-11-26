package com.neu.prattle.messaging;


/**
 * This is a message processor factory interface for generating message processors
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/03/2019
 */
public interface IMessageProcessorFactory {

  /**
   * Get an IMessageProcessor instance of specified by given TypeOfMessageProcessor
   *
   * @param messageProcessor: type of message processor desired
   * @returns iMessageProcessor: an instance o the requested messageProcessor
   */
  IMessageProcessor getInstanceOf(TypeOfMessageProcessor messageProcessor);
}
