package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;

public class GroupDatabaseImpl implements GroupDAO {
  @Override
  public void createGroup() {

  }

  @Override
  public List<Group> findAllGroups() {
    return null;
  }

  @Override
  public Group findGroupByName(Group group) {
    return null;
  }

  @Override
  public void updateGroup(Group group) {

  }

  @Override
  public void deleteGroup(Group group) {

  }

  @Override
  public boolean isGroupActive(Group group) {
    return false;
  }

  @Override
  public User findPrimaryModerator(Group group) {
    return null;
  }

  @Override
  public List<User> findAllModeratorsByGroup(Group group) {
    return null;
  }

  @Override
  public List<User> findAllModerators() {
    return null;
  }

  @Override
  public List<Group> findAllSubGroupsByGroup(Group group) {
    return null;
  }

  @Override
  public List<Group> findAllSubGroups() {
    return null;
  }

  @Override
  public String findUserAlias(Group group, User user) {
    return null;
  }
}
