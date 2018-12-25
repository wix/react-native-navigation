import * as React from 'react';
import * as PropTypes from 'prop-types';
import { requireNativeComponent } from 'react-native';

export class Element extends React.Component<{ elementId: any; resizeMode?: any }> {
  static propTypes = {
    elementId: PropTypes.string.isRequired,
    resizeMode: PropTypes.string
  };

  static defaultProps = {
    resizeMode: ''
  };

  render() {
    return <RNNElement {...this.props} />;
  }
}

const RNNElement = requireNativeComponent('RNNElement', Element, {
  nativeOnly: { nativeID: true }
});
