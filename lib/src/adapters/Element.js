import PropTypes from 'prop-types';
import React from 'react';
import { requireNativeComponent } from 'react-native';

class Element extends React.Component {
  render() {
    return <RNNElement {...this.props} />;
  }
}

Element.propTypes = {
  elementId: PropTypes.string.isRequired,
  resizeMode: PropTypes.string
};
Element.defaultProps = {
  resizeMode: ''
};
const RNNElement = requireNativeComponent('RNNElement', Element);

module.exports = Element;
