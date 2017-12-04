const React = require('react');
const {
  ViewPropTypes,
  requireNativeComponent,
  View,
  Platform
} = require('react-native');

class CoordinatorLayout extends React.Component {

  render() {
    if (Platform.OS === 'ios') {
      return (
        <View {...this.props} />
      );
    } else {
      return (
        <RNNCoordinatorLayout {...this.props} />
      );
    }
  }
}

CoordinatorLayout.propTypes = {
  ...ViewPropTypes
};

const RNNCoordinatorLayout = requireNativeComponent('RNNCoordinatorLayout', CoordinatorLayout);

module.exports = CoordinatorLayout;
