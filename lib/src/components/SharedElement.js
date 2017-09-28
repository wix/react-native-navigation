import PropTypes from 'prop-types';
import React from 'react';
import { requireNativeComponent } from 'react-native';

class SharedElement extends React.Component {
  render() {
    return <RNNSharedElement {...this.props} />;
  }
}

SharedElement.propTypes = {
  elementId: PropTypes.string,
  type: PropTypes.string,
  interactive: PropTypes.bool
};

const RNNSharedElement = requireNativeComponent('RNNSharedElement', SharedElement);

module.exports = SharedElement;
