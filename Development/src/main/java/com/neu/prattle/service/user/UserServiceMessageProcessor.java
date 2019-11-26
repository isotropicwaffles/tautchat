package com.neu.prattle.service.user;


import com.neu.prattle.messaging.GenericMessageResponses;
import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageAddresses;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.websocket.SessionServiceCommands;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;


/**
 * This is a user service message processor
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public class UserServiceMessageProcessor implements IMessageProcessor {

  /**
   * Singleton instance of this class
   */
  private static IMessageProcessor instance = new UserServiceMessageProcessor();

  /**
   * Private constructor for class
   */
  private UserServiceMessageProcessor() {
  }

  /**
   * Returns singleton instance of this class
   *
   * @returns instance - instance of this clas
   */
  public static IMessageProcessor getInstance() {

    return UserServiceMessageProcessor.instance;
  }

  
  /**
   * This processes the received message
   *
   * @param message - a message to be processed
   */
  public void processMessage(Message message) throws IOException {

    Message response;

    if (message.getContentType().equals(UserServiceCommands.LOGIN.label)) {
      response = processLogin(message);

    } else if (message.getContentType().equals(UserServiceCommands.USER_CREATE.label)) {
      response = processUserCreation(message);

    } else if (message.getContentType().equals(UserServiceCommands.SEARCH_USERS_BY_NAME.label)) {
      response = processSearchForUserName(message);
    } else {
      response = generateResponseMessage(message.getContentType(),
              GenericMessageResponses.UNKNOWN_COMMAND.label);
    }

    if (response.getTo() == null) {
      response.setTo(message.getFrom());
    }

    IMessageProcessor.sendMessage(response);

  }


  /**
   * Evaluates whether the message can be processed by this processor
   *
   * @param message - a message to be processed
   * @returns boolean - True if the message was sent to the user services
   */
  @Override
  public boolean canProcessMessage(Message message) {
    return message.getType().contentEquals(MessageAddresses.USER_SERVICE.label);
  }

  /**
   * This processes the searching for user by name message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processSearchForUserName(Message message) {

    Set<User> users = UserServiceImpl.getInstance().findUserByPartialName(message.getContent());

    return generateResponseMessage(message.getContentType(), UserService.generateUserList(users));

  }

  /**
   * This processes the login message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processLogin(Message message) {
    Message response;
    String userName = message.getContent();
    Optional<User> user = UserServiceImpl.getInstance().findUserByName(userName);

    if (!user.isPresent()) {
      response = generateResponseMessage(message.getContentType(),
              GenericMessageResponses.FAILURE_RESPONSE.label);
    } else {

      IMessageProcessor.sendMessage(generateSessionLoginRequest(message.getFrom(), userName));
      response = generateResponseMessage(message.getContentType(),
              GenericMessageResponses.SUCCESS_RESPONSE.label);
      response.setTo(userName);
    }

    return response;

  }


  /**
   * This processes the user create message
   *
   * @param message - a message to be processed
   * @return a message in reponse to this request
   */
  private Message processUserCreation(Message message) throws IOException {
    Message response;

    String userName = message.getContent();

    Optional<User> user = UserServiceImpl.getInstance().findUserByName(userName);

    if (!user.isPresent()) {
      User user1 = new User.UserBuilder()
              .setName(userName)
              .build();

      UserServiceImpl.getInstance().addUser(user1);

      response = generateResponseMessage(message.getContentType(),
              GenericMessageResponses.SUCCESS_RESPONSE.label);
    } else {
      response = generateResponseMessage(message.getContentType(),
              GenericMessageResponses.FAILURE_RESPONSE.label);
    }

    return response;
  }


  /**
   * This generates a response message from the user servicese
   *
   * @param receiver - to whom the message should be sent
   * @param response - the response that is being sent
   */
  private Message generateResponseMessage(String contentType, String response) {
    return Message.messageBuilder()
            .setFrom(MessageAddresses.USER_SERVICE.label)
            .setContentType(contentType)
            .setType(MessageAddresses.DIRECT_MESSAGE.label)
            .setMessageContent(response)
            .build();
  }

  /**
   * This generates a login request to send to Session Service
   *
   * @param userID    - current user ID
   * @param loginName - user Login Name
   */
  private Message generateSessionLoginRequest(String userID, String loginName) {
    return Message.messageBuilder()
            .setFrom(MessageAddresses.USER_SERVICE.label)
            .setTo(userID)
            .setType(MessageAddresses.SESSION_SERVICE.label)
            .setContentType(SessionServiceCommands.LOGIN.label)
            .setMessageContent(loginName)
            .build();
  }


}
