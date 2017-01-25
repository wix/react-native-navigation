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
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
    marginLeft: 10,
    marginRight: 10,
    marginTop: 18,
    marginBottom: 18
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
          <TouchableHighlight onPress={() => Alert.alert(`You've tapped on notification #${this._counter}`, 'We hope you had fun!')}>
            <Text>Tap Me</Text>
          </TouchableHighlight>
      </View>
    );
  }
}
