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

class CustomTextButton extends Component {

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

module.exports = CustomTextButton;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: 60,
    flexDirection: 'column',
    justifyContent: 'center',
  },
  button: {
    flex: 1,
    flexDirection: 'column',
  },
  text: {
    color: 'black',
  }
});
