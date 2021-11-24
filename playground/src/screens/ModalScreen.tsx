import React from 'react';
import { NavigationComponent, Modal as RNNModal } from 'react-native-navigation';
import last from 'lodash/last';
import concat from 'lodash/concat';
import forEach from 'lodash/forEach';
import head from 'lodash/head';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from './../services/Navigation';
import { component } from './../commons/Layouts';
import { stack } from '../commons/Layouts';
import Screens from './Screens';
import flags from '../flags';
import testIDs from '../testIDs';
import { Dimensions, Modal, Image, Platform, StyleSheet } from 'react-native';
import { View } from 'react-native-ui-lib';
import { SafeAreaView } from 'react-native';

const height = Math.round(Dimensions.get('window').height);
const MODAL_ANIMATION_DURATION = 350;
const {
  PUSH_BTN,
  MODAL_SCREEN_HEADER,
  MODAL_BTN,
  SHOW_MODAL_FROM_DECLARED_BUTTON,
  OVERLAY_BTN,
  MODAL_DISABLED_BACK_BTN,
  DISMISS_MODAL_BTN,
  DISMISS_REACT_MODAL_BTN,
  DISMISS_UNKNOWN_MODAL_BTN,
  MODAL_LIFECYCLE_BTN,
  MODAL_COMMANDS_BTN,
  DISMISS_PREVIOUS_MODAL_BTN,
  DISMISS_ALL_PREVIOUS_MODAL_BTN,
  DISMISS_ALL_MODALS_BTN,
  DISMISS_FIRST_MODAL_BTN,
  SET_ROOT,
  TOGGLE_REACT_DECLARED_MODAL,
} = testIDs;

interface Props {
  previousModalIds?: string[];
  modalPosition?: number;
}

interface State {
  swipeableToDismiss: boolean;
  modalVisible: boolean;
}

export default class ModalScreen extends NavigationComponent<Props, State> {
  static options() {
    return {
      topBar: {
        testID: MODAL_SCREEN_HEADER,
        title: {
          text: 'Modal',
        },
      },
    };
  }

  constructor(props: any) {
    super(props);
    this.state = {
      swipeableToDismiss: false,
      modalVisible: false,
    };
  }

  render() {
    return (
      <Root
        componentId={this.props.componentId}
        footer={`Modal Stack Position: ${this.getModalPosition()}`}
      >
        <Button label="Show Modal" testID={MODAL_BTN} onPress={this.showModal} />
        {flags.useCustomAnimations && (
          <Button label="Back.Compat. Show Modal Anim" onPress={this.showModalWithTransition} />
        )}
        {flags.useCustomAnimations && (
          <Button label="New! Show Modal Push Anim" onPress={this.showModalWithTransitionPush} />
        )}
        {!this.props.previousModalIds && (
          <Button
            label="Show Disabled Hardware Back Modal"
            testID={MODAL_DISABLED_BACK_BTN}
            onPress={this.showDisabledHardwareBackModal}
          />
        )}
        <Button label="Dismiss Modal" testID={DISMISS_MODAL_BTN} onPress={this.dismissModal} />
        <Button
          label="Dismiss Unknown Modal"
          testID={DISMISS_UNKNOWN_MODAL_BTN}
          onPress={this.dismissUnknownModal}
        />
        <Button
          label="Modal Lifecycle"
          testID={MODAL_LIFECYCLE_BTN}
          onPress={this.modalLifecycle}
        />
        <Button label="Modal Commands" testID={MODAL_COMMANDS_BTN} onPress={this.modalCommands} />
        {this.getPreviousModalId() && (
          <Button
            label="Dismiss Previous Modal"
            testID={DISMISS_PREVIOUS_MODAL_BTN}
            onPress={this.dismissPreviousModal}
          />
        )}
        {this.props.previousModalIds && (
          <Button
            label="Dismiss All Previous Modals"
            testID={DISMISS_ALL_PREVIOUS_MODAL_BTN}
            onPress={this.dismissAllPreviousModals}
          />
        )}
        <Button
          label="Dismiss All Modals"
          testID={DISMISS_ALL_MODALS_BTN}
          onPress={this.dismissAllModals}
        />
        {this.props.previousModalIds && (
          <Button
            label="Dismiss First Modal"
            testID={DISMISS_FIRST_MODAL_BTN}
            onPress={this.dismissFirstModal}
          />
        )}
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button label="Set Root" testID={SET_ROOT} onPress={this.setRoot} />
        <Button
          label="Toggle declared modal"
          testID={TOGGLE_REACT_DECLARED_MODAL}
          onPress={this.toggleModal}
        />
        {Platform.OS === 'android' && (
          <RNNModal
            visible={this.state.modalVisible}
            onRequestClose={() => this.setState({ modalVisible: false })}
          >
            <Button
              label="Dismiss declared Modal"
              testID={DISMISS_REACT_MODAL_BTN}
              onPress={this.toggleModal}
            />
            <Button
              label="Show Modal"
              testID={SHOW_MODAL_FROM_DECLARED_BUTTON}
              onPress={this.showModal}
            />
            <Button label="Show Overlay" testID={OVERLAY_BTN} onPress={this.showOverlay} />
            <View style={styles.logoContainer}>
              <Image
                style={styles.tinyLogo}
                source={{
                  uri: 'https://wix.github.io/react-native-navigation/img/logo.png',
                }}
              />
            </View>
          </RNNModal>
        )}
        {Platform.OS === 'ios' && (
          <Modal
            visible={this.state.modalVisible}
            onRequestClose={() => this.setState({ modalVisible: false })}
          >
            <SafeAreaView>
              <Button
                label="Dismiss declared Modal"
                testID={DISMISS_REACT_MODAL_BTN}
                onPress={this.toggleModal}
              />
              <Button
                label="Show Modal"
                testID={SHOW_MODAL_FROM_DECLARED_BUTTON}
                onPress={this.showModal}
              />
              <Button label="Show Overlay" testID={OVERLAY_BTN} onPress={this.showOverlay} />
              <View style={styles.logoContainer}>
                <Image
                  style={styles.tinyLogo}
                  source={{
                    uri: 'https://reactnative.dev/img/tiny_logo.png',
                  }}
                />
              </View>
            </SafeAreaView>
          </Modal>
        )}
      </Root>
    );
  }

  showOverlay = async () => {
    await Navigation.showOverlay(Screens.OverlayAlert, {
      layout: { componentBackgroundColor: 'transparent' },
      overlay: { interceptTouchOutside: false },
    });
  };

  toggleModal = () => this.setState({ modalVisible: !this.state.modalVisible });

  showModalWithTransition = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        options: {
          animations: {
            showModal: {
              translationY: {
                from: height,
                to: 0,
                duration: MODAL_ANIMATION_DURATION,
                interpolation: { type: 'decelerate' },
              },
            },
            dismissModal: {
              translationY: {
                from: 0,
                to: height,
                duration: MODAL_ANIMATION_DURATION,
                interpolation: { type: 'decelerate' },
              },
            },
          },
        },
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: concat([], this.props.previousModalIds || [], this.props.componentId),
        },
      },
    });
  };

  showModalWithTransitionPush = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        options: {
          animations: {
            showModal: {
              enter: {
                translationY: {
                  from: height,
                  to: 0,
                  duration: MODAL_ANIMATION_DURATION,
                  interpolation: { type: 'decelerate' },
                },
              },
              exit: {
                translationY: {
                  from: 0,
                  to: -height,
                  duration: MODAL_ANIMATION_DURATION,
                  interpolation: { type: 'decelerate' },
                },
              },
            },
            dismissModal: {
              enter: {
                translationY: {
                  from: -height,
                  to: 0,
                  duration: MODAL_ANIMATION_DURATION,
                  interpolation: { type: 'decelerate' },
                },
              },
              exit: {
                translationY: {
                  from: 0,
                  to: height,
                  duration: MODAL_ANIMATION_DURATION,
                  interpolation: { type: 'decelerate' },
                },
              },
            },
          },
        },
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: concat([], this.props.previousModalIds || [], this.props.componentId),
        },
      },
    });
  };
  showModal = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        passProps: {
          modalPosition: this.getModalPosition() + 1,
          previousModalIds: concat([], this.props.previousModalIds || [], this.props.componentId),
        },
      },
    });
  };

  showDisabledHardwareBackModal = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
        options: {
          hardwareBackButton: { dismissModalOnPress: false },
        },
      },
    });
  };

  dismissModal = async () => await Navigation.dismissModal(this.props.componentId);

  dismissPreviousModal = () => Navigation.dismissModal(this.getPreviousModalId());

  dismissUnknownModal = () => Navigation.dismissModal('unknown');

  dismissAllPreviousModals = () =>
    forEach(this.props.previousModalIds, (id) => Navigation.dismissModal(id));

  dismissFirstModal = () => Navigation.dismissModal(head(this.props.previousModalIds)!);

  dismissAllModals = () => Navigation.dismissAllModals();

  modalLifecycle = () =>
    Navigation.showModal({
      component: {
        name: Screens.Lifecycle,
        passProps: { isModal: true },
      },
    });

  modalCommands = () => Navigation.push(this, component(Screens.ModalCommands));

  push = () => Navigation.push(this, Screens.Pushed);

  setRoot = () => Navigation.setRoot(stack(Screens.Pushed));

  getModalPosition = () => this.props.modalPosition || 1;

  getPreviousModalId = () => last(this.props.previousModalIds)!;
}

const styles = StyleSheet.create({
  logoContainer: {
    justifyContent: 'center',
    flex: 1,
    flexDirection: 'row',
  },
  tinyLogo: {
    justifyContent: 'center',
    width: 128,
    height: 128,
  },
});
