package com.neu.prattle.messaging;


/**
*
* This is a message processor factory interface for generating message processors
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/03/2019
*/
public interface IMessageProcessorFactory {

	
	/**
	* Generates a specific type of message processor from a more general Message processor template.
	* 
	* @param TypeOfMessageProcessor messageRouter - the type of message router desired
	*
	* @return IMessageProcessor - A message router of the input TypeOfMessageProcessor 
	*/
	public IMessageProcessor createMessageProcessor(TypeOfMessageProcessor messageProcessor);
}
