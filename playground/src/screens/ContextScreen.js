const React = require('react');
const { View, Text } = require('react-native');
const testIDs = require('../testIDs');
const { TitleContext, Context } = require('../context');

class ContextScreen extends React.Component {
  static contextType = Context;

  static options() {
    return {
      topBar: {
        title: {
          text: 'My Screen'
        },
        background: {
          color: 'red'
        }
      }
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 10 }}>
          <Text style={styles.h2}>Default value: </Text>
          <Text style={styles.h2} testID={testIDs.CENTERED_TEXT_HEADER}>{this.context}</Text>
        </View>
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          <Text style={styles.h2}>Provider value: </Text>
          <TitleContext.Consumer>
            {title => (
              <Text style={styles.h2} testID={testIDs.CENTERED_TEXT_HEADER}>{title}</Text>
            )}
          </TitleContext.Consumer>
        </View>
      </View>
    );
  }
}

module.exports = ContextScreen;

const styles = {
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff'
  },
  h1: {
    fontSize: 24,
    textAlign: 'center'
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
  },
};
