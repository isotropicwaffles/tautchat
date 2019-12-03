import React, { Component } from 'react'

export class AddMessage extends Component {
    state = {
        content: ''
    }

    onChange = (e) => this.setState({ [e.target.name]: e.target.value });

    onSubmit = (e) => {
        e.preventDefault();
        this.props.addMessage(this.state.content);
        this.setState({ content: '' })
    }

    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <input
                    className = "input"
                    type="text"
                    name="content"
                    placeholder="Type Message"
                    autoComplete="off"
                    value={this.state.content}
                    onChange={this.onChange}
                />
            </form>

        )
    }
}

export default AddMessage