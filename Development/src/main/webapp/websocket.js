var ws;
var username;
var error_label = document.getElementById("login_status");
var displayname = document.getElementById("login_name");


/* Enumeration for message types service messages
 * 
 */
const messageTypes = {
	    USERSERVICE: '~USER_SERVICE',
	    BROADCAST: "",
}


/* Enumeration for user service messages
 * 
 */
const userServiceContent = {
	    LOGIN: 'LOGIN',
	    USERCREATE: 'USER CREATE',
	    SUCCESS_RESPONSE: 'SUCCESS',
	    FAILURE_RESPONSE: 'FAILURE'
}

/* Enumeration for colors
 * 
 */
const colors = {
	    RED:  '#d00',
	    GREEN: '#009d00'
}

/* Connects to the server and sets up call back for messaging events
 * 
 */
function connect() {
	if (typeof ws === 'undefined') {
				
		var host = document.location.host;
		var pathname = document.location.pathname;
		
		ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);
		
		ws.onmessage = function(event) {
		    console.log(event.data);

		    generalMessageRouter(JSON.parse(event.data));
		    
		};
	}
}

/* Sends a message from chat
 * 
 */
function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}


/* Routes received messages to correct logic
 * 
 * @param json message - json of message
 */
function generalMessageRouter(message) {
	if (message.from == messageTypes.USERSERVICE) {
		//  process user server message
		userServiveMessageRouter(message);
	}
	
	if (message.from == messageTypes.BROADCAST) {
		//  process chat message
		processChatMessage(message);
	}
}

/* Routes received user service messages to correct logic
 * 
 * @param json message - json of user service type message
 */
function userServiveMessageRouter(message) {
	//  Check if it's a login response
	if (message.content.indexOf(userServiceContent.LOGIN, 0) !==- 1) {
		processLoginResponse(message);

	}
	
	//  Check if it's a create user response
	if (message.content.indexOf(userServiceContent.USERCREATE, 0) !==- 1) {
		//  process chat message
		processUserCreateResponse(message);
	}

}

/* Processes chat message received by session
 * 
 * @param json message - json of chat type message
 */
function processChatMessage(message) {
	var log = document.getElementById("log");
    log.innerHTML += message.from + " : " + message.content + "\n";
}


/* Sends a user login request to server
 * 
 */
function login() {
	username = document.getElementById("username_login").value;
	document.location.href='/chat.html';
	// Run this in case they are not already connected to server
	// connect();
	
	//Send user creation request
    // var json = JSON.stringify({
    	// "from": username,
    	// "to": messageTypes.USERSERVICE,
        // "content": userServiceContent.LOGIN
    // });
    
    // ws.send(json);
    
}

/*Sends a user creation request to server
 * 
 */
function createUser() {

	username = document.getElementById("username_login").value;
	document.location.href= '/chat.html';
	// connect();

	//Send user creation request
    // var json = JSON.stringify({
    	// "from": username,
    	// "to": messageTypes.USERSERVICE,
        // "content": userServiceContent.USERCREATE
    // });
    
    // ws.send(json);

}

/* Process server response for user login
*
* @param json message - json of user service type message of login response
*/
function processLoginResponse(message){
	if (message.content.includes(userServiceContent.SUCCESS_RESPONSE)){
		//If successful this should run
		document.location.href='/prattle/chat.html';
	}
	
	if (message.content.includes(userServiceContent.FAILURE_RESPONSE)){
	    // If unsuccessful this should run
	    error_label.style.color = colors.RED;
		error_label.innerHTML =  "ERROR: User Name Does not Exist. Please Create User First.";	
	}
}

/*Process server response for user creation
 * 
* @param json message - json of user service type message of create user response
 */
function processUserCreateResponse(message){
	if (message.content.includes(userServiceContent.SUCCESS_RESPONSE)){
	   	//If successful this should run
	    error_label.style.color = colors.GREEN;
	    error_label.innerHTML =  "User Successfully Created!";
	}
	
	if (message.content.includes(userServiceContent.FAILURE_RESPONSE)){
	    
	    // THIS WILL BE SPLIT INTO A DIFFERENT FUNCTION
	    // If unsuccessful this should run
	    error_label.style.color = colors.RED;
	    error_label.innerHTML =  "ERROR: Could Not Create User Name. Please Try Another Name.";	
	}
}