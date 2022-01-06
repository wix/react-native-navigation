import React from 'react';
import { SafeAreaView, StyleSheet, TextStyle, View, ViewStyle } from 'react-native';
import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';
import testIDs from '../testIDs';
import Button from '../components/Button';
import Navigation from '../services/Navigation';

interface Props extends NavigationComponentProps {
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
          flex: 1,
          flexDirection: 'column-reverse',
        }}
        pointerEvents="box-none"
      >
        <View testID={BANNER_OVERLAY} style={styles.banner}>
          <Button
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
