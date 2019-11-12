package com.neu.prattle.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.Optional;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.group.GroupService;
import com.neu.prattle.service.group.GroupServiceImpl;


/**
 *
 * This is a group message processor that sends the message to the appropriate function
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GroupMessageProcessor implements IMessageProcessor {


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
		return 	message.getType().equals(MessageAddresses.GROUP_MESSAGE.label);
	}


	/**
	 * Processes Message to group and sends to all group users and subgroups
	 * 
	 * @param message - a message to be processed
	 */
	private void processGroupMessage(Message message) {	


		Optional<Group> group = GroupServiceImpl.getInstance().findGroupByName(message.getTo());

		if (group.isPresent()) {

			Set<User> groupUsers = group.get().getMembers();
			Set<Group> groupSubGroups = group.get().getSubGroups();

			// Send message to group users
			for (User user : groupUsers) {
				IMessageProcessor.sendMessage(Message.messageBuilder()
						.setFrom(message.getFrom())
						.setTo(user.getName())
						.setType(MessageAddresses.DIRECT_MESSAGE.label)
						.setContentType(MessageAddresses.GROUP_MESSAGE.label)
						.setMessageContent(message.getContent())
						.setAdditionalInfo(message.getTo())
						.setDateSent(message.getDateSent())
						.build());

			}		

			// Send message to subgroups
			for (Group subGroup : groupSubGroups) {
				IMessageProcessor.sendMessage(Message.messageBuilder()
						.setFrom(message.getFrom())
						.setTo(subGroup.getName())
						.setType(MessageAddresses.GROUP_MESSAGE.label)
						.setContentType(MessageAddresses.GROUP_MESSAGE.label)
						.setMessageContent(message.getContent())
						.setAdditionalInfo(message.getAdditionalInfo())
						.setDateSent(message.getDateSent())
						.build());
			}
		}

	}





}
