const React = require('react');
const { PureComponent } = require('react');
const { View, Text } = require('react-native');

const FAB = 'fab';

class TopTabScreen extends PureComponent {
  static get options() {
    return {
      topBar: {
        title: {
          color: 'black',
          fontSize: 16,
          fontFamily: 'HelveticaNeue-Italic'
        }
      },
      fab: {
        id: FAB,
        backgroundColor: 'blue',
        clickColor: 'blue',
        rippleColor: 'aquamarine'
      }
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{this.props.text || 'Top Tab Screen'}</Text>
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
      </View>
    );
  }
}

module.exports = TopTabScreen;

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
