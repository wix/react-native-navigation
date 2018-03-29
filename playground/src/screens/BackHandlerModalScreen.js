const React = require('react');
const { Component } = require('react');

const { View, Text, Button, BackHandler } = require('react-native');

class BackHandlerModalScreen extends Component {
  static get options() {
    return {
      topBar: {
        title: {
          text: 'Back Handler'
        }
      }
    };
  }

  constructor(props) {
    super(props);
    this.backHandler = () => {
      this.setState({
        backPress: 'Back button pressed!'
      });
      return true;
    };
    this.state = {
      backPress: ''
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{`Back Handler Screen`}</Text>
        <Text style={styles.h2}>{this.state.backPress}</Text>
        <Button title='add back handler' onPress={() => this.addBackHandler()} />
        <Button title='remove back handler' onPress={() => this.removeBackHandler()} />
      </View>
    );
  }

  addBackHandler() {
    BackHandler.addEventListener('hardwareBackPress', this.backHandler);
  }

  removeBackHandler() {
    BackHandler.removeEventListener('hardwareBackPress', this.backHandler);
  }

  componentWillUnmount() {
    BackHandler.removeEventListener('hardwareBackPress', this.backHandler);
  }
}

const styles = {
  root: {
    flex: 1,
    backgroundColor: 'white',
    justifyContent: 'center',
    alignItems: 'center'
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10
  }
};

module.exports = BackHandlerModalScreen;
