var ws;
var username;
var error_label = document.getElementById("login_status");

function connect() {
	if (typeof ws === 'undefined') {
				
		var host = document.location.host;
		var pathname = document.location.pathname;
		
		ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);
		
		ws.onmessage = function(event) {
			var log = document.getElementById("log");
		    console.log(event.data);
		    var message = JSON.parse(event.data);
		    log.innerHTML += message.from + " : " + message.content + "\n";
		};
	}
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}

function login() {
	username = document.getElementById("username_login").value;

	// Run this in case they are not already connected to server
	connect();
	
    var json = JSON.stringify({
        "content": "~USER_SERVICE-LOGIN: " + username
    });
    
    ws.send(json);
    
    // If unsuccessful this should run
    error_label.style.color = '#d00'
	error_label.innerHTML =  "ERROR: User Name Does not Exist. Please Create User First."
		
	//If successful this should run
	document.location.href='/prattle/chat.html';

}

function create_user() {
	username = document.getElementById("username_login").value;

	connect();

    var json = JSON.stringify({
        "content": "~USER_SERVICE-CREATION: " + username
    });
    
    ws.send(json);
    

    // If unsuccessful this should run
    error_label.style.color = '#d00'
    error_label.innerHTML =  "ERROR: Could Not Create User Name. Please Try Another Name."
    	
   	//If successful this should run
    error_label.style.color = '#d00'
    error_label.innerHTML =  "User Successfully Created!"
}