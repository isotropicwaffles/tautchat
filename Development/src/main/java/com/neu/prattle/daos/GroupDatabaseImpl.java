package com.neu.prattle.daos;

import com.neu.prattle.DatabaseConnection;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class GroupDatabaseImpl implements GroupDAO {

  private LogManager logManager = LogManager.getLogManager();
  private Logger logging = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);

  private UserDatabaseImpl userDatabase = new UserDatabaseImpl();
  private static final String ANDUSERNAME = "' AND `username`='";
  private static final String STATUS = "status";
  private static final String ISBOT = "is_bot";
  private static final String SEARCHABLE = "searchable";
  private static final String GROUPNAME = "group_name";
  private static final String ISACTIVE = "is_active";
  private static final String PRIMARYMODERATOR = "primary_moderator";
  private static final String SELECTALLGROUP = "SELECT * FROM `tautdb`.`groups` WHERE `group_name`='";

  private void executeUpdateQueryHelper(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(string);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update Group Query SQL blew up: " + e.toString());
    }
  }

  private boolean executeBooleanQuery(String string) {
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(string)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Execute Boolean Query (Group) SQL blew up: " + e.toString());
    }
    return false;
  }

  private List<User> executeQueryUserList(String string) {
    ArrayList<User> groupMembers = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<Boolean> isBotList = new ArrayList<>();
    ArrayList<Boolean> isSearchableList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(string)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          nameList.add(results.getString("name"));
          statusList.add(results.getString(STATUS));
          isBotList.add(results.getBoolean(ISBOT));
          isSearchableList.add(results.getBoolean(SEARCHABLE));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Users (Group) SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
        User user = new User.UserBuilder()
        		.setName(nameList.get(index))
        		.setId(idList.get(index))
        		.setBot(isBotList.get(index))
        		.setSearchable(isSearchableList.get(index))
        		.setStatus(userDatabase.stringToUserStatus(statusList.get(index)))
        		.build();

        groupMembers.add(user);
        index++;
    }
    return groupMembers;
  }

  private List<Group> executeQueryGroupList(String string) {
    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> groupNameList = new ArrayList<>();
    ArrayList<Boolean> isActiveList = new ArrayList<>();
    ArrayList<String> primaryModeratorList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(string)) {
        while (resultSet.next()) {
          idList.add(resultSet.getInt("id"));
          isActiveList.add(resultSet.getBoolean(ISACTIVE));
          groupNameList.add(resultSet.getString(GROUPNAME));
          primaryModeratorList.add(resultSet.getString(PRIMARYMODERATOR));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find all Groups SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User mod = userDatabase.findUserByUsername(primaryModeratorList.get(index));
      Group group = new Group(mod);
      group.setId(idList.get(index));
      group.setName(mod, groupNameList.get(index));
      group.setActive(isActiveList.get(index));
      groups.add(group);
      index++;
    }
    return groups;
  }

  @Override
  public void createGroup(Group group) {
    String createUserSQL = "INSERT INTO `tautdb`.`groups` (`is_active`, `group_name`, "
            + "`primary_moderator`)"
            + " VALUES (" + group.isActive() + ", '" + group.getName() + "', '"
            + group.getModerators().iterator().next().getName() + "')";
    executeUpdateQueryHelper(createUserSQL);
  }

  @Override
  public void addUserToGroup(User user, Group group) {
    String createUserSQL = "INSERT INTO `tautdb`.`group_members` (`group_name`, "
            + "`username`) VALUES ('" + group.getName() + "', '" + user.getName() + "')";
    if (!isUserMemberOfGroup(user, group)) {
      executeUpdateQueryHelper(createUserSQL);
    }
  }

  @Override
  public void deleteUserFromGroup(User user, Group group) {
    String deleteGroupSQL = "DELETE FROM `tautdb`.`group_members` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    executeUpdateQueryHelper(deleteGroupSQL);
  }

  @Override
  public boolean isUserMemberOfGroup(User user, Group group) {
    String groupExistsSQL = "SELECT * FROM `tautdb`.`group_members` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    return executeBooleanQuery(groupExistsSQL);
  }

  @Override
  public List<User> findAllGroupMembers(Group group) {
    String findAllGroupMembersSQL = "SELECT * FROM `tautdb`.`group_members` JOIN `tautdb`.`users` "
            + "ON `group_members`.`username` = `users`.`name` WHERE `group_members`.`group_name`='"
            + group.getName() + "'";
    return executeQueryUserList(findAllGroupMembersSQL);
  }

  @Override
  public List<Group> findAllGroups() {
    String findAllGroupsSQL = "SELECT * FROM `tautdb`.`groups`";
    return executeQueryGroupList(findAllGroupsSQL);
  }

  @Override
  public Group findGroupByName(String groupName) {
    String findGroupByNameSQL = SELECTALLGROUP + groupName + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(findGroupByNameSQL)) {
        if (resultSet.next()) {
          int id = resultSet.getInt("id");
          boolean isActive = resultSet.getBoolean(ISACTIVE);
          String primaryModerator = resultSet.getString(PRIMARYMODERATOR);
          User mod = userDatabase.findUserByUsername(primaryModerator);
          Group groupFromDB = new Group(mod);
          groupFromDB.setId(id);
          groupFromDB.setName(mod, groupName);
          groupFromDB.setActive(isActive);
          return groupFromDB;
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find Group by Group Name SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public boolean groupExists(String groupName) {
    String groupExistsSQL = SELECTALLGROUP + groupName + "'";
    return executeBooleanQuery(groupExistsSQL);
  }

  @Override
  public void updateGroup(Group group) {
    String updateGroupSQL = "UPDATE `tautdb`.`groups` SET `is_active`=" + group.isActive()
            + ", `primary_moderator`='" + group.getModerators().iterator().next().getName()
            + "' WHERE `group_name`='" + group.getName() + "'";
    executeUpdateQueryHelper(updateGroupSQL);
  }

  @Override
  public void deleteGroup(Group group) {
    String deleteGroupSQL = "DELETE FROM `tautdb`.`groups` WHERE `group_name`='"
            + group.getName() + "'";
    executeUpdateQueryHelper(deleteGroupSQL);
  }

  @Override
  public boolean isGroupActive(Group group) {
    String isGroupActiveSQL = SELECTALLGROUP + group.getName() + "' AND `is_active`=1";
    return executeBooleanQuery(isGroupActiveSQL);
  }

  @Override
  public User findPrimaryModerator(Group group) {
    String findPrimaryModeratorSQL = "SELECT `primary_moderator` FROM `tautdb`.`groups` "
            + "WHERE `group_name`='" + group.getName() + "'";
    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findPrimaryModeratorSQL)) {
        if (results.next()) {
          return userDatabase.findUserByUsername(results.getString(PRIMARYMODERATOR));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find Primary Moderator SQL blew up: " + e.toString());
    }
    return null;
  }

  @Override
  public void createModeratorForGroup(User user, Group group) {
    String createModeratorForGroupSQL = "INSERT INTO `tautdb`.`moderators` (`group_name`, `username`) "
            + " VALUES ('" + group.getName() + "', '" + user.getName() + "')";
    executeUpdateQueryHelper(createModeratorForGroupSQL);
  }

  @Override
  public void deleteModeratorForGroup(User user, Group group) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`moderators` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    executeUpdateQueryHelper(deleteModeratorSQL);
  }

  @Override
  public boolean moderatorForGroupExists(User user, Group group) {
    String moderatorExistsSQL = "SELECT * FROM `tautdb`.`moderators` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    return executeBooleanQuery(moderatorExistsSQL);
  }


  @Override
  public List<User> findAllModeratorsByGroup(Group group) {
    String findAllModeratorsByGroupSQL = "SELECT * FROM `tautdb`.`moderators` JOIN `tautdb`.`users` "
            + "ON `moderators`.`username` = `users`.`name` WHERE `moderators`.`group_name`='"
            + group.getName() + "'";
    return executeQueryUserList(findAllModeratorsByGroupSQL);
  }

  @Override
  public List<User> findAllModerators() {
    String findAllModeratorsSQL = "SELECT * FROM `tautdb`.`moderators` JOIN `tautdb`.`users` "
            + "ON `moderators`.`username` = `users`.`name`";
    return executeQueryUserList(findAllModeratorsSQL);
  }

  @Override
  public void createSubGroupForGroup(Group parentGroup, Group subgroup) {
    String createSubGroupForGroupSQL = "INSERT INTO `tautdb`.`subgroups` (`subgroup_name`, "
            + "`parent_group_name`) VALUES ('" + subgroup.getName() + "', '" + parentGroup.getName()
            + "')";
    executeUpdateQueryHelper(createSubGroupForGroupSQL);
  }

  @Override
  public boolean subgroupForGroupExists(Group parentGroup, Group subgroup) {
    String subgroupExistsSQL = "SELECT * FROM `tautdb`.`subgroups` WHERE `subgroup_name`='"
            + subgroup.getName() + "' AND `parent_group_name`='" + parentGroup.getName() + "'";
    return executeBooleanQuery(subgroupExistsSQL);
  }

  @Override
  public void deleteSubGroupForGroup(Group parentGroup, Group subgroup) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`subgroups` WHERE `parent_group_name`='"
            + parentGroup.getName() + "' AND `subgroup_name`='" + subgroup.getName() + "'";
    executeUpdateQueryHelper(deleteModeratorSQL);
  }


  @Override
  public List<Group> findAllSubGroupsByGroup(Group group) {
    String findAllSubGroupsByGroupSQL = "SELECT * FROM `tautdb`.`subgroups` JOIN `tautdb`.`groups`"
            + " ON `subgroups`.`parent_group_name` = `groups`.`group_name` WHERE `subgroups`."
            + "`parent_group_name`='" + group.getName() + "'";
    return executeQueryGroupList(findAllSubGroupsByGroupSQL);
  }

  @Override
  public List<Group> findAllSubGroups() {
    String findAllSubGroupsSQL = "SELECT * FROM `tautdb`.`subgroups` JOIN `tautdb`.`groups`"
            + " ON `subgroups`.`subgroup_name` = `groups`.`group_name`";
    return executeQueryGroupList(findAllSubGroupsSQL);
  }

  @Override
  public void createUserAliasForGroup(String alias, User user, Group group) {
    String createUserAliasForGroupSQL = "INSERT INTO `tautdb`.`member_aliases` (`username`, "
            + "`group_name`, `alias`) VALUES ('" + user.getName() + "', '" + group.getName()
            + "', '" + alias + "')";
    executeUpdateQueryHelper(createUserAliasForGroupSQL);
  }

  @Override
  public void deleteUserAliasForGroup(User user, Group group) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`member_aliases` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    executeUpdateQueryHelper(deleteModeratorSQL);
  }

  @Override
  public boolean userAliasForGroupExists(User user, Group group) {
    String groupExistsSQL = "SELECT * FROM `tautdb`.`member_aliases` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";
    return executeBooleanQuery(groupExistsSQL);
  }


  @Override
  public String findUserAlias(Group group, User user) {
    String findUserAliasSQL = "SELECT `alias` FROM `tautdb`.`member_aliases` WHERE `username`='"
            + user.getName() + "' AND `group_name`='" + group.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findUserAliasSQL)) {
        if (results.next()) {
          return results.getString("alias");
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All SubGroups SQL blew up: " + e.toString());
    }
    return null;
  }

  public void truncateGroups() {
    String truncateGroupsSQL = "DELETE FROM `tautdb`.`groups`";
    executeUpdateQueryHelper(truncateGroupsSQL);
  }

  public void truncateGroupMembers() {
    String truncateGroupMembers = "DELETE FROM `tautdb`.`group_members`";
    executeUpdateQueryHelper(truncateGroupMembers);
  }

  public void truncateModerators() {
    String truncateModeratorsSQL = "DELETE FROM `tautdb`.`moderators`";
    executeUpdateQueryHelper(truncateModeratorsSQL);
  }

  public void truncateSubgroups() {
    String truncateSubgroupsSQL = "DELETE FROM `tautdb`.`subgroups`";
    executeUpdateQueryHelper(truncateSubgroupsSQL);
  }

  public void truncateMemberAliases() {
    String truncateMemberAliasesSQL = "DELETE FROM `tautdb`.`member_aliases`";
    executeUpdateQueryHelper(truncateMemberAliasesSQL);
  }
}
