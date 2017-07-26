import _ from 'lodash';
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button
} from 'react-native';

import Navigation from 'react-native-navigation';

class OptionsScreen extends Component {
  static navigationOptions = {
    title: 'Static Title'
  }

  constructor(props) {
    super(props);
    this.onClickDynamicOptions = this.onClickDynamicOptions.bind(this);
    this.state = { horizontal: false };
  }

  render() {
    return (
      <View style={styles.root} onLayout={this.detectHorizontal.bind(this)}>
        <Text style={styles.h1}>{`Options Screen`}</Text>
        <Button title="Dynamic Options" onPress={this.onClickDynamicOptions} />
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
        <Text style={styles.footer} testID="currentOrientation">{this.state.horizontal ? 'Landscape' : 'Portrait'}</Text>
      </View>
    );
  }

  onClickDynamicOptions() {
    Navigation.setOptions(this.props.containerId, {
      title: 'Dynamic Title',
      topBarTextColor: '#123456'
    });
  }

  detectHorizontal({ nativeEvent: { layout: { width, height, x, y } } }) {
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

export default OptionsScreen;
