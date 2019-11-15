package com.neu.prattle.service.group;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupNotPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserServiceImpl;

import java.util.Iterator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/***
 * Implementation of {@link GroupService}
 *
 * It stores the user accounts in-memory, which means any user accounts
 * created will be deleted once the application has been restarted.
 *
 * @author Richard Showalter-Bucher
 * @version dated 2019/11/10
 */
public class GroupServiceImpl implements GroupService {

    /***
     * GroupServiceImpl is a Singleton class.
     */
    private GroupServiceImpl() {

    }

    /***
     * Variable to store the singleton instance
     * 
     */
    private static GroupService accountService = null;


    /**
     * Call this method to return an instance of this service.
     * @return this
     */
    public static GroupService getInstance() {
    	
    	if (accountService == null) {
    		
    		groupMap = new HashMap<>();
            accountService = new GroupServiceImpl();
    	}
    	
        return accountService;
    }


    
    /** The sessions. */
    private static Map<String,Group> groupMap = new HashMap<>();

    /***
     *
     * @param name -> The name of the group.
     * @return An optional wrapper supplying the group.
     */
    @Override
    public Optional<Group> findGroupByName(String name) {
    	
    	Group group = groupMap.get(name);
    	
    	if (group != null) {
    		return Optional.of(group);
    	}
    	else {
    		return Optional.empty();
    	}
    }
    
    

    /***
     * Attempts to return the name of a group and throws and error if the group doesn't exist
     *
     * @param name -> The name of the group.
     * @return The associated group.
     * @throws error if group does not exist
     */
    @Override
	public Group protectedFindGroupByName(String name) {
		
		Optional<Group> potentialGroup = findGroupByName(name); 

		if (potentialGroup.isPresent()) {
			return potentialGroup.get();
		}
		else {

			throw new GroupNotPresentException(String.format("Group %s could not be found", name));
		}
	}
    

    /**
 	*	Adds group to Service 
	*	
	*	@param group : group to add
 	*/
    @Override
    public synchronized void addGroup(Group group) {
    	
    	Optional<Group> groupToAdd = accountService.findGroupByName(group.getName());
    	
        if (groupToAdd.isPresent()) {
        	throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
        }
          
        groupMap.put(group.getName(), group);
        
    }
    
    
    /**
 	*	Delets group to Service 
	*	
	*	@param group : group to add
 	*/
    @Override
    public synchronized void deleteGroup(Group group) {
    	
    	groupMap.remove(group.getName());
    }
    
    
    /***
     * Returns a set if Groups that the memember is a part of
     * @param user - The user to query groups for
     * @return Set of group objects.
     */
    @Override

    public Set<Group> getUserGroupMemberships(User user){
		
    	Set<Group> userGroups = new HashSet<>();
    	
    	
    	Iterator<Map.Entry<String,Group>> it =  groupMap.entrySet().iterator();
        
    	while (it.hasNext()) {
        	Map.Entry<String,Group> pair = it.next();
            if (pair.getValue().hasMember(user)) {
            	userGroups.add(pair.getValue());
            }
            	
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        return userGroups;
    	
    	
    }
    
    /**
     * Call this method to clear the current instance of this service.
     * 
     */
    public static void clear() {
        accountService=null;
    }

}
