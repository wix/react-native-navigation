const React = require('react');
const { PureComponent } = require('react');

const { Text, Button, View, Alert, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');

const testIDs = require('../testIDs');

class CustomDialog extends PureComponent {
  static get options() {
    return {
      statusBarBackgroundColor: 'green'
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.DIALOG_HEADER}>Test view</Text>
        <Button title='OK' testID={testIDs.OK_BUTTON} onPress={() => this.onCLickOk()} />
      </View>
    );
  }

  didDisappear() {
    if (Platform.OS === 'android') {
      Alert.alert('Overlay disappeared');
    }
  }
  onCLickOk() {
    Navigation.dismissOverlay(this.props.componentId);
  }
}

const styles = {
  root: {
    backgroundColor: 'green',
    justifyContent: 'center',
    alignItems: 'center',
    height: 100,
    bottom: 0,
    position: 'absolute',
    left: 0,
    right: 0
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10
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

module.exports = CustomDialog;
