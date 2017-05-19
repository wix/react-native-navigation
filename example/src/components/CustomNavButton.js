import React from 'react';
import { Text, TouchableOpacity } from 'react-native';

export default class CustomNavButton extends React.Component {
    render() {
        return (
            <TouchableOpacity onPress={this.props.onPress}>
                <Text>CustomButton</Text>
            </TouchableOpacity>
        );
    }
}
