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
  componentDidMount() {
    this.props.navigator.setStyle({
      navBarCustomView: 'example.CustomTopBar',
      navBarComponentAlignment: 'center',
      navBarCustomViewInitialProps: {title: 'Hi Custom'}
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>Custom component in TopBar</Text>
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
  }
});
