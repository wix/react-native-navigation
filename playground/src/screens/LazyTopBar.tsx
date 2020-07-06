import React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { NavigationComponentProps } from 'react-native-navigation';
import testIDs from '../testIDs';

interface Props extends NavigationComponentProps {
  title: string;
  text: string;
}

export default class LazyTopBar extends React.Component<Props> {
  render() {
    return (
      <View style={styles.container} testID={testIDs.LAZY_TOP_PAR}>
        <Text style={styles.text}>{this.props.text}</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    alignSelf: 'center',
  },
  text: {
    alignSelf: 'center',
    color: 'black',
  },
});
