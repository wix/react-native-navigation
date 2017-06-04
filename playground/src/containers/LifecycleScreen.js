import React, { Component } from 'react';
import { View, Text, Button } from 'react-native';

import Navigation from 'react-native-navigation';

class LifecycleScreen extends Component {
  constructor(props) {
    super(props);
    this.onClickPush = this.onClickPush.bind(this);
    this.state = {
      text: 'nothing yet'
    };
  }

  onStart() {
    this.setState({ text: 'onStart' });
  }

  onStop() {
    alert('onStop'); //eslint-disable-line
  }

  componentWillUnmount() {
    alert('componentWillUnmount'); //eslint-disable-line
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Lifecycle Screen`}</Text>
        <Text style={styles.h1}>{this.state.text}</Text>
        <Button title="Push to test onStop" onPress={this.onClickPush} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
      </View>
    );
  }

  onClickPush() {
    Navigation.push(this.props.containerId, {
      name: 'navigation.playground.TextScreen'
    });
  }
}
export default LifecycleScreen;

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
