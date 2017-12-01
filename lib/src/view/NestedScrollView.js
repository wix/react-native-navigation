const React = require('react');
const {
  ScrollView,
  requireNativeComponent
} = require('react-native');

class NestedScrollView extends React.Component {

  render() {
    return (
      <RnnNestedScrollView {...this.props} />
    );
  }
}

NestedScrollView.propTypes = {
  ...ScrollView.propTypes
};

const RnnNestedScrollView = requireNativeComponent('RnnNestedScrollView', NestedScrollView);

module.exports = NestedScrollView;