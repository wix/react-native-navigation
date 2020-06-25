import React from 'react';
import { Text, Button } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import { GlobalContext, Context } from '../context';

export default class ContextScreen extends React.Component<NavigationComponentProps> {
  static contextType = Context;

  static options() {
    return {
      topBar: {
        title: {
          text: 'React Context API',
        },
      },
    };
  }

  render() {
    return (
      <Root style={styles.root}>
        <Text style={styles.text}>Default value: {this.context}</Text>
        <GlobalContext.Consumer>
          {(ctx) => <Text style={styles.text}>Provider value: {ctx.title}</Text>}
        </GlobalContext.Consumer>
        <GlobalContext.Consumer>
          {(ctx) => <Button title={`clicked ${ctx.count}`} onPress={() => ctx.incrementCount()} />}
        </GlobalContext.Consumer>
      </Root>
    );
  }
}

const styles = {
  root: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 14,
    textAlign: 'center',
    marginBottom: 8,
  },
};
