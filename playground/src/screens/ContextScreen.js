const React = require('react');
const { View, Text, Button } = require('react-native');
const testIDs = require('../testIDs');
const { GlobalContext, Context } = require('../context');

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
          <GlobalContext.Consumer>
            {ctx => (
              <Text style={styles.h2} testID={testIDs.CENTERED_TEXT_HEADER}>{ctx.title}</Text>
            )}
          </GlobalContext.Consumer>
        </View>
        <View>
          <GlobalContext.Consumer>
            {ctx => (
              <Button title={`clicked ${ctx.count}`} onPress={() => ctx.count++}/>
            )}
          </GlobalContext.Consumer>
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
