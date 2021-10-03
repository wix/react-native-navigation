import React from 'react';
import { requireNativeComponent, ViewProps, StyleSheet } from 'react-native';
import { View } from 'react-native-ui-lib';
export interface RNNModalProps extends ViewProps {
  presentationStyle: 'unspecified' | 'none' | 'overCurrentContext';
  blurOnUnmount: boolean;
  onShow?: () => any;
  onRequestDismiss: () => any;
  visible: boolean;
}

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');

export class RNNModal extends React.Component<RNNModalProps> {
  static defaultProps = {
    presentationStyle: 'unspecified',
    blurOnUnmount: false,
  };
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
    position: 'absolute',
  },
  container: {
    top: 0,
    flex: 1,
  },
});
