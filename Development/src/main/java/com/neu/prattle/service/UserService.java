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
 * to perform all the CRUD operations on user accounts.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 *
 */
public interface UserService {
    /***
     * Returns an optional object which might be empty or wraps an object
     * if the System contains a {@link User} object having the same name
     * as the parameter.
     *
     * @param name The name of the user
     * @return Optional object.
     */
    Optional<User> findUserByName(String name);

    /***
     * Tries to add a user in the system
     * @param user User object
     * @throws IOException when fileWriter blows up.
     *
     */
    void addUser(User user) throws IOException;
    
    /***
     * Tries to delete a user in the system
     * @param user User object
     * @throws IOException when user doesn't exist
     *
     */
    void deleteUser(User user) throws IOException;
    
    /***
     * Tries to friend two users
     * @param user1 User object, user2 User object
     * @throws IOException when users don't exist or friendship already exists
     *
     */
    void friendUsers(User user1, User user2) throws IOException;
    
    /***
     * Tries to unfriend two users
     * @param user1 User object, user2 User object
     * @throws IOException when users don't exist or already unfriended
     *
     */
    void unfriendUsers(User user1, User user2) throws IOException;
    
    /***
     * Tries to let user follow another user
     * @param follower User object, followee User object
     * @throws IOException when users don't exist or already following
     *
     */
    void followUser(User follower, User followee) throws IOException;
    
    /***
     * Tries to let user unfollow another user
     * @param follower User object, followee User object
     * @throws IOException when users don't exist or already not following
     *
     */
    void unfollowUser(User follower, User followee) throws IOException;
    
    /***
     * Tries to get the status of the user
     * @param user User object
     * @return UserStatus object
     *
     */
    UserStatus getUserStatus(User user);
    
    /***
     * Tries to get the icon of the user
     * @param user User object
     * @return Icon object
     *
     */
    Icon getUserDefaultIcon(User user);
    
    /***
     * Tries to get the list of friends a user has
     * @param user User object
     * @return Set<User> object
     *
     */
    Set<User> getUserFriends(User user);
    
    /***
     * Tries to get the list of followers a user has
     * @param user User object
     * @return Set<User> object
     *
     */
    Set<User> getUserFollowers(User user);
    
    /***
     * Tries to get the list of followees a user has
     * @param user User object
     * @return Set<User> object
     *
     */
    Set<User> getUserFollowees(User user);
    
    /***
     * Tries to get the list of groups a user is part of
     * @param user User object
     * @return Set<Group> object
     *
     */
    Set<Group> getUserGroupMemberships(User user);
    
}
