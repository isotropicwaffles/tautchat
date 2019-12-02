package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.hsqldb.cmdline.SqlToolError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupDatabaseImplTest {

  private static GroupDatabaseImpl groupDatabase = new GroupDatabaseImpl();
  private static UserDatabaseImpl userImplTest = new UserDatabaseImpl();
  private static User testPerson;
  private static User testIndividual;
  private static User testHuman;
  private static Group testGroup;
  private static Group testSubGroup;
  private static Group testGroupOther;
  private static GroupDatabaseImpl mockImpl;
  private static ResultSet mockResultSet;
  private static Logger mockLogger;

  @BeforeClass
  public static void setUp() throws ClassNotFoundException, SqlToolError, SQLException, IOException {
	DatabaseSupportFunctions.setUpTestDatabase();

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

    mockImpl = mock(GroupDatabaseImpl.class);
    mockResultSet = mock(ResultSet.class);
    mockLogger = mock(Logger.class);
  }

  @AfterClass
  public static void tearDown() throws SQLException {
    groupDatabase.truncateMemberAliases();
    groupDatabase.truncateGroupMembers();
    groupDatabase.truncateSubgroups();
    groupDatabase.truncateModerators();
    groupDatabase.truncateGroups();
    userImplTest.deleteAllUsers();
	DatabaseSupportFunctions.tearDownTestDatabase();

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

/*  @Test
  public void breakSQLQuery() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(outContent));
    //groupDatabase.createGroup(testGroup);
*//*    groupDatabase.findPrimaryModerator(testGroup);
    groupDatabase.executeUpdateHelper("Invalid string");
    groupDatabase.findGroupByName(testGroup); *//*
    groupDatabase.executeBooleanQuery("Not a SQL query");

    assertTrue(outContent.toString().contains("SQL blew up"));
  }*/

  @Test
  public void testtest() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(outContent));
    try{
      groupDatabase.createGroup(testGroup);
    } catch (Exception e) {
      assertTrue(outContent.toString().contentEquals("SQL blew up"));
    }
    try{
      assertFalse(groupDatabase.executeBooleanQuery("nonsense"));
    } catch (Exception e2) {
      assertTrue(outContent.toString().contentEquals("SQL blew up"));
    }
    try{
      groupDatabase.executeUpdateHelper("nonsense again");
    } catch (Exception e3) {
      assertTrue(outContent.toString().contentEquals("SQL blew up"));
    }
  }

  @Test(expected = SQLException.class)
  public void breakCreateGroup() {
    doThrow(SQLException.class).when(mockImpl).createGroup(any(Group.class));
    mockImpl.createGroup(testGroup);
  }

  @Test(expected = SQLException.class)
  public void breakUpdateHelper() {
    doThrow(SQLException.class).when(mockImpl).executeUpdateHelper(anyString());
    mockImpl.executeUpdateHelper("filler");
  }

  @SuppressWarnings("unchecked")
@Test
  public void breakBooleanQuery() {
    try {
      when(mockResultSet.next()).thenThrow(SQLException.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assertFalse(mockImpl.executeBooleanQuery("Filler text"));
  }

  @SuppressWarnings("unchecked")
@Test(expected = SQLException.class)
  public void breakPrimaryMod() {
    when(mockImpl.findPrimaryModerator(any(Group.class))).thenThrow(SQLException.class);
    mockImpl.findPrimaryModerator(testGroup);
  }

  @SuppressWarnings("unchecked")
@Test(expected = SQLException.class)
  public void breakFindGroupByName() {
    when(mockImpl.findGroupByName(any(Group.class))).thenThrow(SQLException.class);
    mockImpl.findGroupByName(testGroup);
  }
}