import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route} from "react-router-dom";
import './App.css';
import LoginPage from './LoginPage';
import Header from './components/layout/Header';
import About from './components/pages/About';
import Messenger from './Messenger';
import { connect, send } from './WebSocket'

class App extends Component {
  state = {
    ws: '',
    username: null,
    active_socket: false,
    authenticated: true,
    queuedMessage: ''
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

      var host = document.location.host;
      var pathname = document.location.pathname;

      ws = new WebSocket("ws://localhost:8080/prattle/chat/");
      // ws = new WebSocket("ws://" +host  + pathname + "chat/");
      this.setState({ ws })

      ws.onopen = () => {
        console.log('Connection open!');
        this.setState({ active_socket: true })
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
        console.log(event.data);
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
      connect();
      setTimeout(function () { ws.send(message); }, 500);
    }
  }

  generalMessageRouter = (message) => {
    console.log("General message router")
    console.log(message);
    if (message.type == "BROADCAST_MESSAGE") {
      this.chatMessageRouter(message);
    }
    if (message.from == "USER_SERVICE") {
      this.userServiveMessageRouter(message);
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
    console.log("I got something to use");
    console.log(message);
    this.setState({queuedMessage: message})

  }

  userServiveMessageRouter(message) {
    if (message.contentType == "LOGIN") {
      this.processLoginResponse(message);
    }
    if (message.contentType == "USER_CREATE") {
      this.processUserCreateResponse(message);
    }
  }

  processLoginResponse(message) {
    if (message.content.includes("SUCCESS")) {
      this.setState({username: message.to})
      console.log(message)
      console.log("SUCCESS: User successfully logged into session.");
      this.setState({authenticated: true});
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


  render() {
    return (

      <Router basename="prattle">
        <div className="App">
          <div className="container">
            <Header username={this.state.username}/>
            <Route exact path="/" render={(routeProps) => (<LoginPage {...routeProps} connect={this.connect} send={this.send} username={this.state.username}/>)} />
            <Route path="/about" component={About} />
            <Route path="/chat/:messageWith?" render={(routeProps) => (<Messenger {...routeProps} connect={this.connect} send={this.send} username={this.state.username} queuedMessage={this.state.queuedMessage}/>)} />
          </div>
        </div>
      </Router>
    );
  }

}

export default App;