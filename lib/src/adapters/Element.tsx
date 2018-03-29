import * as React from 'react';
import * as PropTypes from 'prop-types';
import { requireNativeComponent } from 'react-native';

let RNNElement: React.ComponentType<any>;

export class Element extends React.Component<{ elementId: any; resizeMode: any; }, any> {
  static propTypes = {
    elementId: PropTypes.string.isRequired,
    resizeMode: PropTypes.string
  };

  static defaultProps = {
    resizeMode: ''
  };

  render() {
    return (
      <RNNElement {...this.props} />
    );
  }
}

RNNElement = requireNativeComponent('RNNElement', Element);
