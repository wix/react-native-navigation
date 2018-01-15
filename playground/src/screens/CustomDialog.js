const React = require('react');
const { PureComponent } = require('react');

const { View, Text, Button } = require('react-native');
const Navigation = require('react-native-navigation');

class CustomDialog extends PureComponent {

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>Test view</Text>
        <Button title="OK" onPress={this.onCLickOk} />
      </View>
    );
  }

  onCLickOk() {
    Navigation.dismissOverlay();
  }
}

const styles = {
  root: {
    flex: 1,
    backgroundColor: 'green',
    justifyContent: 'center',
    alignItems: 'center'
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
