import React, { Component } from 'react'

export class ToField extends Component {

    state = {
        messageWith: ''
    }

    onChange = (e) => {

        this.setState({
            [e.target.name]: e.target.value
        }, () => {
            this.props.setTo(this.state.messageWith);
        });

    }

    render() {
        return (
            <form style={{ display: 'flex' }}>
                <input
                    type="text"
                    name="messageWith"
                    placeholder="Who To Message"
                    style={{ flex: '10', padding: '5px' }}
                    value={this.state.messageWith}
                    onChange={this.onChange}
                />
            </form>

        )
    }
}

export default ToField