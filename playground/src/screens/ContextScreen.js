const React = require('react');
const { Text, Button } = require('react-native');
const Root = require('../components/Root');
const { GlobalContext, Context } = require('../context');

class ContextScreen extends React.Component {
  static contextType = Context;

  static options() {
    return {
      topBar: {
        title: {
          text: 'React Context API'
        }
      }
    };
  }

  render() {
    return (
      <Root style={styles.root}>
        <Text style={styles.text}>Default value: {this.context}</Text>
        <GlobalContext.Consumer>
          {ctx => <Text style={styles.text}>Provider value: {ctx.title}</Text>}
        </GlobalContext.Consumer>
        <GlobalContext.Consumer>
          {ctx => <Button title={`clicked ${ctx.count}`} onPress={() => ctx.count++} />}
        </GlobalContext.Consumer>
      </Root>
    );
  }
}

module.exports = ContextScreen;

const styles = {
  root: {
    justifyContent: 'center',
    alignItems: 'center'
  },
  text: {
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 8
  }
};
