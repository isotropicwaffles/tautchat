package com.neu.prattle.messaging;

import java.util.ArrayList;
import java.util.List;


/**
*
* This is a general message process router that routes messages to sub messsage processors
*
* @author Richard Alexander Showalter-Bucher
* @version 1.0 11/01/2019
*/
public class SystemMessageRouter extends AbstractMessageRouter {

	//(TODO) COMPLETE COMMITS
	private static IMessageProcessor instance = new SystemMessageRouter();
	
	//(TODO) COMPLETE COMMITS
	private SystemMessageRouter(){

		List<IMessageProcessor> subMessageProcessors = new ArrayList<>();

	
		subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.USER_SERVICE_PROCESSOR));
		subMessageProcessors.add(MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.SESSION_SERVICE_MESSAGE_PROCESSOR));

		
		super.setSubMessageProcessors(subMessageProcessors);

	}
	
	//(TODO) COMPLETE COMMITS
	public static IMessageProcessor getInstance()  {

		return SystemMessageRouter.instance;
	}

	

}
