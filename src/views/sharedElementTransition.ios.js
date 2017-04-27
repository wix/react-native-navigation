import React, {Component} from 'react';
import {
  View
} from 'react-native';

export default class SharedElementTransition extends Component {
  render() {
    return (
      <View {...this.props}>
        {this.props.children}
      </View>
    );
  }
}
