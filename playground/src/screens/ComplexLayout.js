const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

const { Navigation } = require('react-native-navigation');
const testIDs = require('../testIDs');

class ComplexLayout extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1} testID={testIDs.CENTERED_TEXT_HEADER}>{this.props.text || 'Complex layout screen'}</Text>
        <Button title={'External component in stack'} testID={testIDs.EXTERNAL_COMPONENT_IN_STACK} onPress={() => this.onExternalComponentInStackPressed()} />
        <Button title={'External component in deep stack'} testID={testIDs.EXTERNAL_COMPONENT_IN_DEEP_STACK} onPress={() => this.onExternalComponentInDeepStackPressed()} />
      </View>
    );
  }

  onExternalComponentInStackPressed() {
    Navigation.showModal({
      stack: {
        children: [{
          externalComponent: {
            name: 'RNNCustomComponent',
            passProps: {
              text: 'External component in stack'
            }
          }
        }]
      }
    });
  }

  onExternalComponentInDeepStackPressed() {
    Navigation.showModal({
      stack: {
        children: [{
          component: {
            name: 'navigation.playground.TextScreen'
          }
        },
        {
          externalComponent: {
            name: 'RNNCustomComponent',
            passProps: {
              text: 'External component in deep stack'
            }
          }
        }]
      }
    });
  }
}

module.exports = ComplexLayout;

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
