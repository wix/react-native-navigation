const React = require('react');
const {
  ScrollView,
  View,
  requireNativeComponent,
  Platform
} = require('react-native');

class NestedScrollView extends React.Component {

  render() {
    if (Platform.OS === 'ios') {
      return (
        <ScrollView {...this.props} />
      );
    }
    return (
      <RnnNestedScrollView>
        <View collapsable={false} {...this.props} />
      </RnnNestedScrollView>
    );
  }
}

NestedScrollView.propTypes = {
  ...ScrollView.propTypes
};

const RnnNestedScrollView = requireNativeComponent('RnnNestedScrollView', NestedScrollView);

module.exports = NestedScrollView;
