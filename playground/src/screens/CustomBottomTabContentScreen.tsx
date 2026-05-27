import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import {
  NavigationComponent,
  NavigationProps,
  Options,
} from 'react-native-navigation';
import testIDs from '../testIDs';

interface Props extends NavigationProps {
  title?: string;
}

export default class CustomBottomTabContentScreen extends NavigationComponent<Props> {
  static options(): Options {
    return {
      topBar: { visible: false },
    };
  }

  render() {
    const title = this.props.title ?? 'Tab content';
    return (
      <View style={styles.container}>
        <Text testID={testIDs.CUSTOM_BOTTOM_TAB_SELECTED_LABEL} style={styles.text}>
          {title}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white',
  },
  text: {
    fontSize: 22,
    fontWeight: '600',
  },
});
