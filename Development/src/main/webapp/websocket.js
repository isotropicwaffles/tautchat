var ws;
var username;
var error_label = document.getElementById("login_status");
var displayname = document.getElementById("login_name");


/* Enumeration for message types service messages
 * 
 */
const messageTypes = {
	
	USER_SERVICE: 'USER_SERVICE',
	/**
	* Represents Session Service Address
	*/
    SESSION_SERVICE: 'SESSION_SERVICE',
	/**
	* Represents Group Service Address
	*/
    GROUP_SERVICE: 'GROUP_SERVICE',
	/**
	* Represents group message address
	*/
    GROUP_MESSAGE: 'GROUP_MESSAGE"',//(TODO) Hook up logic for using this
    /**
	* Represents a direct message address
	*/
	DIRECT_MESSAGE: 'DIRECT_MESSAGE"', //(TODO) Hook up logic for using this
    /**
	* Represents a broadcast message address
	*/
	BROADCAST_MESSAGE: 'BROADCAST_MESSAGE',
}


/* Enumeration for user service messages
 * 
 */
const userServiceContent = {
	    LOGIN: 'LOGIN',
	    USERCREATE: 'USER_CREATE',
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
	console.log('Checking Socket Connection');

	if (typeof ws === 'undefined') {
				
				console.log('Connecting to Socket');

    
	 var host = document.location.host;
	 var pathname = document.location.pathname;
		    
	ws = new WebSocket("ws://" +host  + pathname + "chat/");
		
	ws.onopen = function(){
		console.log('Connection open!');
		}

	ws.onclose = function(code) {
		console.log("websocket closing. Code:", code );
	}
	
	ws.onerror= function(evt){
		console.log("Websocket Error");
		console.log("Error Code: ", evt.data);
	}
	ws.onmessage = function(event) {
		console.log('Received Message');

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
	if (message.from == messageTypes.USER_SERVICE) {
		//  process user server message
		userServiveMessageRouter(message);
	}
	
	if (message.from == messageTypes.BROADCAST_MESSAGE) {
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
	// Run this in case they are not already connected to server
	connect();
	
	//Send user creation request
    var json = JSON.stringify({
    	"from": username,
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.LOGIN
        "content": username
    });
    
    setTimeout(function(){ ws.send(json);},500);
    
}

/*Sends a user creation request to server
 * 
 */
function createUser() {

	username = document.getElementById("username_login").value;
	connect();

	//Send user creation request
    var json = JSON.stringify({
    	"from": username,
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.USERCREATE,
        "content": username
   });
    
    setTimeout(function(){ ws.send(json);},500);
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