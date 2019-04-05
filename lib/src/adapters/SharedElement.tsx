import * as React from 'react';
import * as PropTypes from 'prop-types';
import { requireNativeComponent } from 'react-native';

export interface SharedElementProps {
  elementId: string;
  resizeMode: string;
}

export class SharedElement extends React.Component<SharedElementProps> {
  static propTypes = {
    elementId: PropTypes.string.isRequired,
    resizeMode: PropTypes.string
  };

  static defaultProps = {
    resizeMode: ''
  };

  render() {
    return <RnnSharedElement {...this.props} />;
  }
}

const RnnSharedElement = requireNativeComponent('RNNElement', SharedElement, {
  nativeOnly: { nativeID: true }
});
