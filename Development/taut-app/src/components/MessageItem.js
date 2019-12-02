import React, { Component } from 'react'
import PropTypes from 'prop-types';

export class MessageItem extends Component {
    getStyle = () => {
        return {
            background: '#f4f4f4',
            padding: '10px',
            borderBottom: '1px #ccc dotted',
        }
    }

    render() {

        const { from, to, content  } = this.props.message;

        return (
            <div style={this.getStyle()}>
                <p>
                    {from}
                    {'->'}
                    {to}
                    {': '}
                    {content}
                </p>
            </div>
        )
    }
}

MessageItem.propTypes = {
    message: PropTypes.object.isRequired
}

export default MessageItem