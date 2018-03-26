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

  componentDidAppear() {
    console.log('guyca', 'componentDidAppear');
  }

  componentDidDisappear() {
    console.log('guyca', `componentDidDisappear`);
  }

  componentDidMount() {
    console.log('guyca', `componentDidMount`);
  }

  componentWillUnmount() {
    console.log('guyca', `componentWillUnmount`);
  }

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={() => Alert.alert(this.props.title, 'Thanks for that :)')}>
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
    flexDirection: 'column',
    justifyContent: 'center',
    alignSelf: 'center'
  },
  text: {
    alignSelf: 'center',
    color: 'black',
  }
});
