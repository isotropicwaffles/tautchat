package com.neu.prattle.service.group;

import com.neu.prattle.messaging.GenericMessageResponses;
import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserService;

import java.io.IOException;
import java.util.Set;


/**
 * This is a Groiup service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class GroupServiceGroupQueryMessageProcessor implements IMessageProcessor {


  /**
   * Singleton instance of this class
   */
  private static IMessageProcessor instance = new GroupServiceGroupQueryMessageProcessor();

  /**
   * Private constructor for class
   */
  private GroupServiceGroupQueryMessageProcessor() {
  }

  /**
   * Returns singleton instance of this class
   *
   * @return instance - instance of this clas
   */
  public static IMessageProcessor getInstance() {

    return GroupServiceGroupQueryMessageProcessor.instance;
  }


  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @return boolean - True if the message was sent to the user services
   */
  @Override
  public boolean canProcessMessage(Message message) {
    return message.getContentType().contentEquals(GroupServiceCommands.GET_GROUP_USERS.label) ||
            message.getContentType().contentEquals(GroupServiceCommands.GET_GROUP_SUBGROUPS.label) ||
            message.getContentType().contentEquals(GroupServiceCommands.GET_GROUP_SUPERGROUPS.label) ||
            message.getContentType().contentEquals(GroupServiceCommands.GET_GROUP_MODERATORS.label) ||
            message.getContentType().contentEquals(GroupServiceCommands.GET_PENDING_USER_REQUESTS.label) ||
            message.getContentType().contentEquals(GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label)||
            message.getContentType().contentEquals(GroupServiceCommands.SEARCH_GROUPS_BY_NAME.label);
    
  }


  /**
   * This processes the received message
   *
   * @param message - a message to be processed
   */
  public void processMessage(Message message) throws IOException {

    Message response;

    try {
      if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_USERS.label)) {

        response = processGetUsersFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_SUBGROUPS.label)) {

        response = processGetSubGroupsFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_SUPERGROUPS.label)) {

        response = processGetSuperGroupsFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GET_GROUP_MODERATORS.label)) {

        response = processGetModeratorsFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GET_PENDING_USER_REQUESTS.label)) {

        response = processGetPendingUsersAddsFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.GET_PENDING_SUBGROUP_REQUESTS.label)) {

        response = processGetPendingSubgroupAddsFromGroup(message);

      } else if (message.getContentType().equals(GroupServiceCommands.SEARCH_GROUPS_BY_NAME.label)) {

          response = processSearchForGroupName(message);
      }else {
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
   * This processes the searching for group by name message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processSearchForGroupName(Message message) {

    Set<Group> users = GroupServiceImpl.getInstance().findGroupByPartialName(message.getContent());

    return generateResponseMessage(message.getContentType(), GroupService.generateGroupList(users));

  }

  /**
   * Sends the requester a list of the users within queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetUsersFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<User> users = group.getMembers();

    return generateResponseMessage(message.getContentType(), UserService.generateUserList(users));

  }

  /**
   * Sends the requester a list of the moderators within queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetModeratorsFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<User> users = group.getModerators();

    return generateResponseMessage(message.getContentType(), UserService.generateUserList(users));

  }

  /**
   * Sends the requester a list of the subgroups within queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetSubGroupsFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<Group> groups = group.getSubGroups();

    return generateResponseMessage(message.getContentType(), GroupService.generateGroupList(groups));

  }

  /**
   * Sends the requester a list of the super groups within queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetSuperGroupsFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<Group> groups = group.getSuperGroups();

    return generateResponseMessage(message.getContentType(), GroupService.generateGroupList(groups));

  }

  /**
   * Sends the requester a list of the users pending to be added to queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetPendingUsersAddsFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<User> users = group.getPendingUserRequests();

    return generateResponseMessage(message.getContentType(), UserService.generateUserList(users));
  }

  /**
   * Sends the requester a list of the subgroups pending to be added to queried group
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processGetPendingSubgroupAddsFromGroup(Message message) {

    Group group = GroupServiceImpl.getInstance().protectedFindGroupByName(message.getTo());

    Set<Group> subgroups = group.getPendingSubGroupRequests();

    return generateResponseMessage(message.getContentType(), GroupService.generateGroupList(subgroups));
  }

  /**
   * This generates a response message from the group services
   *
   * @param contentType - the type of content of this message
   * @param response    - the response that is being sent
   * 
   * @return message - response message
   */
  private Message generateResponseMessage(String contentType, String response) {
    return Message.messageBuilder()
            .setFrom(MessageAddresses.GROUP_SERVICE.label)
            .setContentType(contentType)
            .setType(MessageAddresses.DIRECT_MESSAGE.label)
            .setMessageContent(response)
            .build();
  }


}
