import React from 'react';
import { requireNativeComponent, ViewProps, StyleSheet } from 'react-native';
import { View } from 'react-native-ui-lib';
export interface RNNModalProps extends ViewProps {
  // animationType?: 'none' | 'slide' | 'fade' | null;
  // presentationStyle?: 'fullScreen' | 'pageSheet' | 'formSheet' | 'overFullScreen' | null;
  // transparent?: boolean;
  // statusBarTranslucent?: boolean;
  // hardwareAccelerated?: boolean;
  onShow?: () => any;
  onDismiss?: () => any;
  onRequestDismiss: () => any;
  visible: boolean;
}

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');

export class RNNModal extends React.Component<RNNModalProps> {
  constructor(props: RNNModalProps) {
    super(props);
  }
  render() {
    const p = { ...this.props, style: styles.modal };
    if (this.props.visible) {
      return (
        <RNNModalViewManager {...p}>
          <View style={styles.container} collapsable={false}>
            {this.props.children}
          </View>
        </RNNModalViewManager>
      );
    } else {
      return null;
    }
  }
}

const styles = StyleSheet.create({
  modal: {
    // backgroundColor: 'yellow',
    position: 'absolute',
  },
  container: {
    // backgroundColor: 'red',
    top: 0,
    flex: 1,
  },
});
