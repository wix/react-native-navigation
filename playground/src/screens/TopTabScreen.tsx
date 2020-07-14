import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';

const FAB = 'fab';

interface Props extends NavigationComponentProps {
  text?: string;
}

export default class TopTabScreen extends React.PureComponent<Props> {
  static options() {
    return {
      topBar: {
        title: {
          color: 'black',
          fontSize: 16,
          fontFamily: 'HelveticaNeue-Italic',
        },
      },
      fab: {
        id: FAB,
        backgroundColor: 'blue',
        clickColor: 'blue',
        rippleColor: 'aquamarine',
      },
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

const styles = StyleSheet.create({
  root: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5fcff',
  },
  h1: {
    fontSize: 24,
    textAlign: 'center',
    margin: 10,
  },
  h2: {
    fontSize: 12,
    textAlign: 'center',
    margin: 10,
  },
  footer: {
    fontSize: 10,
    color: '#888',
    marginTop: 10,
  },
});
