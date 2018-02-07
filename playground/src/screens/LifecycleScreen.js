const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class LifecycleScreen extends Component {
  constructor(props) {
    super(props);
    this.onClickPush = this.onClickPush.bind(this);
    this.state = {
      text: 'nothing yet'
    };
  }

  didAppear() {
    this.setState({ text: 'didAppear' });
  }

  didDisappear() {
    alert('didDisappear'); // eslint-disable-line no-alert
  }

  componentWillUnmount() {
    alert('componentWillUnmount'); // eslint-disable-line no-alert
  }

  onNavigationButtonPressed(id) {
    alert(`onNavigationButtonPressed: ${id}`); // eslint-disable-line no-alert
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Lifecycle Screen`}</Text>
        <Text style={styles.h1}>{this.state.text}</Text>
        <Button title="Push to test didDisappear" testID={testIDs.PUSH_TO_TEST_DID_DISAPPEAR_BUTTON} onPress={this.onClickPush} />
        <Button title="Pop" testID={testIDs.POP_BUTTON} onPress={() => this.onClickPop()} />
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }

  onClickPush() {
    Navigation.push(this.props.componentId, { component: { name: 'navigation.playground.TextScreen' } });
  }

  onClickPop() {
    Navigation.pop(this.props.componentId);
  }
}
module.exports = LifecycleScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};
