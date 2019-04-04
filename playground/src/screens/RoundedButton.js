const React = require('react');
const { Component } = require('react');
const {
  StyleSheet,
  View,
  TouchableOpacity,
  Text,
  Alert,
} = require('react-native');
const Colors = require('../commons/Colors');
const { Navigation } = require('react-native-navigation');

class RoundedButton extends Component {

  constructor(props) {
    super(props);
    this.subscription = Navigation.events().bindComponent(this);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.button}>
          <TouchableOpacity onPress={() => Alert.alert(this.props.title, 'Thanks for that :)')}>
            <Text style={styles.text}>{this.props.title}</Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}

module.exports = RoundedButton;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center'
  },
  button: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primary,
    justifyContent: 'center',
  },
  text: {
    color: 'white',
    alignSelf: 'center'
  }
});
