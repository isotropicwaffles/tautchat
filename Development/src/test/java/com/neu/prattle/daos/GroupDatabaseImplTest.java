package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    testPerson = new User();
    testPerson.setName("HughMann");
    testPerson.setSearchable(true);
    testPerson.setStatus(UserStatus.ONLINE);
    testIndividual = new User();
    testIndividual.setName("NotarObot");
    testIndividual.setSearchable(true);
    testIndividual.setStatus(UserStatus.OFFLINE);
    testHuman = new User();
    testHuman.setName("Bender");
    testHuman.setSearchable(false);
    testHuman.setStatus(UserStatus.ONLINE);
    if (!userImplTest.userExists(testPerson.getName())) {
      userImplTest.createUser(testPerson);
    }
    if (!userImplTest.userExists(testIndividual.getName())) {
      userImplTest.createUser(testIndividual);
    }
    if (!userImplTest.userExists(testHuman.getName())) {
      userImplTest.createUser(testHuman);
    }
    testGroup = new Group(testPerson);
    testGroup.setActive(true);
    testGroup.setName(testPerson, "Super Cool Group");
    if (!groupDatabase.groupExists(testGroup.getName())) {
      groupDatabase.createGroup(testGroup);
    }
    testSubGroup = new Group(testIndividual);
    testSubGroup.setActive(false);
    testSubGroup.setName(testIndividual, "Baby Group");
    if (!groupDatabase.groupExists(testSubGroup.getName())) {
      groupDatabase.createGroup(testSubGroup);
    }
    testGroupOther = new Group(testHuman);
    testGroupOther.setActive(true);
    testGroupOther.setName(testHuman, "Other collection of folks");
    if (!groupDatabase.groupExists(testGroupOther.getName())) {
      groupDatabase.createGroup(testGroupOther);
    }
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

  @After
  public void tearDown() {
    groupDatabase.deleteUserAliasForGroup(testIndividual, testSubGroup);
    groupDatabase.deleteUserFromGroup(testPerson, testGroup);
    groupDatabase.deleteUserFromGroup(testIndividual, testSubGroup);
    groupDatabase.deleteUserFromGroup(testHuman, testGroupOther);
    groupDatabase.deleteUserFromGroup(testIndividual, testGroup);
    groupDatabase.deleteUserFromGroup(testHuman, testGroup);
    groupDatabase.deleteModeratorForGroup(testPerson, testGroup);
    groupDatabase.deleteModeratorForGroup(testIndividual, testSubGroup);
    groupDatabase.deleteModeratorForGroup(testHuman, testGroupOther);
    groupDatabase.deleteSubGroupForGroup(testGroup, testSubGroup);
    groupDatabase.deleteSubGroupForGroup(testGroup, testGroupOther);
    userImplTest.deleteUserByUsername("HughMann");
    userImplTest.deleteUserByUsername("NotarObot");
    userImplTest.deleteUserByUsername("Bender");
    groupDatabase.deleteGroup(testGroup);
    groupDatabase.deleteGroup(testSubGroup);
    groupDatabase.deleteGroup(testGroupOther);
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
    assertTrue(groupDatabase.findAllModeratorsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllModerators() {
    assertTrue(groupDatabase.findAllModerators().size() > 0);
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
  public void createModeratorForGroup() {
  }

  @Test
  public void deleteModeratorForGroup() {
  }

  @Test
  public void moderatorForGroupExists() {
  }

  @Test
  public void createSubGroupForGroup() {
  }

  @Test
  public void subgroupForGroupExists() {
  }

  @Test
  public void deleteSubGroupForGroup() {
  }

  @Test
  public void createUserAliasForGroup() {
  }

  @Test
  public void deleteUserAliasForGroup() {
  }

  @Test
  public void userAliasForGroupExists() {
  }

  @Test
  public void addUserToGroup() {
  }

  @Test
  public void deleteUserFromGroup() {
  }

  @Test
  public void isUserMemberOfGroup() {
  }

  @Test
  public void findAllGroupMembersTest() {

  }
}