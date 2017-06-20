import React, {Component, PropTypes} from 'react';
import {
  View
} from 'react-native';

export default class SharedElementTransition extends Component {
  static propTypes = {
    children: PropTypes.object
  };

  render() {
    const {children, ...restProps} = this.props;
    return (
      <View {...restProps}>
        {children}
      </View>
    );
  }
}
