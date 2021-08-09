import React from 'react';
import { requireNativeComponent, ViewProps } from 'react-native';
export interface RNNModalProps extends ViewProps {
  animationType?: 'none' | 'slide' | 'fade' | null;
  presentationStyle?: 'fullScreen' | 'pageSheet' | 'formSheet' | 'overFullScreen' | null;
  transparent?: boolean;
  statusBarTranslucent?: boolean;
  hardwareAccelerated?: boolean;
  visible?: boolean;
}

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');

export class RNNModal extends React.Component<RNNModalProps> {
  constructor(props: RNNModalProps) {
    super(props);
  }
  render() {
    console.log(`xxxxxxx, visibility: ${this.props.visible}`);
    if (this.props.visible !== true) return null;
    return <RNNModalViewManager {...this.props} />;
  }
}
