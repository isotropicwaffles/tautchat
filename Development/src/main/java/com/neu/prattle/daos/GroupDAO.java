package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;

/**
 * The interface Group dao.
 */
public interface GroupDAO {

  /**
   * Create group.
   *
   * @param group the group
   */
  void createGroup(Group group);

  /**
   * Add user to group.
   *
   * @param user  the user
   * @param group the group
   */
  void addUserToGroup(User user, Group group);

  /**
   * Delete user from group.
   *
   * @param user  the user
   * @param group the group
   */
  void deleteUserFromGroup(User user, Group group);

  /**
   * Is user member of group boolean.
   *
   * @param user  the user
   * @param group the group
   * @return the boolean
   */
  boolean isUserMemberOfGroup(User user, Group group);

  /**
   * Find all group members list.
   *
   * @param group the group
   * @return the list
   */
  List<User> findAllGroupMembers(Group group);

  /**
   * Find all groups list.
   *
   * @return the list
   */
  List<Group> findAllGroups();

  /**
   * Find group by name group.
   *
   * @param group the group name
   * @return the group
   */
  Group findGroupByName(Group group);

  /**
   * Group exists boolean.
   *
   * @param group the group name
   * @return the boolean
   */
  boolean groupExists(Group group);

  /**
   * Update group.
   *
   * @param group the group
   */
  void updateGroup(Group group);

  /**
   * Delete group.
   *
   * @param group the group
   */
  void deleteGroup(Group group);

  /**
   * Is group active boolean.
   *
   * @param group the group
   * @return the boolean
   */
  boolean isGroupActive(Group group);

  /**
   * Find primary moderator user.
   *
   * @param group the group
   * @return the user
   */
  User findPrimaryModerator(Group group);

  /**
   * Create moderator for group.
   *
   * @param user  the user
   * @param group the group
   */
  void createModeratorForGroup(User user, Group group);

  /**
   * Delete moderator for group.
   *
   * @param user  the user
   * @param group the group
   */
  void deleteModeratorForGroup(User user, Group group);

  /**
   * Moderator for group exists boolean.
   *
   * @param user  the user
   * @param group the group
   * @return the boolean
   */
  boolean moderatorForGroupExists(User user, Group group);

  /**
   * Find all moderators by group list.
   *
   * @param group the group
   * @return the list
   */
  List<User> findAllModeratorsByGroup(Group group);

  /**
   * Find all moderators list.
   *
   * @return the list
   */
  List<User> findAllModerators();

  /**
   * Create sub group for group.
   *
   * @param parentGroup the parent group
   * @param subgroup    the subgroup
   */
  void createSubGroupForGroup(Group parentGroup, Group subgroup);

  /**
   * Delete sub group for group.
   *
   * @param parentGroup the parent group
   * @param subgroup    the subgroup
   */
  void deleteSubGroupForGroup(Group parentGroup, Group subgroup);

  /**
   * Subgroup for group exists boolean.
   *
   * @param parentGroup the parent group
   * @param subgroup    the subgroup
   * @return the boolean
   */
  boolean subgroupForGroupExists(Group parentGroup, Group subgroup);

  /**
   * Find all sub groups by group list.
   *
   * @param group the group
   * @return the list
   */
  List<Group> findAllSubGroupsByGroup(Group group);

  /**
   * Find all sub groups list.
   *
   * @return the list
   */
  List<Group> findAllSubGroups();

  /**
   * Create user alias for group.
   *
   * @param alias the alias
   * @param user  the user
   * @param group the group
   */
  void createUserAliasForGroup(String alias, User user, Group group);

  /**
   * Delete user alias for group.
   *
   * @param user  the user
   * @param group the group
   */
  void deleteUserAliasForGroup(User user, Group group);

  /**
   * User alias for group exists boolean.
   *
   * @param user  the user
   * @param group the group
   * @return the boolean
   */
  boolean userAliasForGroupExists(User user, Group group);

  /**
   * Find user alias string.
   *
   * @param group the group
   * @param user  the user
   * @return the string
   */
  String findUserAlias(Group group, User user);
}
