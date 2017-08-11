import React from 'react';
import {
  View,
  StyleSheet,
  Text,
  TouchableOpacity,
  Button
} from 'react-native';
import {Navigation} from 'react-native-navigation';

let navigator;
const CustomButton = ({text}) =>
  <TouchableOpacity
    style={[styles.buttonContainer]}
    onPress={() => navigator.pop()}
  >
    <View style={styles.button}>
      <Text style={{ color: 'white' }}>{text}</Text>
    </View>
  </TouchableOpacity>;
Navigation.registerComponent('CustomButton', () => CustomButton);

export default class CustomButtonScreen extends React.Component {
  static navigatorButtons = {
    rightButtons: [
      {
        id: 'custom-button',
        component: 'CustomButton',
        passProps: {
          text: 'Hi!'
        }
      }
    ]
  };

  componentWillMount() {
    navigator = this.props.navigator;
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>Custom Left Button</Text>
      </View>
    );
  }

  componentWillUnmount() {
    navigator = null;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white'
  },
  buttonContainer: {
    width: 48,
    height: 48,
    justifyContent: 'center',
    alignItems: 'center'
  },
  button: {
    backgroundColor: 'tomato',
    width: 34,
    height: 34,
    borderRadius: 34 / 2,
    overflow: 'hidden',
    justifyContent: 'center',
    alignItems: 'center'
  }
});
