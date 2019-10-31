var ws;
var username;
var error_label = document.getElementById("login_status");


// Connects to the server and sets up call back for messaging events
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

// Sends a message from chat
function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}


// Routes received messages to correct logic
function generalMessageRouter(message) {
	if (message.from == "~USER_SERVICE") {
		//  process user server message
		userServiveMessageRouter(message);
	}
	
	if (message.from !== "") {
		//  process chat message
		processChatMessage(message);
	}
}

// Routes received user service messages to correct logic
function userServiveMessageRouter(message) {
	//  Check if it's a login response
	if (message.content.indexOf("LOGIN", 0) !==- 1) {
		processLoginResponse(message);

	}
	
	//  Check if it's a create user response
	if (message.content.indexOf("USER CREATE", 0) !==- 1) {
		//  process chat message
		processUserCreateResponse(message);
	}

}

// Processes chat message received by session
function processChatMessage(message) {
	var log = document.getElementById("log");
    log.innerHTML += message.from + " : " + message.content + "\n";
}


// Sends a user login request to server
function login() {
	username = document.getElementById("username_login").value;

	// Run this in case they are not already connected to server
	connect();
	
	// Send user creation request
    var json = JSON.stringify({
    	"from": username,
    	"to": "~USER_SERVICE",
        "content": "LOGIN"
    });
    
    ws.send(json);
    
}

//Sends a user creation request to server
function createUser() {

	username = document.getElementById("username_login").value;

	connect();

	// Send user creation request
    var json = JSON.stringify({
    	"from": username,
    	"to": "~USER_SERVICE",
        "content": "CREATE_USER"
    });
    
    ws.send(json);

}

// Process server response for user login
function processLoginResponse(message){
	if (message.content.includes("SUCCESS")){
		//If successful this should run
		document.location.href='/prattle/chat.html';
	}
	
	if (message.content.includes("FAILURE")){
	    // If unsuccessful this should run
	    error_label.style.color = '#d00';
		error_label.innerHTML =  "ERROR: User Name Does not Exist. Please Create User First.";	
	}
}

//Process server response for user creation
function processUserCreateResponse(message){
	if (message.content.includes("SUCCESS")){
	   	//If successful this should run
	    error_label.style.color = '#d00';
	    error_label.innerHTML =  "User Successfully Created!";
	}
	
	if (message.content.includes("FAILURE")){
	    
	    // THIS WILL BE SPLIT INTO A DIFFERENT FUNCTION
	    // If unsuccessful this should run
	    error_label.style.color = '#d00';
	    error_label.innerHTML =  "ERROR: Could Not Create User Name. Please Try Another Name.";	
	}
}