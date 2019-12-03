import { Link } from 'react-router-dom'
import React, { Component } from 'react'

export class Header extends Component {

    state = {
        display: 'TautChat'
    }

    componentDidUpdate(prevProps) {
        // Typical usage (don't forget to compare props):
        if (this.props.username !== prevProps.username) {
            var display = "Hi, " + this.props.username + "!"
            this.setState({ display: display })
        }
    }

    render() {

        return (
            <nav class="breadcrumb is-centered is-medium has-bullet-separator" aria-label="breadcrumbs">
                <h1 class="title is-2">{this.state.display}</h1>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/chat">Chat</Link></li>
                    <li><Link to="/about">About</Link></li>
                </ul>
            </nav>
            // <header>


            // </header>
        )
    }
}

export default Header