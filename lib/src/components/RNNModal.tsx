import React from 'react';
import { ViewProps, StyleSheet, View, BackHandler } from 'react-native';
import { Navigation } from '..'; // this is cyclic we must have a fix!!
import { NavigationFunctionComponent } from '../interfaces/NavigationFunctionComponent';

export interface RNNModalProps extends ViewProps {
  animationType?: 'none' | 'slide' | 'fade' | null;
  presentationStyle?: 'fullScreen' | 'pageSheet' | 'formSheet' | 'overFullScreen' | null;
  transparent?: boolean;
  statusBarTranslucent?: boolean;
  hardwareAccelerated?: boolean;
  visible?: boolean;
  //TODO extend to have more of ModalOptions
}

let incID = 0;

export class RNNModal extends React.Component<RNNModalProps> {
  private displayed: boolean;
  private modalId: string;
  private backHandler?: any;
  constructor(props: RNNModalProps) {
    super(props);
    this.modalId = `RNNModal${++incID}`;
    this.displayed = false;
    this.registerModalComponent();
  }
  componentDidMount() {
    this.backHandler = BackHandler.addEventListener('hardwareBackPress', () => {
      this.displayed = false;
      return false;
    });
  }
  componentWillUnmount() {
    this.backHandler?.remove();
  }
  registerModalComponent() {
    let component: NavigationFunctionComponent<RNNModalProps> = (modalProps) => {
      return (
        <View style={styles.container} {...modalProps}>
          {this.props.children}
        </View>
      );
    };
    Navigation.registerComponent(this.modalId, () => component);
  }
  componentDidUpdate(prevProps: RNNModalProps) {
    if (this.props.visible !== prevProps.visible && !this.displayed) {
      Navigation.showModal({
        component: {
          id: this.modalId,
          name: this.modalId,
        },
      });
      this.displayed = true;
    } else {
      if (this.displayed) {
        Navigation.dismissModal(this.modalId);
      }
      this.displayed = false;
    }
  }
  render() {
    return <></>;
  }
}

const styles = StyleSheet.create({
  modal: {
    //s: 'absolute',
  },
  container: {
    // top: 0,
    // flex: 1,
  },
});
