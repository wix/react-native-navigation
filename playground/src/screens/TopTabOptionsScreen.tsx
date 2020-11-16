import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';
import { Navigation, NavigationComponentProps } from 'react-native-navigation';
import testIDs from '../testIDs';

interface Props extends NavigationComponentProps {
  text?: string;
}
export default class TopTabOptionsScreen extends React.PureComponent<Props> {
  static options() {
    return {
      topBar: {
        title: {
          color: 'black',
          fontSize: 16,
          fontFamily: 'HelveticaNeue-Italic',
        },
      },
    };
  }

  render() {
    return (
      <View style={styles.root}>
        <Text style={styles.h1}>{this.props.text || 'Top Tab Screen'}</Text>
        <Text style={styles.footer}>{`this.props.componentId = ${this.props.componentId}`}</Text>
        <Button
          title="Dynamic Options"
          testID={testIDs.DYNAMIC_OPTIONS_BUTTON}
          onPress={this.onClickDynamicOptions}
        />
      </View>
    );
  }

  onClickDynamicOptions = () => {
    Navigation.mergeOptions(this.props.componentId, {
      topBar: {
        title: {
          text: 'Dynamic Title',
          color: '#00FFFF',
          fontSize: 16,
          fontFamily: 'HelveticaNeue-CondensedBold',
        },
        largeTitle: {
          visible: false,
        },
      },
    });
  };
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
