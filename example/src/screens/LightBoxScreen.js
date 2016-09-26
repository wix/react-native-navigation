import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Dimensions
} from 'react-native';

export default class LightBoxScreen extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          This is a LightBox!
        </Text>
        {
          this.props.greeting &&
            <Text style={[styles.welcome, {fontSize: 16, marginTop: 20}]}>
              {this.props.greeting}
            </Text>
        }
        <TouchableOpacity onPress={() => this.onDismissPress()}>
          <Text style={styles.button}>Dismiss</Text>
        </TouchableOpacity>
      </View>
    );
  }
  onDismissPress() {
    this.props.navigator.dismissLightBox();
  }
}

var styles = StyleSheet.create({
  container: {
    flex: 1,
    width: Dimensions.get('window').width * 0.8,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
    borderRadius: 10
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  button: {
    textAlign: 'center',
    fontSize: 18,
    marginBottom: 10,
    marginTop:10,
    color: 'blue'
  }
});
