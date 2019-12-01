import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
} from "react-router-dom";
import './App.css';
import LoginPage from './components/LoginPage';
import Header from './components/layout/Header';
import About from './components/pages/About';
import Messenger from './components/Messenger';

class App extends Component {

  render() {
    return (
      <Router basename="prattle">
        <div className="App">
          <div className="container">
            <Header />
            <Route exact path="/" component={LoginPage} />
            <Route path="/about" component={About} />
            <Route path="/chat/:currentUser?/:messageWith?" component={Messenger} />
          </div>
        </div>
      </Router>
    );
  }

}

export default App;