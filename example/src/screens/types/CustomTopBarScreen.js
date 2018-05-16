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

  static navigatorButtons = {
    leftButtons: [
      {
        title: 'Back',
        id: 'helloBtn',
      }
    ],
    rightButtons: [
      {
        title: 'Right',
        id: 'helloBtn2',
      }
    ],
  };

  componentDidMount() {
    this.props.navigator.setStyle({
      navBarCustomView: 'example.CustomTopBar',
      navBarComponentAlignment: 'center',
      navBarCustomViewInitialProps: {
        title: 'Hi Custom',
        navigator: this.props.navigator,
      },
    });
    this.props.navigator.setOnNavigatorEvent((e) => {
      if (e.type == 'NavBarButtonPress') { // this is the event type for button presses
        if (e.id == 'helloBtn') { // this is the same id field from the static navigatorButtons definition
          this.props.navigator.pop();
          // alert('Hello left btn');
        }
        if (e.id == 'helloBtn2') { // this is the same id field from the static navigatorButtons definition
          alert('Hello right btn');
        }
      }
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
