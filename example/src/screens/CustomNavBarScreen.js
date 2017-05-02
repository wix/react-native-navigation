import React, {Component} from 'react';
import {
  Text,
  View,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Button
} from 'react-native';

export default class CustomNavBarScreen extends Component {
  static navigatorStyle = {
    drawUnderTabBar: true,
    navBarCustomView: 'example.CustomNavBar',
    navBarCustomViewInitialProps: {name: 'Hi Custom'}
  };


  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={styles.container}>
      </View>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: 'white'
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
