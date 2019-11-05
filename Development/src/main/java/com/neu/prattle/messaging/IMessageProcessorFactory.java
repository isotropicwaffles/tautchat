package com.neu.prattle.messaging;


/**
*
* This is a message processor factory interface for generating message processors
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/03/2019
*/
public interface IMessageProcessorFactory {

	//(TODO) COMPLETE COMMITS
	public IMessageProcessor getInstanceOf(TypeOfMessageProcessor messageProcessor);
}
