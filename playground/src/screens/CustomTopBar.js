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
const { Navigation } = require('react-native-navigation');

class CustomTopBar extends Component {

  constructor(props) {
    super(props);
    console.log('guyca', `T.ctor ${this.props.color}`);
    this.simulateLongRunningTask();
    this.state = {};
    Navigation.events().bindComponent(this);
  }

  componentDidAppear() {
    console.log('RNN', 'CTB.componentDidAppear');
  }

  componentDidDisappear() {
    console.log('RNN', `CTB.componentDidDisappear`);
  }

  componentDidMount() {
    console.log('RNN', `CTB.componentDidMount`);
  }

  componentWillUnmount() {
    console.log('RNN', `CTB.componentWillUnmount`);
  }

  simulateLongRunningTask = () => {
    // tslint:disable-next-line
    for (let i = 0; i < Math.pow(2, 24); i++);
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
