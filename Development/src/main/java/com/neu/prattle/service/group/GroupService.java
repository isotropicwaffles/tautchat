package com.neu.prattle.service.group;

import com.neu.prattle.messaging.ReservedCharacters;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on Group accounts.
 *
 * @author Chad
 * @version dated 2019-11-09
 *
 */
public interface GroupService {

  /***
   * Returns a string representation of a list of groups
   * @param groups - generates string list of groups
   * @return string list of group object names.
   */
  static String generateGroupList(Set<Group> groups) {

    StringJoiner list = new StringJoiner(ReservedCharacters.LIST_SEPARATORS.label);

    for (Group subgroup : groups) {
      list.add(subgroup.getName());
    }

    return list.toString();
  }


  /***
   * Returns an optional object which might be empty or wraps an object
   * if the System contains a {@link Group} object having the same name
   * as the parameter.
   *
   * @param name The name of the group
   * @return Optional object.
   */
  Optional<Group> findGroupByName(String name);


  /***
   * Attempts to return the name of a group and throws and error if the group doesn't exist
   *
   * @param name - The name of the group.
   * @return The associated group.
   */
  Group protectedFindGroupByName(String name);

  
  /***
   * Queries partial name and returns all searchable groups that names partially match
   *
   *
   * @param partialName - The name of the group.
   * @return Set of group with matching parital name.
   */
  Set<Group> findGroupByPartialName(String partialName);
  
  /***
   * Tries to add a group in the system
   * @param group Group object
   * @throws IOException when fileWriter blows up.
   *
   */
  void addGroup(Group group) throws IOException;

  /***
   * Tries to delete a user in the system
   * @param group Group object
   *
   */
  void deleteGroup(Group group);


  /***
   * Returns a set if Groups that the memember is a part of
   * @param user - The user to query groups for
   * @return Set of group objects.
   */
  Set<Group> getUserGroupMemberships(User user);


}
