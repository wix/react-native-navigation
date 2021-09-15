import React from 'react';
import {
  requireNativeComponent,
  ViewProps,
  StyleSheet,
  BackHandler,
  SafeAreaView,
} from 'react-native';
import { View } from 'react-native-ui-lib';
export interface RNNModalProps extends ViewProps {
  // animationType?: 'none' | 'slide' | 'fade' | null;
  // presentationStyle?: 'fullScreen' | 'pageSheet' | 'formSheet' | 'overFullScreen' | null;
  // transparent?: boolean;
  // statusBarTranslucent?: boolean;
  // hardwareAccelerated?: boolean;
  onDismissed(): any;
  visible: boolean;
}

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');
type RNNModalState = {
  displayed: boolean;
};
export class RNNModal extends React.Component<RNNModalProps, RNNModalState> {
  private backHandler?: any;
  private backPressed: boolean;
  constructor(props: RNNModalProps) {
    super(props);
    this.backPressed = false;
  }

  componentDidMount() {
    this.backHandler = BackHandler.addEventListener('hardwareBackPress', () => {
      this.backPressed = true;
      return false;
    });
  }
  componentWillUnmount() {
    console.log(`componentWillUnmount`);
    this.backHandler?.remove();
  }

  componentDidUpdate() {
    if (this.backPressed) {
      this.backPressed = false;
    }
  }
  render() {
    console.log(`render`, this.props);
    const p = { visible: this.props.visible };
    if (this.props.visible && !this.backPressed) {
      return (
        <RNNModalViewManager {...p}>
          <SafeAreaView style={styles.container}>
            <View style={styles.container}>{this.props.children}</View>
          </SafeAreaView>
        </RNNModalViewManager>
      );
    } else {
      return <></>;
    }
  }
}

const styles = StyleSheet.create({
  modal: {
    //s: 'absolute',
  },
  container: {
    top: 0,
    flex: 1,
  },
});
