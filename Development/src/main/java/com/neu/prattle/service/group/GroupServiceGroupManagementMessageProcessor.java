package com.neu.prattle.service.group;

import java.io.IOException;
import java.util.Optional;

import com.neu.prattle.messaging.GenericMessageResponses;
import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserService;
import com.neu.prattle.service.user.UserServiceImpl;


/**
 *
 * This is a Groiup service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 * @param <E>
 */
public class GroupServiceGroupManagementMessageProcessor implements IMessageProcessor {

	/**
	 *	User Service instance
	 */
	private static UserService userAccountService = UserServiceImpl.getInstance();


	/**
	 *	Group Service instance
	 */
	private static GroupService groupAccountService = GroupServiceImpl.getInstance();

	/**
	 *	Singleton instance of this class
	 */
	private static IMessageProcessor instance = new GroupServiceGroupManagementMessageProcessor();

	/**
	 *	Private constructor for class
	 */
	private GroupServiceGroupManagementMessageProcessor(){
	}

	/**
	 *  Returns singleton instance of this class
	 *
	 *  @returns instance - instance of this clas 
	 */
	public static IMessageProcessor getInstance()  {

		return GroupServiceGroupManagementMessageProcessor.instance;
	}


	/**
	 * Evaluates whether the message can be processed by this processor
	 * 
	 * @param message - a message to be processed
	 * @returns boolean - True if the message was sent to the user services
	 */
	@Override
	public boolean canProcessMessage(Message message) {
		return message.getContentType().contentEquals(GroupServiceCommands.GROUP_CREATE.label) || 
			   message.getContentType().contentEquals(GroupServiceCommands.GROUP_DELETE.label) || 
			   message.getContentType().contentEquals(GroupServiceCommands.APPROVE_ADD_USER.label) ||
			   message.getContentType().contentEquals(GroupServiceCommands.APPROVE_ADD_GROUP.label) ||
			   message.getContentType().contentEquals(GroupServiceCommands.REMOVE_USER.label) ||
			   message.getContentType().contentEquals(GroupServiceCommands.REMOVE_GROUP.label) ||
			   message.getContentType().contentEquals(GroupServiceCommands.REQUEST_ADD_USER.label) ||
			   message.getContentType().contentEquals(GroupServiceCommands.REQUEST_ADD_GROUP.label);
	}



	/**
	 * This processes the received message
	 * 
	 * @param message - a message to be processed
	 * @throws IOException 
	 */
	public void processMessage(Message message) throws IOException {	

		Message response;
		
		try {
			if (message.getContentType().equals(GroupServiceCommands.GROUP_CREATE.label)) {

				response = processGroupCreate(message);

			} else if (message.getContentType().equals(GroupServiceCommands.GROUP_DELETE.label)) {

				response = processGroupDelete(message);

			} else if (message.getContentType().equals(GroupServiceCommands.APPROVE_ADD_USER.label)) {

				response = processApproveAddUser(message);

			} else if (message.getContentType().equals(GroupServiceCommands.APPROVE_ADD_GROUP.label)) {

				response = processApproveAddSubGroup(message);

			} else if (message.getContentType().equals(GroupServiceCommands.REMOVE_USER.label)) {

				response = processRemoveUser(message);

			} else if (message.getContentType().equals(GroupServiceCommands.REMOVE_GROUP.label)) {

				response = processRemoveSubGroup(message);

			} else if (message.getContentType().equals(GroupServiceCommands.REQUEST_ADD_USER.label)) {

				response = processRequestAddUser(message);

			} else if (message.getContentType().equals(GroupServiceCommands.REQUEST_ADD_GROUP.label)) {

				response = processRequestAddSubGroup(message);
				
			} else { 
				response = generateResponseMessage(message.getContentType(),
						GenericMessageResponses.UNKNOWN_COMMAND.label);
				
			}
			
		} catch (Exception e) {

			response = generateResponseMessage(message.getFrom(), e.getMessage());

		}
		
		response.setTo(message.getFrom());
		
		IMessageProcessor.sendMessage(response);

	}
	


	/**
	 * Adds group requested in message. Associates user sending the request as the 
	 * moderator for the group.
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 * @throws IOException 
	 */
	private Message processGroupCreate(Message message) throws IOException {	

		Message response;
		
		User moderator =  userAccountService.protectedfindUserByName(message.getFrom());

		Group newGroup = new Group(moderator);

		Optional<Group> existGroup = groupAccountService.findGroupByName(message.getContent()); 
		
		if (! existGroup.isPresent()) {
			groupAccountService.addGroup(newGroup);
			
			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;


	}

	/**
	 * Deletes group requested in message
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processGroupDelete(Message message) {	
		
		Message response;

		Group existGroup = groupAccountService.protectedFindGroupByName(message.getContent()); 

		groupAccountService.deleteGroup(existGroup);
		
		response = generateSuccessResponseMessage(message);
		
		return response;

	}


	/**
	 * The function submits the request to add of a user to the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processRequestAddUser(Message message) {	
		
		Message response;

		Group existGroup = groupAccountService.protectedFindGroupByName(message.getTo()); 
		User userToAdd = userAccountService.protectedfindUserByName(message.getContent()); 

		if (existGroup.hasMember(userToAdd)) {

			existGroup.addUser(null, userToAdd);
			
			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;
	}

	/**
	 * The function submits the request to add of a subgroup to the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processRequestAddSubGroup(Message message)  {	

		Message response;

		Group group = groupAccountService.protectedFindGroupByName(message.getTo()); 
		Group groupToAdd = groupAccountService.protectedFindGroupByName(message.getContent()); 

		if (!group.hasSubGroup(groupToAdd)) {

			group.addSubgroup(null, groupToAdd);

			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;


	}

	/**
	 * The function processes an approved add of a user to the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processApproveAddUser(Message message) {	

		Message response;

		Group existGroup = groupAccountService.protectedFindGroupByName(message.getTo()); 
		User potentialModerator = userAccountService.protectedfindUserByName(message.getFrom()); 
		User userToAdd = userAccountService.protectedfindUserByName(message.getContent());


		if (existGroup.authenticateAsMod(potentialModerator) &&
				!existGroup.hasMember(userToAdd)) {

			existGroup.addUser(potentialModerator, userToAdd);
		
			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;


	}

	/**
	 * The function processes an approved add of a subgroup to the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processApproveAddSubGroup(Message message) {	

		Message response;

		Group group = groupAccountService.protectedFindGroupByName(message.getTo()); 
		User potentialModerator = userAccountService.protectedfindUserByName(message.getFrom()); 
		Group groupToAdd =groupAccountService.protectedFindGroupByName(message.getContent()); 


		if (group.authenticateAsMod(potentialModerator) && 
				!group.hasSubGroup(groupToAdd)) {

			group.addSubgroup(potentialModerator, groupToAdd);						
			
			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;


	}

	/**
	 * The function processes an removal of user if they exist in the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processRemoveUser(Message message) {	

		Message response;

		Group group = groupAccountService.protectedFindGroupByName(message.getTo()); 
		User userRequesting = userAccountService.protectedfindUserByName(message.getFrom()); 
		User userToBeRemoved = userAccountService.protectedfindUserByName(message.getContent()); 


		if ( group.authenticateAsMod(userRequesting) || 
				userRequesting.equals(userToBeRemoved)) {

			group.removeUser(userToBeRemoved);						

			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		
		return response;

	}

	/**
	 * The function processes an removal of subgroup if they exist in the group
	 * 
	 * @param message - a message to be processed
	 * 
	 * @return a message in reponse to this request
	 */
	private Message processRemoveSubGroup(Message message) {	
		
		Message response;

		Group group = groupAccountService.protectedFindGroupByName(message.getTo()); 
		User userRequesting = userAccountService.protectedfindUserByName(message.getFrom()); 
		Group groupToBeRemoved = groupAccountService.protectedFindGroupByName(message.getContent()); 

		if (group.authenticateAsMod(userRequesting)  || 
				groupToBeRemoved.authenticateAsMod(userRequesting)) {
			group.removeSubgroup(userRequesting, groupToBeRemoved);						

			response = generateSuccessResponseMessage(message);
		}else {
			response = generateFailureResponseMessage(message);
		}
		return response;

	}


	/**
	 * This generates a response message from the group services
	 * 
	 * @param contentType - the type of content of this message
	 * @param response - the response that is being sent
	 */
	private Message generateResponseMessage(String contentType, String response) {
		return Message.messageBuilder()
				.setFrom(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(contentType)
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(response)
				.build();
	}

	/**
	 * This generates a success response message from the group services
	 * 
	 * @param message - the original message that is being processed
	 * @return a successful Response
	 */
	private Message generateSuccessResponseMessage(Message message) {
		return Message.messageBuilder()
				.setFrom(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(message.getContentType())
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(GenericMessageResponses.SUCCESS_RESPONSE.label)
				.build();
	}
	
	/**
	 * This generates a failure response message from the group services
	 * 
	 * @param message - the original message that is being processed
	 * @return a failure Response
	 */
	private Message generateFailureResponseMessage(Message message) {
		return Message.messageBuilder()
				.setFrom(MessageAddresses.GROUP_SERVICE.label)
				.setContentType(message.getContentType())
				.setType(MessageAddresses.DIRECT_MESSAGE.label)
				.setMessageContent(GenericMessageResponses.FAILURE_RESPONSE.label)
				.build();
	}



}
