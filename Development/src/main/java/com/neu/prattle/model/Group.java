package com.neu.prattle.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.neu.prattle.service.user.UserService;
import com.neu.prattle.service.user.UserServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;

/**
 * Group object which can be composed of an arbitrary
 * number of Users.  Groups can also have/be subgroups.
 * @author jdwilson73
 */
public class Group {

	/**
	 * Whether or not the group is still active.
	 * If Group is deactivated, no changes can be made
	 * to Group's fields.  Additionally, Group will be
	 * invisible in all searches by Users who are not
	 * moderators of the Group.  Also no messages may
	 * be routed to/from the deactivated Group.
	 * If Group was deactivated while serving as a
	 * sub/supergroup of another Group, that status will
	 * not be retained--i.e. the Group will have to
	 * re-merge manually.
	 */
	private boolean isActive;
	
	/**
	 * Set of Users serving as moderators for this Group.
	 * Every Group must have at least one moderator.
	 */
	private Set<User> moderators;
	
	/**
	 * Current name of the Group (may change).
	 */
	private String groupName;
	
	/**
	 * Tracks the subgroups composing this Group.
	 */
	private Set<Group> subgroups;
	
	/**
	 * Tracks the groups which have this Group as a subgroup.
	 */
	private Set<Group> supergroups;
	
	/**
	 * Tracks the current alias of each User in the Group.
	 * An alias is the specific name used by a User in the
	 * given Group.
	 */
	private Map<User, String> memberAliases;
	
	/**
	 * Contains Users who have attempted to join the Group
	 * but who have not yet been approved by a moderator.
	 */
	private Set<User> joinUserQueue;
	
	/**
	 * Contains Groups who have attempted to join the Group
	 * but who have not yet been approved by a moderator.
	 */
	private Set<Group> joinSubGroupQueue;
	
	/**
	 * Creates a Group with only a single moderator.
	 * @param moderator initial mod for the Group
	 */
	public Group(User moderator) {
		
		this.isActive = true;
		
		// Poll UserService to check that User is valid/exists
		// E.g. trying to make a bot a mod should fail.
		UserService serv = UserServiceImpl.getInstance();
		if (!validUser(moderator, serv)) {
			throw new IllegalArgumentException("User is not authorized to moderate this group.");
		}

		this.moderators = new LinkedHashSet<>();
		this.moderators.add(moderator);
		
		this.subgroups = new LinkedHashSet<>();
		this.supergroups = new LinkedHashSet<>();
		
		this.memberAliases = new LinkedHashMap<>();
		this.memberAliases.put(moderator, moderator.getName());
		
		this.joinUserQueue = new LinkedHashSet<>();
		this.joinSubGroupQueue = new LinkedHashSet<>();
	}

	// Called by builder
	private Group(String name, Set<User> moderators, 
			Map<User, String> memberAliases,
			Set<Group> subgroups, Set<Group> supergroups) {
		
		this.isActive = true;
		
		// Poll UserService to check that User is valid/exists
		// E.g. trying to make a bot a mod should fail.
		UserService serv = UserServiceImpl.getInstance();
		for (User mod : moderators) {
			if (!validUser(mod, serv)) {
				throw new IllegalArgumentException("User is not authorized to moderate this group.");
			}
		}
		
		this.groupName = name;
		
		this.moderators = moderators;
		
		this.subgroups = subgroups;
		this.supergroups = supergroups;
		
		this.memberAliases = memberAliases;
		
		this.joinUserQueue = new LinkedHashSet<>();
		this.joinSubGroupQueue = new LinkedHashSet<>();
	}

	private boolean validUser(User moderator, UserService serv) {
		return serv.findUserByName(moderator.getName()).isPresent() && !moderator.userIsBot();
	}
	
	/**
	 * Returns current name of Group.
	 * @return group name
	 */
	public String getName() {
		return this.groupName;
	}
	
	/**
	 * Sets the current name of Group.
	 * @param mod the executing moderator
	 * @param name the new group name
	 */
	public void setName(User mod, String name) {
		if (this.isActive) {
			this.groupName = name;
		}
	}
	
	/**
	 * Determines whether or not the given User is a
	 * member of this Group.
	 * @param user the potential member
	 * @return true if the member belongs to the Group, false otherwise
	 */
	public boolean hasMember(User user) {
		return this.memberAliases.containsKey(user);
	}
	
	
	/**
	 * Returns a set of all all user add requests of this group.
	 * @return set of members
	 */
	public Set<User> getPendingUserRequests() {
		return this.joinUserQueue;
	}
	
	/**
	 * Returns a set of all all subgroup add requests of this group.
	 * @return set of members
	 */
	public Set<Group> getPendingSubGroupRequests() {
		return this.joinSubGroupQueue;
	}
	
	/**
	 * Returns a set of all the Users who are members of this Group.
	 * @return set of members
	 */
	public Set<User> getMembers() {
		return this.memberAliases.keySet();
	}
	
	private boolean authenticateAsMod(User user, Group group) {
		return group.moderators.contains(user);
	}
	
	/**
	 * If executed by a moderator, Group is deactivated.
	 * I.e. The Group may no longer have its fields changed
	 * and will not be visible to general Users.
	 * @param mod the executing moderator
	 */
	public void deactivate(User mod) {
		if (this.isActive && authenticateAsMod(mod, this)) {
			// Remove this group from all supergroups' subgroup list
			for (Group supergroup : this.supergroups) {
				supergroup.removeSubgroup(mod, this);
			}
			// Remove this group from all subgroups' supergroup list
			for (Group subgroup : this.subgroups) {
				subgroup.removeSupergroup(mod, this);
			}
			this.isActive = false;
		}
	}
	
	/**
	 * If executed by a moderator, Group is reactivated.
	 * I.e. The Group may once again have its fields changed
	 * and be visible to general Users.
	 * @param mod the executing moderator
	 */
	public void reactivate(User mod) {
		if (authenticateAsMod(mod, this)) {
			this.isActive = true;
		}
	}
	
	/**
	 * Adds given Group to this Group's set of subgroups.
	 * By extension, also adds this group to given Group's
	 * set of supergroups.
	 * @param mod the executing moderator
	 * @param subgroup Group to be added to association
	 */
	public void addSubgroup(User mod, Group subgroup) {
		if (subgroup == null || subgroup.equals(this)) {
			return;
		}
		
		if (this.isActive && authenticateAsMod(mod, this)) {
			// If subgroup already exists, then that means
			// this is likely the second call of the method,
			// so mission accomplished.
			if (this.subgroups.contains(subgroup)) {
				return;
			}
			this.subgroups.add(subgroup);
			// Preserve symmetry by adding this as child to supergroup.
			subgroup.addSupergroup(mod, this);
		} else {
			joinSubGroupQueue.add(subgroup);
		}
	}
	
	/**
	 * Removes given Group from this Group's set of subgroups.
	 * By extension, also removes this group from given Group's
	 * set of supergroups.
	 * @param mod the executing moderator
	 * @param subgroup Group to be removed from association
	 */
	public void removeSubgroup(User mod, Group subgroup) {
		
		if (this.isActive && (authenticateAsMod(mod, this)) ||
				authenticateAsMod(mod, subgroup)){
			// If subgroup already exists, then that means
			// this is likely the second call of the method,
			// so mission accomplished.
			if (this.subgroups.contains(subgroup)) {
				this.subgroups.remove(subgroup);
			} else if (joinSubGroupQueue.contains(subgroup)) {
				this.joinSubGroupQueue.remove(subgroup);
			}
		}
		
		

			// Preserve symmetry by adding this as child to supergroup.
			subgroup.removeSupergroup(mod, this);
		}

	/**
	 * Adds given Group to this Group's set of supergroups.
	 * By extension, also adds this group to given Group's
	 * set of subgroups.
	 * @param mod the executing moderator
	 * @param supergroup Group to be added to association
	 */
	public void addSupergroup(User mod, Group supergroup) {
		if (supergroup == null || supergroup.equals(this)) {
			return;
		}
		
		if (this.isActive && authenticateAsMod(mod, this)) {
			// If subgroup already exists, then that means
			// this is likely the second call of the method,
			// so mission accomplished.
			if (this.supergroups.contains(supergroup)) {
				return;
			}
			this.supergroups.add(supergroup);
			// Preserve symmetry by adding this as child to supergroup.
			supergroup.addSubgroup(mod, this);
		} 
	}
	
	/**
	 * Removes given Group from this Group's set of supergroups.
	 * By extension, also removes this group from given Group's
	 * set of subgroups.
	 * @param mod the executing moderator
	 * @param supergroup Group to be removed from association
	 */
	public void removeSupergroup(User mod, Group supergroup) {
		
		if (this.isActive && (authenticateAsMod(mod, this)) ||
				authenticateAsMod(mod, supergroup)) {
			// If subgroup already exists, then that means
			// this is likely the second call of the method,
			// so mission accomplished.
			if (!this.supergroups.contains(supergroup)) {
				return;
			}
			this.supergroups.remove(supergroup);
			// Preserve symmetry by adding this as child to supergroup.
			supergroup.removeSubgroup(mod, this);
		}
	}
	
	/**
	 * Sends a message to all members of the addressed Group
	 * and its subgroups.  A member belonging to multiple of
	 * these Groups will receive the message once for each
	 * included Group they are a part of.
	 * @param message the sent message
	 */
	public void messageMembers(Message message) {
		message.setFrom(this.groupName);
		
		for (Group subgroup : this.subgroups) {
			subgroup.messageMembers(message);
		}
		
		for (User member : this.memberAliases.keySet()) {
			message.setTo(member.getName());
			ChatEndpoint.directedMessage(message);
		}
	}
	
	public Set<Group> getSubGroups() {
		return this.subgroups;
	}
	
	public Set<Group> getSuperGroups() {
		return this.supergroups;
	}
	
	public boolean hasSubGroup(Group group) {
		return this.subgroups.contains(group);
	}
	
	public Set<User> getModerators() {
		return this.moderators;
	}
	
	public void addUser(User mod, User user) {
		if (user == null) {
			return;
		}
		
		if (authenticateAsMod(mod, this)) {
			this.memberAliases.putIfAbsent(user, user.getName());
		}
		else {
			this.joinUserQueue.add(user);
		}
	}
	
	public void removeUser(User mod, User user) {
		// User can only be removed by the same user or a mod.
		if (mod.equals(user) || authenticateAsMod(mod, this)) {
			
			if (this.memberAliases.containsKey(user)) {
				this.memberAliases.remove(user);
			} else if(this.joinUserQueue.contains(user)) {
				this.joinUserQueue.remove(user);
			}
		}
	}
	
	public static class GroupBuilder {
		private String groupName;
		private Set<User> moderators;
		private Map<User, String> memberAliases;
		private Set<Group> subgroups;
		private Set<Group> supergroups;
		
		public GroupBuilder() {
			this.groupName = "";
			this.moderators = new LinkedHashSet<>();
			this.memberAliases = new LinkedHashMap<>();
			this.subgroups = new LinkedHashSet<>();
			this.supergroups = new LinkedHashSet<>();
		}
		
		public Group build() throws IllegalArgumentException {
			if (groupName.isEmpty() && !moderators.isEmpty()) {
				return new Group(groupName, moderators, 
						memberAliases, subgroups, supergroups);
			}
			
			throw new IllegalArgumentException(
					"Must provide valid group name and at" + 
					" least one moderator.");
		}
		
		public GroupBuilder addUser(User user) {
			if (user != null) {
				this.memberAliases.putIfAbsent(user, user.getName());
			}
			
			return this;
		}
		
		public GroupBuilder addModerator(User user) {
			if (user != null) {
				this.moderators.add(user);
			}
			
			return this;
		}
		
		public GroupBuilder setAlias(User user, String alias) {
			// N.B. '~' denotes Service account
			if (!alias.isEmpty() && alias.charAt(0) != '~') {
				this.memberAliases.replace(user, alias);
			}
			
			return this;
		}
		
		public GroupBuilder addSubGroup(Group subgroup) {
			if (subgroup != null) {
				this.subgroups.add(subgroup);
			}
			
			return this;
		}
		
		public GroupBuilder addSuperGroup(Group supergroup) {
			if (supergroup != null) {
				this.supergroups.add(supergroup);
			}
			
			return this;
		}
		
		public GroupBuilder setName(String name) {
			if (name.isEmpty() && name.charAt(0) != '~') {
				this.groupName = name;
			}
			
			return this;
		}
	}
}
