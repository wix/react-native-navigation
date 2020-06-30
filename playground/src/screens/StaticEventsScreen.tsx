import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Navigation from '../services/Navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';

const { PUSH_BTN, POP_BTN, STATIC_EVENTS_OVERLAY_BTN, MODAL_BTN, SET_ROOT_BTN } = testIDs;

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
        <Button label="Pop" testID={POP_BTN} onPress={this.pop} />
        <Button label="Show Modal" testID={MODAL_BTN} onPress={this.showModal} />
        <Button label="Set Root" testID={SET_ROOT_BTN} onPress={this.setRoot} />
      </Root>
    );
  }

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
  pop = () => Navigation.pop(this);
  setRoot = () => Navigation.setRoot(Screens.SetRoot);
}
