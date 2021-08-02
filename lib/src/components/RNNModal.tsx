import React from 'react';
import { requireNativeComponent } from 'react-native';

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');

export class RNNModal extends React.Component {
  render() {
    return <RNNModalViewManager {...this.props} />;
  }
}
