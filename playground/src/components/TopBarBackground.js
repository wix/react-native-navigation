const React = require('react');
const { Component } = require('react');
const {
  StyleSheet,
  View
} = require('react-native');
const { Navigation } = require('react-native-navigation');

class TopBarBackground extends Component {

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
    this.state = {};
    this.dots = new Array(55).fill('').map((ignored, i) => <View key={'dot' + i} style={[styles.dot, {backgroundColor: this.props.color || '#d3d3d3'}]}/>);
  }

  render() {
    return (
      <View style={styles.container}>
        {this.dots}
      </View>
    );
  }
}

module.exports = TopBarBackground;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    backgroundColor: '#EDEDED',
    flexWrap: 'wrap'
  },
  dot: {
    height: 16,
    width: 16,
    borderRadius: 8,
    margin: 4
  }
});
