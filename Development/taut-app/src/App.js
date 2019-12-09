import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route
} from "react-router-dom";
import './App.css';
import LoginPage from './LoginPage';
import Header from './components/layout/Header';
import About from './components/pages/About';
import Messenger from './Messenger';

class App extends Component {
  state = {
    ws: '',
    username: null,
    active_socket: false,
    authenticated: true,
    queuedMessage: '',
    users: [""],
    groups: ["Kitty Lovers", "Science Project", "Taut Admins"]
  }

  componentDidMount() {
    this.connect();
  }

  connect = () => {
    var ws = this.state.ws
    var active_socket = this.state.active_socket
    console.log('Checking Socket Connection');

    if (this.state.ws === '') {

      console.log('Connecting to Socket');

      var host = window.location.host;
      var pathname = window.location.pathname;

	  console.log(host);
	  console.log(pathname);
      // ws = new WebSocket("ws://localhost:8080/prattle/chat/");
      ws = new WebSocket("ws://" +host  + pathname + "chat/");
      this.setState({ ws })

      ws.onopen = () => {
        console.log('Connection open!');
        this.setState({ active_socket: true }, () => {
          this.retrieveAllUsers();
          this.retrieveAllGroups();
          // this.filterMessages();
        });
      }

      ws.onclose = (code) => {
        console.log("websocket closing. Code:", code);
        this.setState({ active_socket: false })
        this.setState({ ws: '' })
      }

      ws.onerror = (evt) => {
        console.log("Websocket Error");
        console.log("Error Code: ", evt.data);
      }
      ws.onmessage = (event) => {
        console.log('Received Message');
        this.generalMessageRouter(JSON.parse(event.data));
      };
    }
  }

  send = (message) => {
    var ws = this.state.ws
    var active_socket = this.state.active_socket

    if (active_socket) {
      ws.send(message)
    } else {
      this.connect();
      setTimeout(function () { ws.send(message); }, 500);
    }
  }

  retrieveAllUsers = () => {
    var json = JSON.stringify({
      "type": "USER_SERVICE",
      "contentType": "SEARCH_USERS_BY_NAME",
      "content": ""
    });
    this.send(json);
  }

  retrieveAllGroups = () => {
    var json = JSON.stringify({
      "type": "GROUP_SERVICE",
      "contentType": "SEARCH_GROUPS_BY_NAME",
      "content": ""
    });
    this.send(json);
  }

  addGroup = (group) => {
    this.setState({groups : [group, ...this.state.groups]})
  }

  generalMessageRouter = (message) => {
    console.log(message);
    if (message.type == "BROADCAST_MESSAGE") {
      this.chatMessageRouter(message);
    }
    if (message.from == "USER_SERVICE") {
      this.userServiveMessageRouter(message);
    }
    if (message.from == "GROUP_SERVICE") {
      this.groupServiveMessageRouter(message);
    }
  }

  chatMessageRouter = (message) => {
    //  Process Broadcast
    if (message.type == "BROADCAST_MESSAGE") {
      this.processDirectChatMessage(message);
    }
    else if (message.type == "DIRECT_MESSAGE") {
      this.processDirectChatMessage(message);
    }
  }

  processDirectChatMessage(message) {
    console.log(message);
    this.setState({ queuedMessage: message })
  }

  processGroupMessage(message) {
    console.log(message);
    this.setState({ queuedMessage: message })
  }

  userServiveMessageRouter(message) {
    if (message.contentType == "LOGIN") {
      this.processLoginResponse(message);
    }
    if (message.contentType == "USER_CREATE") {
      this.processUserCreateResponse(message);
    }
    if (message.contentType == "SEARCH_USERS_BY_NAME") {
      this.processUserSearch(message);
    }
  }

  groupServiveMessageRouter(message) {
    if (message.contentType == "SEARCH_GROUPS_BY_NAME") {
      this.processGroupSearch(message);
    }
  }

  processLoginResponse(message) {
    if (message.content.includes("SUCCESS")) {
      this.setState({ username: message.to })
      console.log(message)
      console.log("SUCCESS: User successfully logged into session.");
      this.setState({ authenticated: true });
    } else if (message.content.includes("FAILURE")) {
      alert("User Name Does not Exist. Please Create User First.");
      console.log("ERROR: User Name Does not Exist. Please Create User First.");
    }
  }

  processUserCreateResponse(message) {
    if (message.content.includes("SUCCESS")) {
      alert("User successfully created. Please log in to continue")
      console.log("User Successfully Created!");
    }
    else if (message.content.includes("FAILURE")) {
      alert("Could Not Create User Name. Please Try Another Name.");
      console.log("ERROR: Could Not Create User Name. Please Try Another Name.");
    }
  }

  processUserSearch(message) {
    console.log(message.content)
    const userList = message.content.split(',')
    this.setState({users : userList});
  }

  processGroupSearch(message) {
    console.log(message.content)
    const groupList = message.content.split(',')
    this.setState({groups : groupList});
  }


  render() {
    return (

      <Router basename="prattle">
        <div className="App">
          <div className="container">
            <Header username={this.state.username} />
            <Route exact path="/" render={(routeProps) => (<LoginPage {...routeProps} connect={this.connect} send={this.send} username={this.state.username}  />)} />
            <Route path="/about" component={About} />
            <Route path="/chat/:messageWith?" render={(routeProps) => (<Messenger {...routeProps} connect={this.connect} send={this.send} username={this.state.username} queuedMessage={this.state.queuedMessage} users={this.state.users} groups={this.state.groups} addGroup={this.addGroup}/>)} />
          </div>
        </div>
      </Router>
    );
  }

}

export default App;
