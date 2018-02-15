const React = require('react');
const { PureComponent } = require('react');

const { Text, Button, View, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');

const testIDs = require('../testIDs');

class Alert extends PureComponent {

  render() {
    return (
      <View style={styles.root} key={'overlay'}>
        <View style={styles.alert}>
          <Text style={styles.h1} testID={testIDs.DIALOG_HEADER}>{this.props.title}</Text>
          <View style={styles.buttonContainer}>
            <Button title='OK' testID={testIDs.OK_BUTTON} onPress={() => this.onCLickOk()} />
          </View>
        </View>
      </View>
    );
  }

  onCLickOk() {
    Navigation.dismissOverlay(this.props.componentId);
  }
}

const styles = {
  root: {
    justifyContent: 'center',
    alignItems: 'center',
    flex: 1
  },
  alert: {
    alignItems: 'center',
    backgroundColor: '#efefef',
    width: 250,
    height: 100,
    elevation: 4
  },
  buttonContainer: {
    width: '50%',
    alignItems: 'center'
  },
  h1: {
    fontSize: 18,
    margin: 10
  }
};

module.exports = Alert;
