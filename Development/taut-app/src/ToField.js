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

    getStyle() {
        return {
            textAlign: 'left'
        }
    }


    render() {
        return (
            <div >
                <h1 class="subtitle is-3" style={this.getStyle()}>{this.props.messageWith}</h1>
            </div>
        )
    }
}

export default ToField