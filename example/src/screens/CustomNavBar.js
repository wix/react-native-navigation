import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  TouchableOpacity,
  Text,
  Image
} from 'react-native';


export default class CustomNavBar extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity stye={styles.button} onPress={ () => alert('Thanks for that :)') }>
          <Text style={{color: 'red', textAlign: 'center'}}>{this.props.name}</Text>
          <Text style={{textAlign: 'center'}}>Press Me</Text>
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
    backgroundColor: 'transparent'
  },
  button: {
    textAlign: 'center',
    fontSize: 22,
    marginBottom: 10,
    marginTop: 10,
    color: 'blue',

  }
});



