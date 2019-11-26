
/*Sends a group create message request to server
 * 
 * @param groupName - group name of group to create
 */
function sendGroupCreateMessage(groupName) {

    var json = JSON.stringify({
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GROUP_CREATE,
        "content": groupName
   });
    
   send(json);
}


/*Sends a group delete message request to server
 * 
 * @param groupName - group name of group to delete
 */
function sendGroupDeleteMessage(groupName) {

    var json = JSON.stringify({
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GROUP_DELETE,
        "content": groupName
   });
    
   send(json);
}


/*Sends a group an invite for a given user
 * 
 * @param groupName - group name 
 * @param userName - user to invite to group
 */
function sendInviteUserToGroupMessage(groupName, userName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.INVITE_USER,
        "content": userName
   });
    
   send(json);
}


/*Sends a group an approval to add user
 * 
 * @param groupName - group name 
 * @param userName - user to add
 */
function sendGroupApproveAddUserMessage(groupName, userName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.APPROVE_ADD_USER,
        "content": userName
   });
    
   send(json);
}


/*Sends a group an approval to add group
 * 
 * @param superGroupName - group being sent request
 * @param subGroupName - subgroup to add
 */
function sendGroupApproveAddGroupMessage(superGroupName, subGroupName) {

    var json = JSON.stringify({
    	"to": superGroupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.APPROVE_ADD_GROUP,
        "content": subGroupName
   });
    
   send(json);
}


/*Sends a remove user from group message
 * 
 * @param groupName - group name 
 * @param userName - user to remove
 */
function sendRemoveUserFromGroupMessage(groupName, userName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.REMOVE_USER,
        "content": userName
   });
    
   send(json);
}


/*Sends a remove subgroup from supergroup message
 * 
 * @param superGroupName - group being sent request
 * @param subGroupName - subgroup to add
 */
function sendRemoveGroupFromGroupMessage(superGroupName, subGroupName) {

    var json = JSON.stringify({
    	"to": superGroupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.REMOVE_GROUP,
        "content": subGroupName
   });
    
   send(json);
}



/* User requests to be added to group
 * 
 * @param groupName - group name 
 * @param userName - user name requesting
 */
function sendRequestToAddUserToGroupMessage(groupName, userName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.REQUEST_ADD_USER,
        "content": userName
   });
    
   send(json);
}



/* Group requests to be added to group
 * 
 * @param superGroupName - group being sent request
 * @param subGroupName - subgroup requesting to be added
 */
function sendAddGroupToGroupMessage(superGroupName, subGroupName) {

    var json = JSON.stringify({
    	"to": superGroupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.REQUEST_ADD_GROUP,
        "content": subGroupName
   });
    
   send(json);
}


/* Query request for group users
 * 
 * @param groupName - group name 
 */
function sendQueryGroupUsersMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_GROUP_USERS,
   });
    
   send(json);
}



/* Query request for group subgroups
 * 
 * @param groupName - group name 
 */
function sendQueryGroupSubGroupsMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_GROUP_SUBGROUPS,
   });
    
   send(json);
}


/* Query request for group superGroups
 * 
 * @param groupName - group name 
 */
function sendQueryGroupSuperGroupsMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_GROUP_SUPERGROUPS,
   });
    
   send(json);
}

/* Query request for group moderators
 * 
 * @param groupName - group name 
 */
function sendQueryGroupModeratorsMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_GROUP_MODERATORS,
   });
    
   send(json);
}

/* Query request for group pending user requests
 * 
 * @param groupName - group name 
 */
function sendQueryGroupPendingUsersMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_PENDING_USER_REQUESTS,
   });
    
  send(json);
}


/* Query request for group pending subgroup requests
 * 
 * @param groupName - group name 
 */
function sendQueryGroupPendingSubgroupsMessage(groupName) {

    var json = JSON.stringify({
    	"to": groupName,
    	"type": messageTypes.GROUP_SERVICE,
		"contentType": GroupServiceContent.GET_PENDING_SUBGROUP_REQUESTS,
   });
    
   send(json);
}


