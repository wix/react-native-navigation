import React from 'react';
import { NavigationProps, Options } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const {
  PUSH_BTN,
  BACK_BUTTON_SCREEN_HEADER,
  STATIC_EVENTS_OVERLAY_BTN,
  PUSH_DISABLED_BACK_BTN,
  PUSH_DISABLED_HARDWARE_BACK_BTN,
  MODAL_DISABLED_BACK_BTN,
  TOGGLE_BACK_BUTTON_VISIBILITY,
  BACK_BUTTON,
  PUSH_BACK_BUTTON_ICON_BACKGROUND,
} = testIDs;

export default class BackButtonScreen extends React.Component<NavigationProps> {
  visible: boolean = true;
  static options(): Options {
    return {
      topBar: {
        testID: BACK_BUTTON_SCREEN_HEADER,
        title: {
          text: 'Back Button',
        },
        backButton: {
          testID: BACK_BUTTON,
        },
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button
          label="Push disabled back button pop"
          testID={PUSH_DISABLED_BACK_BTN}
          onPress={this.pushDisabledBackButton}
        />
        <Button
          label="Push disabled hardware back button pop"
          testID={PUSH_DISABLED_HARDWARE_BACK_BTN}
          onPress={this.pushDisabledHardwareBackButton}
        />
        <Button
          label="Show Overlay"
          testID={STATIC_EVENTS_OVERLAY_BTN}
          onPress={this.showEventsOverlay}
        />
        <Button
          label="Toggle Visibility"
          testID={TOGGLE_BACK_BUTTON_VISIBILITY}
          onPress={this.toggleVisibility}
        />
        <Button
          label="Push with Icon Background"
          testID={PUSH_BACK_BUTTON_ICON_BACKGROUND}
          onPress={this.pushWithIconBackground}
        />
        <Button label="Modal" testID={MODAL_DISABLED_BACK_BTN} onPress={this.showModal} />
      </Root>
    );
  }

  push = () => Navigation.push(this, Screens.Pushed);

  pushDisabledBackButton = () =>
    Navigation.push(this, Screens.Pushed, {
      topBar: {
        backButton: {
          popStackOnPress: false,
        },
      },
    });

  pushDisabledHardwareBackButton = () =>
    Navigation.push(this, Screens.Pushed, {
      hardwareBackButton: {
        popStackOnPress: false,
      },
    });

  pushWithIconBackground = () =>
    Navigation.push(this, Screens.Pushed, {
      topBar: {
        title: {
          text: 'Back Button with Icon Bg',
        },
        rightButtons: [
          {
            id: 'RIGHT',
            icon: require('../../img/navicon_add.png'),
            color: 'white', // Icon color - white to be visible on red background
            iconBackground: {
              color: 'orange',
              cornerRadius: 20,
              width: 40,
              height: 40,
            },
          },
        ],
        backButton: {
          id: 'BACK',
          icon: require('../../img/navicon_add.png'),
          color: 'white', // Icon color - white to be visible on red background
          iconBackground: {
            color: 'red',
            cornerRadius: 20,
            width: 40,
            height: 40,
          },
        },
      },
    });

  showEventsOverlay = () =>
    Navigation.showOverlay(Screens.EventsOverlay, {
      overlay: {
        interceptTouchOutside: false,
      },
    });

  showModal = () =>
    Navigation.showModal(Screens.Modal, {
      hardwareBackButton: {
        dismissModalOnPress: false,
      },
    });

  toggleVisibility = () => {
    this.visible = !this.visible;
    Navigation.mergeOptions(this, {
      topBar: {
        backButton: {
          visible: this.visible,
        },
      },
    });
  };
}
