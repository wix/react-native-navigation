const React = require('react');
const {
  ViewPropTypes,
  requireNativeComponent
} = require('react-native');
const PropTypes = require('prop-types');

class FloatingActionButton extends React.Component {
  shouldComponentUpdate() {
    return false;
  }
  render() {
    return (
      <RNNFloatingActionButton {...this.props} />
    );
  }
}

FloatingActionButton.propTypes = {
  gravityTop: PropTypes.bool,
  gravityBottom: PropTypes.bool,
  gravityRight: PropTypes.bool,
  gravityLeft: PropTypes.bool,
  hidden: PropTypes.bool,
  backgroundColor: PropTypes.string,
  icon: PropTypes.string,
  elevation: PropTypes.number,
  ...ViewPropTypes
};

const RNNFloatingActionButton = requireNativeComponent('RNNFloatingActionButton', FloatingActionButton);

module.exports = FloatingActionButton;
