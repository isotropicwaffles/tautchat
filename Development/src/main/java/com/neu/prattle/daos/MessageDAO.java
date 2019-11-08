package com.neu.prattle.daos;


import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import java.util.Collection;

/**
 * The Message Data Access Object.
 */
public interface MessageDAO {

  /**
   * Create message record in database.
   *
   * @param message the message to be stored
   */
  void createMessage(Message message);

  /**
   * Find all messages for a user.
   *
   * @param user the user to id desired messages.
   * @return the collection of messages for that user
   */
  Collection<Message> findAllMessagesOfUser(User user);

  /**
   * Find message by message id.
   *
   * @param messageId the message id
   * @return the message
   */
  Message findMessageById(int messageId);

  /**
   * Update message. Useful for flagging/monitoring purposes.
   *
   * @param messageId the message id
   * @param message   the message
   * @return 0 denoting completion of method
   */
  int updateMessage(int messageId, Message message);

  /**
   * Delete a stored message. Do we ever need this in practice?
   *
   * @param messageId id for message record
   * @return 0 denoting completion of method
   */
  int deleteMessage(int messageId);
}

