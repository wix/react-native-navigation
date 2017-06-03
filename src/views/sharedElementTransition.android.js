import React, {Component} from 'react';
import {
  requireNativeComponent
} from 'react-native';

const SharedElementTransitionModule = requireNativeComponent('SharedElementTransition', null);

export default class SharedElementTransition extends Component {
  render() {
    return <SharedElementTransitionModule {...this.props} />;
  }
}
