import _ from 'lodash/math';
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button
} from 'react-native';
import Navigation from 'react-native-navigation';

class TextScreen extends Component {
  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{this.props.text || 'Text Screen'}</Text>
        {this.renderTextFromFunctionInProps()}
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
        <Button title={"setTabBadge"} onPress={() => this.onButtonPress()}/>
      </View>
    );
  }

  renderTextFromFunctionInProps() {
    if (!this.props.myFunction) {
      return undefined;
    }
    return (
      <Text style={styles.h1}>{this.props.myFunction()}</Text>
    );
  }

  onButtonPress() {
    Navigation.setOptions(this.props.containerId, {
      setTabBadge: `${_.floor(Math.random() * 10)}`
    });
  }
}

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

export default TextScreen;
