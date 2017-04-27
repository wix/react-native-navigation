import React, {Component} from 'react';
import {
  View
} from 'react-native';

export default class SharedElementTransition extends Component {
  static propTypes = {
    children: PropTypes.object
  };

  render() {
    return (
      <View {...this.props}>
        {this.props.children}
      </View>
    );
  }
}
