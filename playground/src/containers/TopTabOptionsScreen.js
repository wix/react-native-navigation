const React = require('react');
const { PureComponent } = require('react');
const testIDs = require('../testIDs');
const { View, Text, Button } = require('react-native');
const Navigation = require('react-native-navigation');

class TopTabOptionsScreen extends PureComponent {
  static get navigationOptions() {
    return {
      topBar: {
        title: 'Tab 1',
        textColor: 'black',
        textFontSize: 16,
        textFontFamily: 'HelveticaNeue-Italic'
      }
    };
  }

  constructor(props) {
    super(props);
    this.onClickDynamicOptions = this.onClickDynamicOptions.bind(this);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{this.props.text || 'Top Tab Screen'}</Text>
        <Text style={styles.footer}>{`this.props.containerId = ${this.props.containerId}`}</Text>
        <Button title="Dynamic Options" testID={testIDs.DYNAMIC_OPTIONS_BUTTON} onPress={this.onClickDynamicOptions} />
      </View>
    );
  }

  onClickDynamicOptions() {
    Navigation.setOptions(this.props.containerId, {
      topBar: {
        title: 'Dynamic Title',
        textColor: '#00FFFF',
        largeTitle: false,
        buttonColor: 'red',
        textFontSize: 20,
        textFontFamily: 'HelveticaNeue-CondensedBold'
      }
    });
  }
}

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
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

module.exports = TopTabOptionsScreen;
