import React, {Component} from 'react';
import {
  Text,
  View,
  TouchableHighlight,
  StyleSheet,
  Alert
} from 'react-native';

const styleSheet = StyleSheet.create({
  container: {
    height: 120,
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
  }
});

let globalCounter = 0;
export default class InAppNotification extends Component {

  constructor(props) {
    super(props);
    this._counter = globalCounter++;
  }

  render() {
    const message = `I'm notification #${this._counter}!`;
    return (
      <View style={styleSheet.container}>
          <Text style={{color: 'black', fontSize: 20}}>{message}</Text>
          <TouchableHighlight onPress={() => {
            this.props.navigator.dismissInAppNotification();
            Alert.alert(`You've tapped on notification #${this._counter}`, 'We hope you had fun!');
          }}>
            <Text>Tap Me</Text>
          </TouchableHighlight>
      </View>
    );
  }
}
