import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import { component } from '../commons/Layouts';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const {
  SHOW_OVERLAY_BTN,
  SHOW_TOUCH_THROUGH_OVERLAY_BTN,
  ALERT_BUTTON,
  SET_ROOT_BTN,
  TOAST_BTN,
} = testIDs;

export default class OverlayScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Overlay',
        },
      },
    };
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Toast" testID={TOAST_BTN} onPress={this.toast} />
        <Button label="Alert" testID={ALERT_BUTTON} onPress={() => alert('Alert displayed')} />
        <Button
          label="Show overlay"
          testID={SHOW_OVERLAY_BTN}
          onPress={() => this.showOverlay(true)}
        />
        <Button
          label="Show touch through overlay"
          testID={SHOW_TOUCH_THROUGH_OVERLAY_BTN}
          onPress={() => this.showOverlay(false)}
        />
        <Button label="Show overlay with ScrollView" onPress={this.showOverlayWithScrollView} />
        <Button label="Set Root" testID={SET_ROOT_BTN} onPress={this.setRoot} />
      </Root>
    );
  }

  toast = () => Navigation.showOverlay(Screens.Toast);

  showOverlay = (interceptTouchOutside: boolean) =>
    Navigation.showOverlay(Screens.OverlayAlert, {
      layout: { componentBackgroundColor: 'transparent' },
      overlay: { interceptTouchOutside },
    });

  setRoot = () => Navigation.setRoot({ root: component(Screens.Pushed) });

  showOverlayWithScrollView = () =>
    Navigation.showOverlay(Screens.ScrollViewOverlay, {
      layout: { componentBackgroundColor: 'transparent' },
    });
}
