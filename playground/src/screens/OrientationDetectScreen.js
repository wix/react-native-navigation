const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class OrientationDetectScreen extends Component {
  constructor(props) {
    super(props);

    this.detectHorizontal = this.detectHorizontal.bind(this);
    this.state = { horizontal: false };
    Navigation.mergeOptions(this.props.componentId, {
      layout: {
        orientation: props.orientation
      }
    });
  }

  render() {
    return (
      <View style={styles.root} onLayout={this.detectHorizontal}>
        <Text style={styles.h1}>{`Orientation Screen`}</Text>
        <Button title='Dismiss' testID={testIDs.DISMISS_BUTTON} onPress={() => Navigation.dismissModal(this.props.componentId)} />
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
        {this.state.horizontal ?
        <Text style={styles.footer} testID={testIDs.LANDSCAPE_ELEMENT}>Landscape</Text> :
        <Text style={styles.footer} testID={testIDs.PORTRAIT_ELEMENT}>Portrait</Text>}
      </View>
    );
  }

  detectHorizontal({ nativeEvent: { layout: { width, height } } }) {
    this.setState({
      horizontal: width > height
    });
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};

module.exports = OrientationDetectScreen;
