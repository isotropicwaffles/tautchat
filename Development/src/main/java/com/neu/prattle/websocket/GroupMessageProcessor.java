package com.neu.prattle.websocket;

import java.io.IOException;
import java.util.List;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;


/**
 *
 * This is a group message processor that sends the message to the appropriate function
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GroupMessageProcessor implements IMessageProcessor {

	
	/**
	*	Group Service instance
	*/
	//private static GroupService groupAccountService = UserServiceImpl.getInstance();



	/**
	*	An Instance of this object
	*/
	private static IMessageProcessor instance = new GroupMessageProcessor();

	/**
	*   Constructor for this object 
	*/ 
	private GroupMessageProcessor(){

	}

    /**
	*   Returns instance of this object
	*
	*	@return instance : an instance of this message processor
	*/ 
	public static IMessageProcessor getInstance()  {

		return GroupMessageProcessor.instance;
	}


	/**
	 * This processes the message and sends the message to the appropreiate message processor
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	@Override
	public void processMessage(Message message) throws IOException {	

		if (message.getType().equals(MessageAddresses.GROUP_MESSAGE.label)) {
			processGroupMessage(message);
		}


	}

	/**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - Always true at this time since it will be expected to send direct message and be the last evaluted processor.
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		return true;
	}
	
	
	/**
	 * Processes Message to group and sends to all group users and subgroups
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	private void processGroupMessage(Message message) throws IOException {	
		
		List<User> groupUsers;
		List<Group> groupSubGroups;
		
		// Send message to group users
		for (User user : groupUsers) {
			IMessageProcessor.sendMessage(Message.messageBuilder()
						.setFrom(message.getFrom())
						.setTo(user.getName())
						.setType(MessageAddresses.DIRECT_MESSAGE.label)
						.setContentType(message.getContentType())
						.setMessageContent(message.getContent())
						.setAdditionalInfo(message.getTo())
						.setDateSent(message.getDateSent())
						.build());
			
		}		
		
		// Send message to subgroups
		for (Group group : groupSubGroups) {
			IMessageProcessor.sendMessage(Message.messageBuilder()
					.setFrom(message.getTo())
					.setTo(group.getName())
					.setType(MessageAddresses.GROUP_MESSAGE.label)
					.setContentType(message.getContentType())
					.setMessageContent(message.getContent())
					.setAdditionalInfo(message.getAdditionalInfo())
					.setDateSent(message.getDateSent())
					.build());
		}

	}
	




}
