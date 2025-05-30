import React from 'react';
import { SafeAreaView, StyleSheet, TextStyle, View, ViewStyle } from 'react-native';
import { NavigationComponent, NavigationProps, Options } from 'react-native-navigation';
import testIDs from '../testIDs';
import Button from '../components/Button';
import Navigation from '../services/Navigation';

interface Props extends NavigationProps {
  incrementDismissedOverlays: any;
}

const { BANNER_OVERLAY } = testIDs;
let adjustResize = true;
export default class OverlayBanner extends NavigationComponent<Props> {
  static options(): Options {
    return {
      layout: {
        adjustResize: adjustResize,
      },
    };
  }

  render() {
    return (
      <SafeAreaView
        style={{
          position: 'absolute',
          left: 0,
          right: 0,
          bottom: 0,
        }}
        pointerEvents="box-none"
      >
        <View style={styles.banner}>
          <Button
            testID={BANNER_OVERLAY}
            // @ts-ignore
            size={'small'}
            style={styles.text}
            label="Toggle adjustResize Overlay"
            onPress={this.toggleAdjustResize}
          />
        </View>
      </SafeAreaView>
    );
  }

  toggleAdjustResize = () => {
    adjustResize = !adjustResize;
    Navigation.mergeOptions(this.props.componentId, {
      layout: {
        adjustResize,
      },
    });
  };
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
