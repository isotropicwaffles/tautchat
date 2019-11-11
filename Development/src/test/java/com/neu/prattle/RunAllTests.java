package com.neu.prattle;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.neu.prattle.daos.MessageDatabaseImplTest;
import com.neu.prattle.daos.UserDatabaseImplTest;
import com.neu.prattle.messaging.UserServiceMessageProcessorTest;;


@RunWith(Suite.class)
@SuiteClasses({UserTest.class, MessageTest.class, ChatEndpointTest.class, UserServiceMessageProcessorTest.class, MessageDatabaseImplTest.class, UserDatabaseImplTest.class})
public class RunAllTests {

}
