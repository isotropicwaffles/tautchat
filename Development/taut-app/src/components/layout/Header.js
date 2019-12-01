import React from 'react'
import {Link} from 'react-router-dom'

export default function Header() {
    return (
        <header>
            <h1>TautChat</h1>
            <Link to="/">Home</Link> | {' '}
            <Link to="/about">About</Link> | {' '}
            <Link to="/chat">Chat</Link> | {' '}
            <Link to="/groupchat">GroupChat</Link>
        </header>
    )
}