package com.neu.prattle.messaging;


/**
 * This enumeration represents the different types of message processors available
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/03/2019
 */
public enum TypeOfMessageProcessor {

  /**
   * Represents General Message Processor
   */
  GENERAL_MESSAGE_PROCESSOR,
  /**
   * Represents Outgoing Message Processor
   */
  OUTGOING_MESSAGE_PROCESSOR,
  /**
   * Represents Group Message Processor
   */
  GROUP_MESSAGE_PROCESSOR,
  /**
   * Represents System Message Processor
   */
  SYSTEM_MESSAGE_PROCESSOR,
  /**
   * Represents Session Service Message Processor
   */
  SESSION_SERVICE_MESSAGE_PROCESSOR,
  /**
   * Represents User Service Processor
   */
  USER_SERVICE_PROCESSOR,
  /**
   * Represents Group Service Processor
   */
  GROUP_SERVICE_ROUTER,
  /**
   * Represents Group Service Management Processor
   */
  GROUP_SERVICE_GROUP_MANAGEMENT_PROCESSOR,
  /**
   * Represents Group Service Group Queries Processor
   */
  GROUP_SERVICE_GROUP_QUERY_PROCESSOR,

}
