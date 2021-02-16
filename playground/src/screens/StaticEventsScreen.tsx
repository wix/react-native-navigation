import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Navigation from '../services/Navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  PUSH_BTN,
  PUSH_DISABLED_BACK_BTN,
  PUSH_DISABLED_HARDWARE_BACK_BTN,
  POP_BTN,
  STATIC_EVENTS_OVERLAY_BTN,
  MODAL_BTN,
  SET_ROOT_BTN,
  SHOW_RIGHT_BUTTON,
  ROUND_BUTTON,
} = testIDs;

export default class StaticEventsScreen extends React.Component<NavigationComponentProps> {
  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label="Show Overlay"
          testID={STATIC_EVENTS_OVERLAY_BTN}
          onPress={this.showEventsOverlay}
        />
        <Button label="Push" testID={PUSH_BTN} onPress={this.push} />
        <Button
          label="Push disabled back button"
          testID={PUSH_DISABLED_BACK_BTN}
          onPress={this.pushDisabledBackButton}
        />
        <Button
          label="Push disabled hardware back button"
          testID={PUSH_DISABLED_HARDWARE_BACK_BTN}
          onPress={this.pushDisabledHardwareBackButton}
        />
        <Button label="Pop" testID={POP_BTN} onPress={this.pop} />
        <Button label="Show Modal" testID={MODAL_BTN} onPress={this.showModal} />
        <Button label="Set Root" testID={SET_ROOT_BTN} onPress={this.setRoot} />
        <Button
          label="Show Custom Right Button"
          testID={SHOW_RIGHT_BUTTON}
          onPress={this.showRightButton}
        />
      </Root>
    );
  }

  showRightButton = () => {
    Navigation.mergeOptions(this, {
      topBar: {
        rightButtons: [
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              id: 'ROUND_COMPONENT',
              name: Screens.RoundButton,
              passProps: {
                title: 'Two',
                timesCreated: 1,
              },
            },
          },
        ],
      },
    });
  };

  showModal = () => {
    Navigation.showModal({
      component: {
        name: Screens.Modal,
      },
    });
  };

  showEventsOverlay = () =>
    Navigation.showOverlay(Screens.EventsOverlay, {
      overlay: {
        interceptTouchOutside: false,
      },
    });
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
  pop = () => Navigation.pop(this);
  setRoot = () => Navigation.setRoot(Screens.SetRoot);
}
