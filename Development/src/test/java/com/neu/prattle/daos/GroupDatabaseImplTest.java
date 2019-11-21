package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class GroupDatabaseImplTest {

  private static GroupDatabaseImpl groupDatabase = new GroupDatabaseImpl();
  private static UserDatabaseImpl userImplTest = new UserDatabaseImpl();
  private static User testPerson;
  private static User testIndividual;
  private static User testHuman;
  private static Group testGroup;
  private static Group testSubGroup;
  private static Group testGroupOther;

  @BeforeClass
  public static void setUp() {
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

    groupDatabase.createSubGroupForGroup(testGroup, testSubGroup);
    groupDatabase.createSubGroupForGroup(testGroup, testGroupOther);

    groupDatabase.createModeratorForGroup(testPerson, testGroup);
    groupDatabase.createModeratorForGroup(testIndividual, testSubGroup);
    groupDatabase.createModeratorForGroup(testHuman, testGroupOther);


    groupDatabase.createUserAliasForGroup("McTesterson", testIndividual, testSubGroup);
    groupDatabase.addUserToGroup(testPerson, testGroup);
    groupDatabase.addUserToGroup(testIndividual, testSubGroup);
    groupDatabase.addUserToGroup(testHuman, testGroupOther);
    groupDatabase.addUserToGroup(testIndividual, testGroup);
    groupDatabase.addUserToGroup(testHuman, testGroup);
  }

  @AfterClass
  public static void tearDown() {
    groupDatabase.truncateMemberAliases();
    groupDatabase.truncateGroupMembers();
    groupDatabase.truncateSubgroups();
    groupDatabase.truncateModerators();
    groupDatabase.truncateGroups();
    userImplTest.deleteAllUsers();
  }

  @Test
  public void findAllGroups() {
    assertTrue(groupDatabase.findAllGroups().size() > 0);
  }

  @Test
  public void findGroupByName() {
    assertTrue("Super Cool Group".equalsIgnoreCase(
            groupDatabase.findGroupByName(testGroup).getName()));
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
    assertFalse(groupDatabase.groupExists(testSubGroup));
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
    assertTrue(groupDatabase.findAllModeratorsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllModerators() {
    assertTrue(groupDatabase.findAllModerators().size() > 0);
  }

  @Test
  public void deleteModerator() {
    groupDatabase.createModeratorForGroup(testPerson, testSubGroup);
    assertTrue(groupDatabase.moderatorForGroupExists(testPerson, testSubGroup));
    groupDatabase.deleteModeratorForGroup(testPerson, testSubGroup);
    assertFalse(groupDatabase.moderatorForGroupExists(testPerson, testSubGroup));
  }

  @Test
  public void findAllSubGroupsByGroup() {
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testGroup).size() > 0);
    assertFalse(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllSubGroups() {
    assertTrue(groupDatabase.findAllSubGroups().size() > 0);
  }

  @Test
  public void findUserAlias() {
    assertTrue("McTesterson".equalsIgnoreCase(groupDatabase.
            findUserAlias(testSubGroup, testIndividual)));
  }

  @Test
  public void moderatorForGroupExists() {
    assertTrue(groupDatabase.moderatorForGroupExists(testPerson, testGroup));
  }

  @Test
  public void subgroupForGroupExists() {
    assertTrue(groupDatabase.subgroupForGroupExists(testGroup, testSubGroup));
    assertFalse(groupDatabase.subgroupForGroupExists(testSubGroup, testGroupOther));
  }

  @Test
  public void deleteSubGroupForGroup() {
    groupDatabase.createSubGroupForGroup(testSubGroup, testGroup);
    assertTrue(groupDatabase.subgroupForGroupExists(testSubGroup, testGroup));
    groupDatabase.deleteSubGroupForGroup(testSubGroup, testGroup);
    assertFalse(groupDatabase.subgroupForGroupExists(testSubGroup, testGroup));
  }

  @Test
  public void deleteUserAliasForGroup() {
    groupDatabase.createUserAliasForGroup("Godzilla", testHuman, testGroupOther);
    groupDatabase.deleteUserAliasForGroup(testHuman, testGroupOther);
    assertFalse("Godzilla".equalsIgnoreCase(groupDatabase.findUserAlias(testGroupOther, testHuman)));
  }

  @Test
  public void userAliasForGroupExists() {
    assertTrue(groupDatabase.userAliasForGroupExists(testIndividual, testSubGroup));
  }

  @Test
  public void addUserToGroup() {
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    assertTrue(groupDatabase.isUserMemberOfGroup(testIndividual, testGroupOther));
  }

  @Test
  public void deleteUserFromGroup() {
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    groupDatabase.deleteUserFromGroup(testIndividual, testGroupOther);
    assertFalse(groupDatabase.isUserMemberOfGroup(testIndividual, testGroupOther));
  }

  @Test
  public void findAllGroupMembersTest() {
    assertTrue(groupDatabase.findAllGroupMembers(testGroup).size() > 2);
  }

  @Test
  public void breakSQLQuery() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(outContent));
    groupDatabase.createGroup(testGroup);
/*    groupDatabase.findPrimaryModerator(testGroup);
    groupDatabase.executeUpdateHelper("Invalid string");
    groupDatabase.findGroupByName(testGroup);
    groupDatabase.executeBooleanQuery("Not a SQL query");*/

    assertTrue(outContent.toString().contains("SQL blew up"));
  }

}