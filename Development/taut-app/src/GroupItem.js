import React, { Component } from 'react'
import PropTypes from 'prop-types';

export class GroupItem extends Component {
    render() {

        const { groupWith } = this.props.group;

        return (
            <div>
                <p>
                    {groupWith}
                </p>
            </div >
        )
    }
}

GroupItem.propTypes = {
    group: PropTypes.object.isRequired
}

export default GroupItem