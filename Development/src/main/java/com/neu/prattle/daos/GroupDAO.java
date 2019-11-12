package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;

public interface GroupDAO {

  void createGroup();
  List<Group> findAllGroups();
  Group findGroupByName(Group group);
  void updateGroup(Group group);
  void deleteGroup(Group group);
  boolean isGroupActive(Group group);
  User findPrimaryModerator(Group group);
  List<User> findAllModeratorsByGroup(Group group);
  List<User> findAllModerators();
  List<Group> findAllSubGroupsByGroup(Group group);
  List<Group> findAllSubGroups();
  String findUserAlias(Group group, User user);
}
