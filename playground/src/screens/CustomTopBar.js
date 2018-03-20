const React = require('react');
const { Component } = require('react');
const {
  StyleSheet,
  View,
  TouchableOpacity,
  Text,
  Alert,
  Platform
} = require('react-native');

class CustomTopBar extends Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity stye={styles.button} onPress={() => Alert.alert(this.props.title, 'Thanks for that :)')}>
          <Text style={styles.text}>Press Me</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

module.exports = CustomTopBar;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white'
  },
  button: {
    alignSelf: 'center',
    backgroundColor: 'green'
  },
  text: {
    alignSelf: 'center',
    color: 'black'
  }
});
