package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class GroupDatabaseImplTest {

  private GroupDatabaseImpl groupDatabase = new GroupDatabaseImpl();
  private UserDatabaseImpl userImplTest = new UserDatabaseImpl();
  private User testPerson;
  private User testIndividual;
  private User testHuman;
  private Group testGroup;
  private Group testSubGroup;
  private Group testGroupOther;

  @Before
  public void setUp() {
    testPerson = new User.UserBuilder()
            .setName("HughMann")
            .setSearchable(true)
            .setStatus(UserStatus.ONLINE)
            .build();

    testIndividual = new User.UserBuilder()
            .setName("NotarObot")
            .setSearchable(true)
            .setStatus(UserStatus.OFFLINE)
            .build();

    testHuman = new User.UserBuilder()
            .setName("Bender")
            .setSearchable(false)
            .setStatus(UserStatus.ONLINE)
            .build();

    userImplTest.createUser(testPerson);
    userImplTest.createUser(testIndividual);
    userImplTest.createUser(testHuman);

    testGroup = new Group(testPerson);
    testGroup.setActive(true);
    testGroup.setName(testPerson, "Super Cool Group");
    groupDatabase.createGroup(testGroup);

    testSubGroup = new Group(testIndividual);
    testSubGroup.setActive(false);
    testSubGroup.setName(testIndividual, "Baby Group");
    groupDatabase.createGroup(testSubGroup);

    testGroupOther = new Group(testHuman);
    testGroupOther.setActive(true);
    testGroupOther.setName(testHuman, "Other collection of folks");
    groupDatabase.createGroup(testGroupOther);
  }

  private void setUpSubGroups() {
    groupDatabase.createSubGroupForGroup(testGroup, testSubGroup);
    groupDatabase.createSubGroupForGroup(testGroup, testGroupOther);
  }

  private void setUpModeratorTable() {
    groupDatabase.createModeratorForGroup(testPerson, testGroup);
    groupDatabase.createModeratorForGroup(testIndividual, testSubGroup);
    groupDatabase.createModeratorForGroup(testHuman, testGroupOther);
  }

  private void setUpUserAlias() {
    groupDatabase.createUserAliasForGroup("McTesterson", testIndividual, testSubGroup);
  }

  private void setUpUserGroupMembers() {
    groupDatabase.addUserToGroup(testPerson, testGroup);
    groupDatabase.addUserToGroup(testIndividual, testSubGroup);
    groupDatabase.addUserToGroup(testHuman, testGroupOther);
    groupDatabase.addUserToGroup(testIndividual, testGroup);
    groupDatabase.addUserToGroup(testHuman, testGroup);
  }

  @After
  public void tearDown() {
    groupDatabase.truncateMemberAliases();
    groupDatabase.truncateGroupMembers();
    groupDatabase.truncateSubgroups();
    groupDatabase.truncateModerators();
    groupDatabase.truncateGroups();
    userImplTest.deleteAllUsers();
  }

  @Test
  public void createGroup() {
    Group localGroup = new Group(testHuman);
    localGroup.setName(testHuman, "temp");
    localGroup.setActive(true);
    groupDatabase.createGroup(localGroup);
    assertTrue(groupDatabase.groupExists(localGroup.getName()));
  }

  @Test
  public void findAllGroups() {
    assertTrue(groupDatabase.findAllGroups().size() > 0);
  }

  @Test
  public void findGroupByName() {
    assertTrue("Super Cool Group".equalsIgnoreCase(
            groupDatabase.findGroupByName(testGroup.getName()).getName()));
  }

  @Test
  public void groupExists() {
    assertTrue(groupDatabase.groupExists(testGroupOther.getName()));
  }

  @Test
  public void updateGroup() {
    testGroupOther.setActive(true);
    groupDatabase.updateGroup(testGroupOther);
    assertTrue(groupDatabase.isGroupActive(testGroupOther));
  }

  @Test
  public void deleteGroup() {
    groupDatabase.deleteGroup(testSubGroup);
    assertFalse(groupDatabase.groupExists(testSubGroup.getName()));
  }

  @Test
  public void isGroupActive() {
    assertTrue(groupDatabase.isGroupActive(testGroup));
    assertFalse(groupDatabase.isGroupActive(testSubGroup));
  }

  @Test
  public void findPrimaryModerator() {
    assertEquals(testPerson, groupDatabase.findPrimaryModerator(testGroup));
  }

  @Test
  public void findAllModeratorsByGroup() {
    setUpModeratorTable();
    assertTrue(groupDatabase.findAllModeratorsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllModerators() {
    setUpModeratorTable();
    assertTrue(groupDatabase.findAllModerators().size() > 0);
  }

  @Test
  public void findAllSubGroupsByGroup() {
    setUpSubGroups();
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testGroup).size() > 0);
    assertFalse(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllSubGroups() {
    setUpSubGroups();
    assertTrue(groupDatabase.findAllSubGroups().size() > 0);
  }

  @Test
  public void findUserAlias() {
    setUpUserAlias();
    assertTrue("McTesterson".equalsIgnoreCase(groupDatabase.
            findUserAlias(testSubGroup, testIndividual)));
  }

  @Test
  public void createModeratorForGroup() {
    setUpModeratorTable();
    groupDatabase.createModeratorForGroup(testHuman, testGroup);
    assertTrue(groupDatabase.findAllModeratorsByGroup(testGroup).size() > 1);
  }

  @Test
  public void deleteModeratorForGroup() {
    setUpModeratorTable();
    groupDatabase.createModeratorForGroup(testHuman, testGroup);
    groupDatabase.deleteModeratorForGroup(testHuman, testGroup);
    assertEquals(1, groupDatabase.findAllModeratorsByGroup(testGroup).size());
  }

  @Test
  public void moderatorForGroupExists() {
    setUpModeratorTable();
    assertTrue(groupDatabase.moderatorForGroupExists(testPerson, testGroup));
  }

  @Test
  public void createSubGroupForGroup() {
    setUpSubGroups();
    groupDatabase.createSubGroupForGroup(testSubGroup, testGroup);
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void subgroupForGroupExists() {
    setUpSubGroups();
    assertTrue(groupDatabase.subgroupForGroupExists(testGroup, testSubGroup));
    assertFalse(groupDatabase.subgroupForGroupExists(testSubGroup, testGroupOther));
  }

  @Test
  public void deleteSubGroupForGroup() {
    setUpSubGroups();
    groupDatabase.createSubGroupForGroup(testSubGroup, testGroup);
    assertTrue(groupDatabase.subgroupForGroupExists(testSubGroup, testGroup));
    groupDatabase.deleteSubGroupForGroup(testSubGroup, testGroup);
    assertFalse(groupDatabase.subgroupForGroupExists(testSubGroup, testGroup));
  }

  @Test
  public void createUserAliasForGroup() {
    groupDatabase.createUserAliasForGroup("Godzilla", testHuman, testGroupOther);
    assertTrue("Godzilla".equalsIgnoreCase(groupDatabase.findUserAlias(testGroupOther, testHuman)));
  }

  @Test
  public void deleteUserAliasForGroup() {
    groupDatabase.createUserAliasForGroup("Godzilla", testHuman, testGroupOther);
    groupDatabase.deleteUserAliasForGroup(testHuman, testGroupOther);
    assertFalse("Godzilla".equalsIgnoreCase(groupDatabase.findUserAlias(testGroupOther, testHuman)));
  }

  @Test
  public void userAliasForGroupExists() {
    setUpUserAlias();
    assertTrue(groupDatabase.userAliasForGroupExists(testIndividual, testSubGroup));
  }

  @Test
  public void addUserToGroup() {
    setUpUserGroupMembers();
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    assertTrue(groupDatabase.findAllGroupMembers(testGroupOther).size() > 1);
  }

  @Test
  public void deleteUserFromGroup() {
    setUpUserGroupMembers();
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    groupDatabase.deleteUserFromGroup(testIndividual, testGroupOther);
    assertEquals(1, groupDatabase.findAllGroupMembers(testGroupOther).size());
  }

  @Test
  public void isUserMemberOfGroup() {
    setUpUserGroupMembers();
    assertTrue(groupDatabase.isUserMemberOfGroup(testPerson, testGroup));
  }

  @Test
  public void findAllGroupMembersTest() {
    setUpUserGroupMembers();
    assertTrue(groupDatabase.findAllGroupMembers(testGroup).size() > 2);
  }

  @Test
  public void truncateGroups() {
    groupDatabase.truncateGroups();
    assertTrue(groupDatabase.findAllGroups().isEmpty());
  }

  @Test
  public void truncateGroupMembers() {
    setUpUserGroupMembers();
    groupDatabase.truncateGroupMembers();
    assertTrue(groupDatabase.findAllGroupMembers(testGroup).isEmpty());
  }

  @Test
  public void truncateModerators() {
    setUpModeratorTable();
    groupDatabase.truncateModerators();
    assertTrue( groupDatabase.findAllModerators().isEmpty());
  }

  @Test
  public void truncateSubgroups() {
    setUpSubGroups();
    groupDatabase.truncateSubgroups();
    assertTrue(groupDatabase.findAllSubGroups().isEmpty());
  }

  @Test
  public void truncateMemberAliases() {
    setUpUserAlias();
    groupDatabase.truncateMemberAliases();
    assertNull(groupDatabase.findUserAlias(testSubGroup, testIndividual));
  }
}