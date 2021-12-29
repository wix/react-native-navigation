import React from 'react';
import { StyleSheet, Text, View, ViewStyle, TextStyle, SafeAreaView } from 'react-native';
import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';
import testIDs from '../testIDs';

interface Props extends NavigationComponentProps {
  incrementDismissedOverlays: any;
}

const { BANNER_OVERLAY } = testIDs;

export default class OverlayBanner extends NavigationComponent<Props> {
  static options(): Options {
    return {
      layout: {
        adjustResize: false,
      },
    };
  }

  render() {
    return (
      <SafeAreaView
        style={{
          flex: 1,
          flexDirection: 'column-reverse',
        }}
        pointerEvents="box-none"
      >
        <View testID={BANNER_OVERLAY} style={styles.banner}>
          <Text style={styles.text}>This is a floating banner</Text>
        </View>
      </SafeAreaView>
    );
  }
}

type Style = {
  banner: ViewStyle;
  text: TextStyle;
};

const styles = StyleSheet.create<Style>({
  text: {
    alignSelf: 'center',
    textAlign: 'center',
  },
  banner: {
    alignContent: 'stretch',
    height: 50,
    backgroundColor: 'lightgray',
    justifyContent: 'center',
  },
});
