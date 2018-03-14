import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  TouchableOpacity,
  Text,
  Alert,
  Platform
} from 'react-native';

export default class CustomTopBar extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity style={styles.button} onPress={ () => Alert.alert(this.props.title, 'Hello custom btn :)') }>
          <Text style={styles.text}>Custom</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    // backgroundColor: 'yellow'
  },
  button: {
    alignSelf: 'center',
    // backgroundColor: 'green'
  },
  text: {
    alignSelf: 'center',
    color: 'white'
  }
});



