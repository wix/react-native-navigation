import * as React from 'react';
import * as PropTypes from 'prop-types';
import { View, requireNativeComponent } from 'react-native';

let RNNElement: React.ComponentType<any>;

export class Element extends React.Component<{ elementId: any; resizeMode?: any; }, any> {
  static propTypes = {
    elementId: PropTypes.string.isRequired,
    resizeMode: PropTypes.string,
    ...View.propTypes
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

RNNElement = requireNativeComponent(
  'RNNElement',
  Element,
  {
    nativeOnly: {
      nativeID: true
    }
  }
);
