const React = require('react');
const {
  ScrollView,
  View,
  requireNativeComponent
} = require('react-native');

class NestedScrollView extends React.Component {

  render() {
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