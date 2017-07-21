import React, {Component} from 'react';
import {
  View,
  StyleSheet,
  Text
} from 'react-native';
import {Navigation} from 'react-native-navigation';
import CustomTopBar from './CustomTopBar';
Navigation.registerComponent('example.CustomTopBar', () => CustomTopBar);

export default class CustomTopBarScreen extends Component {
  static navigatorStyle = {
    drawUnderTabBar: true,
    navBarCustomView: 'example.CustomTopBar',
    navBarComponentAlignment: 'center',
    navBarCustomViewInitialProps: {name: 'Hi Custom'}
  };

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>Custom component in TopBar</Text>
      </View>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white'
  },
  text: {

  }
});
