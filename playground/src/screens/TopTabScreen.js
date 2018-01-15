const React = require('react');
const { PureComponent } = require('react');
const { View, Text } = require('react-native');
const Navigation = require('react-native-navigation');

class TopTabScreen extends PureComponent {
  static get options() {
    return {
      topBar: {
        textColor: 'black',
        textFontSize: 16,
        textFontFamily: 'HelveticaNeue-Italic'
      }
    };
  }

  constructor(props) {
    super(props);
    Navigation.setOptions(this.props.componentId, {
      topBar: {
        title: this.props.title
      }
    });
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
