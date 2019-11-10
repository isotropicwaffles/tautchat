package com.neu.prattle.service;

import com.neu.prattle.model.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on Group accounts.
 *
 * @author Richard Alexander Showalter-Bucher
 * @version dated 2019-11-09
 *
 */
public interface GroupService {
    /***
     * Returns an optional object which might be empty or wraps an object
     * if the System contains a {@link User} object having the same name
     * as the parameter.
     *
     * @param name The name of the group
     * @return Optional object.
     */
    Optional<User> findGroupByName(String name);

    /***
     * Tries to add a group in the system
     * @param group Group object
     * @throws IOException when fileWriter blows up.
     *
     */
    void addGroup(Group group) throws IOException;
    
    /***
     * Tries to delete a user in the system
     * @param user User object
     * @throws IOException when user doesn't exist
     *
     */
    void deleteGroup(Group group) throws IOException;
    

    Set<Group> getUserGroupMemberships(User user);
    
}
