import { generalMessageRouter } from './GeneralMessageRouter'
import { sendLoginMessage, sendCreateUserMessage } from './UserServiceMessages'

var ws;
var username;
var active_socket = false;
/* Enumeration for colors
 * 
 */

/* Connects to the server and sets up call back for messaging events
 * 
 */
function connect() {
	console.log('Checking Socket Connection');

	if (typeof ws === 'undefined') {

		console.log('Connecting to Socket');

		var host = document.location.host;
		var pathname = document.location.pathname;

		//Hard coded for testing purposes
		ws = new WebSocket("ws://localhost:8080/prattle/chat/");
		// ws = new WebSocket("ws://" +host  + pathname + "chat/");

		ws.onopen = function () {
			console.log('Connection open!');
			active_socket = true;
		}

		ws.onclose = function (code) {
			console.log("websocket closing. Code:", code);
			active_socket = false;
		}

		ws.onerror = function (evt) {
			console.log("Websocket Error");
			console.log("Error Code: ", evt.data);
		}
		ws.onmessage = function (event) {
			console.log('Received Message');
			console.log(event.data);
			generalMessageRouter(JSON.parse(event.data));

		};
	}
}

/* Sends a message to the server
*
*/
function send(message) {
	//If the server is already open then sent the message
	if (active_socket) {
		ws.send(message)
	} else {
		//if server is not connected, then connect first
		connect();
		setTimeout(function () { ws.send(message); }, 500);
	}
}

export { send };