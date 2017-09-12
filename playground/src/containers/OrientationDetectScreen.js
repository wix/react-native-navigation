const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const Navigation = require('react-native-navigation');

class OrientationDetectScreen extends Component {
  constructor(props) {
    super(props);

    this.detectHorizontal = this.detectHorizontal.bind(this);
    this.state = { horizontal: false };
    Navigation.setOptions(this.props.containerId, {
      orientation: props.orientation
    });
  }

  render() {
    return (
      <View style={styles.root} onLayout={this.detectHorizontal}>
        <Text style={styles.h1}>{`Orientation Screen`}</Text>
        <Button title="Dismiss" onPress={() => Navigation.dismissModal(this.props.containerId)} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
        <Text style={styles.footer} testID="currentOrientation">{this.state.horizontal ? 'Landscape' : 'Portrait'}</Text>
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
    alignItems: 'center'
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
