package com.neu.prattle.service.group;

import com.neu.prattle.messaging.GenericMessageResponses;
import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserServiceImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;


/**
 * This is a Groiup service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GroupServiceGroupManagementMessageProcessor implements IMessageProcessor {

  /**
   * Singleton instance of this class
   */
  private static IMessageProcessor instance = new GroupServiceGroupManagementMessageProcessor();

  /**
   * Private constructor for class
   */
  private GroupServiceGroupManagementMessageProcessor() {
  }

  /**
   * Returns singleton instance of this class
   *
   * @returns instance - instance of this clas
   */
  public static IMessageProcessor getInstance() {

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
            message.getContentType().contentEquals(GroupServiceCommands.INVITE_USER.label) ||
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
   */
  public void processMessage(Message message) throws IOException {

    Message response;

    try {
      if (message.getContentType().equals(GroupServiceCommands.GROUP_CREATE.label)) {

        response = processGroupCreate(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GROUP_DELETE.label)) {

        response = processGroupDelete(message);

      } else if (message.getContentType().equals(GroupServiceCommands.INVITE_USER.label)) {

        response = processInviteUser(message);

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

      response = generateResponseMessage(message.getContentType(), e.getMessage());

    }

    response.setTo(message.getFrom());

    IMessageProcessor.sendMessage(response);

  }


  /**
   * Adds group requested in message. Associates user sending the request as the moderator for the
   * group.
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGroupCreate(Message message) throws IOException {

    Message response;

    User moderator = UserServiceImpl.getInstance().protectedfindUserByName(message.getFrom());

    Group newGroup = new Group(moderator);

    newGroup.setName(moderator, message.getContent());

    Optional<Group> existGroup = GroupServiceImpl.getInstance().findGroupByName(message.getContent());

    if (!existGroup.isPresent()) {
      GroupServiceImpl.getInstance().addGroup(newGroup);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;


  }

  /**
   * Deletes group requested in message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGroupDelete(Message message) {

    Message response;

    Group existGroup = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getContent());

    GroupServiceImpl.getInstance().deleteGroup(existGroup);

    response = generateSuccessResponseMessage(message);

    return response;

  }


  /**
   * The function sends an invite message to user based on a message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processInviteUser(Message message) {
    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getAdditionalInfo());
    User userToInvite = UserServiceImpl.getInstance().protectedfindUserByName(message.getContent());

    if (!group.hasMember(userToInvite)) {

      IMessageProcessor.sendMessage(generateInviteMessage(message));

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;
  }

  /**
   * The function function generates the invite message
   *
   * @param message - a message to be processed
   * @return a message in response to this request
   */
  private Message generateInviteMessage(Message message) {

    return Message.messageBuilder()
            .setFrom(message.getFrom())
            .setTo(message.getContent())
            .setContentType(message.getContentType())
            .setType(MessageAddresses.DIRECT_MESSAGE.label)
            .setMessageContent(message.getFrom() + " invites you to join the following group: " + message.getAdditionalInfo() + "!")
            .setAdditionalInfo(message.getAdditionalInfo())
            .build();

  }


  /**
   * The function submits the request to add of a user to the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processRequestAddUser(Message message) {

    Message response;

    Group existGroup = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    User userToAdd = UserServiceImpl.getInstance().protectedfindUserByName(message.getContent());

    if (!existGroup.hasMember(userToAdd)) {

      existGroup.addUser(null, userToAdd);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;
  }

  /**
   * The function submits the request to add of a subgroup to the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processRequestAddSubGroup(Message message) {

    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    Group groupToAdd = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getContent());

    if (!group.hasSubGroup(groupToAdd)) {

      group.addSubgroup(null, groupToAdd);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;


  }

  /**
   * The function processes an approved add of a user to the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processApproveAddUser(Message message) {

    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    User potentialModerator = UserServiceImpl.getInstance().protectedfindUserByName(message.getFrom());
    User userToAdd = UserServiceImpl.getInstance().protectedfindUserByName(message.getContent());

    Set<User> mods = group.getModerators();

    if (mods.contains(potentialModerator) &&
            !group.hasMember(userToAdd)) {

      group.addUser(potentialModerator, userToAdd);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;


  }

  /**
   * The function processes an approved add of a subgroup to the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processApproveAddSubGroup(Message message) {

    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    User potentialModerator = UserServiceImpl.getInstance().protectedfindUserByName(message.getFrom());
    Group groupToAdd = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getContent());

    Set<User> mods = group.getModerators();
    if (mods.contains(potentialModerator) &&
            !group.hasSubGroup(groupToAdd)) {

      group.addSubgroup(potentialModerator, groupToAdd);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;


  }

  /**
   * The function processes an removal of user if they exist in the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processRemoveUser(Message message) {

    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    User userRequesting = UserServiceImpl.getInstance().protectedfindUserByName(message.getFrom());
    User userToBeRemoved = UserServiceImpl.getInstance().protectedfindUserByName(message.getContent());

    Set<User> mods = group.getModerators();
    boolean authenticatedRequest = mods.contains(userRequesting) || userRequesting.equals(userToBeRemoved);
    boolean validRemoval = (!mods.contains(userToBeRemoved) || group.getModerators().size() > 1);

    if (authenticatedRequest && validRemoval) {

      group.removeUser(userRequesting, userToBeRemoved);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }

    return response;

  }

  /**
   * The function processes an removal of subgroup if they exist in the group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processRemoveSubGroup(Message message) {

    Message response;

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());
    User userRequesting = UserServiceImpl.getInstance().protectedfindUserByName(message.getFrom());
    Group groupToBeRemoved = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getContent());


    Set<User> mods = group.getModerators();
    Set<User> subGroupMods = groupToBeRemoved.getModerators();

    boolean authenticatedRequest = mods.contains(userRequesting) ||
            subGroupMods.contains(userRequesting);
    boolean validRemoval = group.hasSubGroup(groupToBeRemoved);

    if (authenticatedRequest && validRemoval) {
      group.removeSubgroup(userRequesting, groupToBeRemoved);

      response = generateSuccessResponseMessage(message);
    } else {
      response = generateFailureResponseMessage(message);
    }
    return response;

  }


  /**
   * This generates a response message from the group services
   *
   * @param contentType - the type of content of this message
   * @param response    - the response that is being sent
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
