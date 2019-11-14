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


  @Override
  public void createGroup(Group group) {
    String createUserSQL = "INSERT INTO `tautdb`.`groups` (`is_active`, `group_name`, "
            + "`primary_moderator`)"
            + " VALUES (" + group.isActive() + ", '" + group.getName() + "', '"
            + group.getModerators().iterator().next().getName() + "')";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createUserSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public void addUserToGroup(User user, Group group) {
    String createUserSQL = "INSERT INTO `tautdb`.`group_members` (`group_name`, "
            + "`username`) VALUES ('" + group.getName() + "', '" + user.getName() + "')";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createUserSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Add User to Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public void deleteUserFromGroup(User user, Group group) {
    String deleteGroupSQL = "DELETE FROM `tautdb`.`group_members` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete User from Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean isUserMemberOfGroup(User user, Group group) {
    String groupExistsSQL = "SELECT * FROM `tautdb`.`group_members` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(groupExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does Group member exist SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public List<User> findAllGroupMembers(Group group) {
    String findAllGroupMembersSQL = "SELECT * FROM `tautdb`.`group_members` JOIN `tautdb`.`users` "
            + "ON `group_members`.`username` = `users`.`name` WHERE `group_members`.`group_name`='"
            + group.getName() + "'";
    ArrayList<User> groupMembers = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<Boolean> isBotList = new ArrayList<>();
    ArrayList<Boolean> isSearchableList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllGroupMembersSQL)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          nameList.add(results.getString("name"));
          statusList.add(results.getString(STATUS));
          isBotList.add(results.getBoolean(ISBOT));
          isSearchableList.add(results.getBoolean(SEARCHABLE));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Group Members SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User user = new User(nameList.get(index), isBotList.get(index));
      user.setId(idList.get(index));
      user.setStatus(userDatabase.stringToUserStatus(statusList.get(index)));
      user.setSearchable(isSearchableList.get(index));
      groupMembers.add(user);
      index++;
    }
    return groupMembers;
  }

  @Override
  public List<Group> findAllGroups() {
    String findAllGroupsSQL = "SELECT * FROM `tautdb`.`groups`";
    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> groupNameList = new ArrayList<>();
    ArrayList<Boolean> isActiveList = new ArrayList<>();
    ArrayList<String> primaryModeratorList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(findAllGroupsSQL)) {
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

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(groupExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does Group exist SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public void updateGroup(Group group) {
    String updateGroupSQL = "UPDATE `tautdb`.`groups` SET `is_active`=" + group.isActive()
            + ", `group_name`='" + group.getName() + "', `primary_moderator`='"
            + group.getModerators().iterator().next().getName() + "' WHERE `id`=" + group.getId();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(updateGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Update Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public void deleteGroup(Group group) {
    String deleteGroupSQL = "DELETE FROM `tautdb`.`groups` WHERE `group_name`='"
            + group.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean isGroupActive(Group group) {
    String isGroupActiveSQL = SELECTALLGROUP + group.getName() + "' AND `is_active`=1";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(isGroupActiveSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Is Group active SQL blew up: " + e.toString());
    }
    return false;
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

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createModeratorForGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create Moderator for Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public void deleteModeratorForGroup(User user, Group group) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`moderators` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteModeratorSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete Moderator SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean moderatorForGroupExists(User user, Group group) {
    String moderatorExistsSQL = "SELECT * FROM `tautdb`.`moderators` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(moderatorExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does Moderator exist SQL blew up: " + e.toString());
    }
    return false;
  }


  @Override
  public List<User> findAllModeratorsByGroup(Group group) {
    String findAllModeratorsByGroupSQL = "SELECT * FROM `tautdb`.`moderators` JOIN `tautdb`.`users` "
            + "ON `moderators`.`username` = `users`.`name` WHERE `moderators`.`group_name`='"
            + group.getName() + "'";
    ArrayList<User> groupMods = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<Boolean> isBotList = new ArrayList<>();
    ArrayList<Boolean> isSearchableList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllModeratorsByGroupSQL)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          nameList.add(results.getString("name"));
          statusList.add(results.getString(STATUS));
          isBotList.add(results.getBoolean(ISBOT));
          isSearchableList.add(results.getBoolean(SEARCHABLE));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Moderators in a Group SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User user = new User(nameList.get(index), isBotList.get(index));
      user.setId(idList.get(index));
      user.setStatus(userDatabase.stringToUserStatus(statusList.get(index)));
      user.setSearchable(isSearchableList.get(index));
      groupMods.add(user);
      index++;
    }
    return groupMods;
  }

  @Override
  public List<User> findAllModerators() {
    String findAllModeratorsSQL = "SELECT * FROM `tautdb`.`moderators` JOIN `tautdb`.`users` "
            + "ON `moderators`.`username` = `users`.`name`";
    ArrayList<User> groupMods = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<Boolean> isBotList = new ArrayList<>();
    ArrayList<Boolean> isSearchableList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllModeratorsSQL)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          nameList.add(results.getString("name"));
          statusList.add(results.getString(STATUS));
          isBotList.add(results.getBoolean(ISBOT));
          isSearchableList.add(results.getBoolean(SEARCHABLE));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All Moderators SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User user = new User(nameList.get(index), isBotList.get(index));
      user.setId(idList.get(index));
      user.setStatus(userDatabase.stringToUserStatus(statusList.get(index)));
      user.setSearchable(isSearchableList.get(index));
      groupMods.add(user);
      index++;
    }
    return groupMods;
  }

  @Override
  public void createSubGroupForGroup(Group parentGroup, Group subgroup) {
    String createSubGroupForGroupSQL = "INSERT INTO `tautdb`.`subgroups` (`subgroup_name`, "
            + "`parent_group_name`) VALUES ('" + subgroup.getName() + "', '" + parentGroup.getName()
            + "')";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createSubGroupForGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create Subgroup for Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean subgroupForGroupExists(Group parentGroup, Group subgroup) {
    String subgroupExistsSQL = "SELECT * FROM `tautdb`.`subgroups` WHERE `subgroup_name`='"
            + subgroup.getName() + "' AND `parent_group_name`='" + parentGroup.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(subgroupExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does SubGroup exist SQL blew up: " + e.toString());
    }
    return false;
  }

  @Override
  public void deleteSubGroupForGroup(Group parentGroup, Group subgroup) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`subgroups` WHERE `parent_group_name`='"
            + parentGroup.getName() + "' AND `subgroup_name`='" + subgroup.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteModeratorSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete SubGroup SQL blew up: " + e.toString());
    }
  }


  @Override
  public List<Group> findAllSubGroupsByGroup(Group group) {
    String findAllSubGroupsByGroupSQL = "SELECT * FROM `tautdb`.`subgroups` JOIN `tautdb`.`groups`"
            + " ON `subgroups`.`parent_group_name` = `groups`.`group_name` WHERE `subgroups`."
            + "`parent_group_name`='" + group.getName() + "'";
    ArrayList<Group> groupSubGroups = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> groupNameList = new ArrayList<>();
    ArrayList<Boolean> isActiveList = new ArrayList<>();
    ArrayList<String> primaryModeratorList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllSubGroupsByGroupSQL)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          isActiveList.add(results.getBoolean(ISACTIVE));
          groupNameList.add(results.getString(GROUPNAME));
          primaryModeratorList.add(results.getString(PRIMARYMODERATOR));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All SubGroups for a Group SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User mod = userDatabase.findUserByUsername(primaryModeratorList.get(index));
      Group newGroup = new Group(mod);
      newGroup.setId(idList.get(index));
      newGroup.setName(mod, groupNameList.get(index));
      newGroup.setActive(isActiveList.get(index));
      groupSubGroups.add(newGroup);
      index++;
    }
    return groupSubGroups;
  }

  @Override
  public List<Group> findAllSubGroups() {
    String findAllSubGroupsSQL = "SELECT * FROM `tautdb`.`subgroups` JOIN `tautdb`.`groups`"
            + " ON `subgroups`.`subgroup_name` = `groups`.`group_name`";
    ArrayList<Group> subGroups = new ArrayList<>();
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> groupNameList = new ArrayList<>();
    ArrayList<Boolean> isActiveList = new ArrayList<>();
    ArrayList<String> primaryModeratorList = new ArrayList<>();

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(findAllSubGroupsSQL)) {
        while (results.next()) {
          idList.add(results.getInt("id"));
          isActiveList.add(results.getBoolean(ISACTIVE));
          groupNameList.add(results.getString(GROUPNAME));
          primaryModeratorList.add(results.getString(PRIMARYMODERATOR));
        }
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Find All SubGroups SQL blew up: " + e.toString());
    }
    int index = 0;
    while (index < idList.size()) {
      User mod = userDatabase.findUserByUsername(primaryModeratorList.get(index));
      Group group = new Group(mod);
      group.setId(idList.get(index));
      group.setName(mod, groupNameList.get(index));
      group.setActive(isActiveList.get(index));
      subGroups.add(group);
      index++;
    }
    return subGroups;
  }

  @Override
  public void createUserAliasForGroup(String alias, User user, Group group) {
    String createUserAliasForGroupSQL = "INSERT INTO `tautdb`.`member_aliases` (`username`, "
            + "`group_name`, `alias`) VALUES ('" + user.getName() + "', '" + group.getName()
            + "', '" + alias + "')";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(createUserAliasForGroupSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Create User Alias for Group SQL blew up: " + e.toString());
    }
  }

  @Override
  public void deleteUserAliasForGroup(User user, Group group) {
    String deleteModeratorSQL = "DELETE FROM `tautdb`.`member_aliases` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(deleteModeratorSQL);
    } catch (SQLException e) {
      logging.log(Level.INFO, "Delete User Alias SQL blew up: " + e.toString());
    }
  }

  @Override
  public boolean userAliasForGroupExists(User user, Group group) {
    String groupExistsSQL = "SELECT * FROM `tautdb`.`member_aliases` WHERE `group_name`='"
            + group.getName() + ANDUSERNAME + user.getName() + "'";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
         Statement statement = connection.createStatement()) {
      try (ResultSet results = statement.executeQuery(groupExistsSQL)) {
        return results.next();
      }
    } catch (SQLException e) {
      logging.log(Level.INFO, "Does Group exist SQL blew up: " + e.toString());
    }
    return false;
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
}
