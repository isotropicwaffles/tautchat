import React, { Component } from 'react'
import PropTypes from 'prop-types';

export class MessageItem extends Component {
    getStyle = (from) => {
        if (this.props.username === from) {
            return {
                fontSize: '20px',
                textAlign: 'right',
                padding: '10px',
            }
        }
        else {
            return {
                fontSize: '20px',
                textAlign: 'left',
                padding: '10px',
            }
        }
    }

    getNameStyle = (from) => {
        return {
            fontSize: '14px',
            padding: '1px',
        }
    }

    getFontStyle = (sentiment) => {
        var sentTest = sentiment.charAt(0);


        switch (sentTest) {

            case "0":
                return {
                    color: '#FF0000'
                }

            case "1":
                return {
                    color: '#696969'
                }

            case "2":
                return {
                    color: '#7CFC00'
                }
        }
    }

    render() {

        const { from, to, content, sentiment } = this.props.message;

        return (
            <div style={this.getStyle(from)}>
                <p style={this.getNameStyle()}>
                    {from}
                </p>
                <p style={this.getFontStyle(sentiment)}>
                    {content}
                </p>
            </div >
        )
    }
}

MessageItem.propTypes = {
    message: PropTypes.object.isRequired
}

export default MessageItem