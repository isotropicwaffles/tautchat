var ws;
var username

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

			//generalMessageRouter(JSON.parse(event.data));
	    
		};
	}
}


/* Sends a user login request to server
 * 
 */
function login() {
	username = document.getElementById("username_login").value;
	//sendLoginMessage(username);
}

/*Sends a user creation request to server
 * 
 */
function createUser() {
	username = document.getElementById("username_login").value;
	//sendCreateUserMessage(username)	
}



