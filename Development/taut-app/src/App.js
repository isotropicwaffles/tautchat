import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  withRouter
} from "react-router-dom";
import logo from './logo.svg';
import './App.css';
import LoginPage from './loginPage';
import Chat from './chatPage';
import GroupChat from './groupChatPage';

function App() {
  return (
    <Router basename={'/prattle'}>
      <div>
        <ul>
          <li>
            <Link to="/login">Login</Link>
          </li>
          <li>
            <Link to="/chat">Chat</Link>
          </li>
          <li>
            <Link to="/groupchat">Group Chat</Link>
          </li>
        </ul>

        <hr />

        {/*
          A <Switch> looks through all its children <Route>
          elements and renders the first one whose path
          matches the current URL. Use a <Switch> any time
          you have multiple routes, but you want only one
          of them to render at a time
        */}
        <Switch>
          <Route path="/login">
            <LoginPage />
          </Route>
          <Route path="/chat">
            <Chat />
          </Route>
          <Route path="/groupchat">
            <GroupChat />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;