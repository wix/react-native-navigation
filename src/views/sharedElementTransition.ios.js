import React, {Component} from 'react';
import PropTypes from 'prop-types';
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
