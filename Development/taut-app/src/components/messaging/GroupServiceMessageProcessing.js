import {genericMessageResponses} from './GeneralMessageRouter';
/* Enumeration for message types service messages
 * 
 */
const GroupServiceContent = {
	
		   /**
		   * Represents Group Create Command
		   */
		  GROUP_CREATE: 'GROUP_CREATE',

		  /**
		   * Represents Group Create Command
		   */
		  GROUP_DELETE: 'GROUP_DELETE',

		  /**
		   * Represents Invite User Command
		   */
		  INVITE_USER: 'INVITE_USER',

		  /**
		   * Represents Group Add User Command
		   */
		  APPROVE_ADD_USER: 'APPROVE_ADD_USER',

		  /**
		   * Represents Group Add User Command
		   */
		  APPROVE_ADD_GROUP: 'APPROVE_ADD_GROUP',

		  /**
		   * Represents Remove USER from Group Command
		   */
		  REMOVE_USER: 'REMOVE_USER',

		  /**
		   * Represents Remove Group from Group Command
		   */
		  REMOVE_GROUP: 'REMOVE_GROUP',

		  /**
		   * Represents a Request to Add User to GROUP Command
		   */
		  REQUEST_ADD_USER: 'ADD_USER',

		  /**
		   * Represents a Request to Add Group to Group Command
		   */
		  REQUEST_ADD_GROUP: 'ADD_GROUP',
		  /**
		   * Represents a request to get the group users
		   */
		  GET_GROUP_USERS: 'GET_GROUP_USERS',
		  /**
		   * Represents a request to get the group sub groups
		   */
		  GET_GROUP_SUBGROUPS: 'GET_GROUP_SUBGROUPS',
		  /**
		   * Represents a request to get the group super groups
		   */
		  GET_GROUP_SUPERGROUPS:'GET_GROUP_SUPERGROUPS',
		  /**
		   * Represents a request to get the group moderator
		   */
		  GET_GROUP_MODERATORS: 'GET_GROUP_MODERATORS',
		  /**
		   * Represents a request to get the pending users requests fo the group
		   */
		  GET_PENDING_USER_REQUESTS: 'GET_PENDING_USER_REQUESTS',
		  /**
		   * Represents a request to get the pending subgroup requests fo the group
		   */
		  GET_PENDING_SUBGROUP_REQUESTS: 'GET_PENDING_SUBGROUP_REQUESTS',
	   
}


/* Routes received group service messages to correct logic
 * 
 * @param json message - json of user service type message
 */
function groupServiveMessageRouter(message) {

	switch(message.contentType){
	
		case GroupServiceContent.GROUP_CREATE:
			 processGroupCreateMessage(message);
			break;
		case GroupServiceContent.GROUP_DELETE:
			processGroupDeleteMessage(message);
			break;
		case GroupServiceContent.INVITE_USER:
			processInviteUserToGroupMessage(message);
			break;
		case GroupServiceContent.APPROVE_ADD_USER:
			processGroupApproveAddUserMessage(message);
			break;
		case GroupServiceContent.APPROVE_ADD_GROUP:
			processGroupApproveAddGroupMessage(message);
			break;
		case GroupServiceContent.REMOVE_USER:
			processRemoveUserFromGroupMessage(message);
			break;
		case GroupServiceContent.REMOVE_GROUP:
			processRemoveGroupFromGroupMessage(messagee);
			break;
		case GroupServiceContent.REQUEST_ADD_USER:
			processRequestToAddUserToGroupMessage(message);
			break;
		case GroupServiceContent.REQUEST_ADD_GROUP:
			processRequestToAddUserToGroupMessage(message);
			break;
		case GroupServiceContent.GET_GROUP_USERS:
			processQueryGroupUsersMessage(message);
			break;
		case GroupServiceContent.GET_GROUP_SUBGROUPS:
			processQueryGroupUsersMessage(message);
			break;
		case GroupServiceContent.GET_GROUP_SUPERGROUPS:
			processQueryGroupSuperGroupsMessage(message);
			break;
		case GroupServiceContent.GET_GROUP_MODERATORS:
			processQueryGroupModeratorsMessage(message);
			break;
		case GroupServiceContent.GET_PENDING_USER_REQUESTS:
			processQueryGroupPendingUsersMessage(message);
			break;
		case GroupServiceContent.GET_PENDING_SUBGROUP_REQUESTS:
			processQueryGroupPendingSubgroupsMessage(message);
	
	}
}



/*Process group create message request to server response
 * 
 * @param message - a message to process
 */
function processGroupCreateMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Process a group delete message request to server response
 * 
 * @param message - a message to process
 */
function processGroupDeleteMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Process a group an invite for a given user response
 * 
 * @param message - a message to process
 */
function processInviteUserToGroupMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Process a group an approval to add user response
 * 
 * @param message - a message to process
 */
function processGroupApproveAddUserMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Process a group an approval to add group response
 * 
 * @param message - a message to process
 */
function processGroupApproveAddGroupMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Process a remove user from group message response
 * 
 * @param message - a message to process
 */
function processRemoveUserFromGroupMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/*Sends a remove subgroup from supergroup message response
 * 
 * @param message - a message to process
 */
function processRemoveGroupFromGroupMessage(messagee) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}

}


/* User requests to be added to group response
 * 
 * @param message - a message to process
 */
function processRequestToAddUserToGroupMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}



/* Process Group requests to be added to group response
 * 
 * @param message - a message to process
 */
function processAddGroupToGroupMessage(message) {
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}


/* Process Query request for group users response
 * 
 * @param message - a message to process
 */
function processQueryGroupUsersMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)

}



/* Process Query request for group subgroups response
 * 
 * @param message - a message to process
 */
function processQueryGroupSubGroupsMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)

}


/* Process Query request for group superGroups response
 * 
 * @param message - a message to process
 */
function processQueryGroupSuperGroupsMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)


}

/* Process Query request for group moderators response
 * 
 * @param message - a message to process 
 */
function processQueryGroupModeratorsMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)


}

/* Process Query request for group pending user requests response
 * 
 * @param message - a message to process 
 */
function processQueryGroupPendingUsersMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)


}


/* Process Query request for group pending subgroup requests response
 * 
 * @param message - a message to process
 */
function processQueryGroupPendingSubgroupsMessage(message) {
	//TODO Chad add your GUI logic here (note multiple users are seperated with a "," in the content of the message)


}

