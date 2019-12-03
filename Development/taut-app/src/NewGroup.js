import React, { Component } from 'react'

export class NewGroup extends Component {
    state = {
        newGroup: ''
    }

    onChange = (e) => this.setState({ [e.target.name]: e.target.value });

    onSubmit = (e) => {
        e.preventDefault();
        this.props.addGroup(this.state.newGroup);
        this.setState({ newGroup: '' })
    }

    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <input
                    className="input is-small"
                    type="text"
                    name="newGroup"
                    placeholder="New Group"
                    autoComplete="off"
                    value={this.state.newGroup}
                    onChange={this.onChange}
                />
            </form>
        )
    }
}

export default NewGroup