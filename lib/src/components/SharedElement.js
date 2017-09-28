import PropTypes from 'prop-types';
import React from 'react';
import { requireNativeComponent } from 'react-native';

class SharedElement extends React.Component {
  render() {
    return <RNNSharedElement {...this.props} />;
  }
}

SharedElement.propTypes = {
  elementId: PropTypes.string.isRequired,
  type: PropTypes.string,
  resizeMode: PropTypes.string
};
SharedElement.defaultProps = {
  type: 'normal',
  resizeMode: ''
};
const RNNSharedElement = requireNativeComponent('RNNSharedElement', SharedElement);

module.exports = SharedElement;
