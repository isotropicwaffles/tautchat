import {Link} from 'react-router-dom'
import React, { Component } from 'react'

export class Header extends Component {

    state = {
        display: 'TautChat'
    }

    componentDidUpdate(prevProps) {
        // Typical usage (don't forget to compare props):
        if (this.props.username !== prevProps.username) {
            var display = "Hi, " + this.props.username + "!"
            this.setState({display:display})
        }
    }

    render() {
        
        return (
            
            <header>
                <h1 class="title is-1">{this.state.display}</h1>
                <Link to="/">Home</Link> | {' '}
                <Link to="/chat">Chat</Link> | {' '}
                {/* <Link to="/groupchat">GroupChat</Link>| {' '} */}
                <Link to="/about">About</Link>
            </header>
        )
    }
}

export default Header