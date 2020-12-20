import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import { component } from '../commons/Layouts';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';
import { Text } from 'react-native';

const {
  SHOW_OVERLAY_BTN,
  SHOW_TOUCH_THROUGH_OVERLAY_BTN,
  ALERT_BUTTON,
  SET_ROOT_BTN,
  TOAST_BTN,
  SHOW_FULLSCREEN_OVERLAY_BTN,
  OVERLAY_DISMISSED_COUNT,
} = testIDs;

interface State {
  overlayDismissedCount?: number;
}
interface Props extends NavigationComponentProps {
  incrementDismissedOverlays: any;
}
export default class OverlayScreen extends React.Component<Props, State> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Overlay',
        },
      },
    };
  }

  state = {
    overlayDismissedCount: 0,
  };

  constructor(props: Props) {
    super(props);
    Navigation.events().registerCommandCompletedListener((event) => {
      if (event.commandName === 'dismissAllOverlays') {
        if (this.props.incrementDismissedOverlays) {
          this.props.incrementDismissedOverlays();
        }
      }
    });
    this.incrementDismissedOverlays = this.incrementDismissedOverlays.bind(this);
  }

  incrementDismissedOverlays() {
    this.setState({
      overlayDismissedCount: this.state.overlayDismissedCount + 1,
    });
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Text testID={OVERLAY_DISMISSED_COUNT}>{this.state?.overlayDismissedCount || ''}</Text>
        <Button label="Toast" testID={TOAST_BTN} onPress={this.toast} />
        <Button label="Alert" testID={ALERT_BUTTON} onPress={() => alert('Alert displayed')} />
        <Button
          label="Show overlay"
          testID={SHOW_OVERLAY_BTN}
          onPress={() => this.showOverlay(true)}
        />
        <Button
          label="Show fullscreen overlay"
          testID={SHOW_FULLSCREEN_OVERLAY_BTN}
          onPress={() => this.showFullScreenOverlay()}
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
    Navigation.showOverlay(
      Screens.OverlayAlert,
      {
        layout: { componentBackgroundColor: 'transparent' },
        overlay: { interceptTouchOutside },
      },
      {
        incrementDismissedOverlays:
          this.props.incrementDismissedOverlays || this.incrementDismissedOverlays,
      }
    );

  showFullScreenOverlay = () =>
    Navigation.showOverlay(
      Screens.Overlay,
      {},
      {
        incrementDismissedOverlays:
          this.props.incrementDismissedOverlays || this.incrementDismissedOverlays,
      }
    );

  setRoot = () => Navigation.setRoot({ root: component(Screens.Pushed) });

  showOverlayWithScrollView = () =>
    Navigation.showOverlay(Screens.ScrollViewOverlay, {
      layout: { componentBackgroundColor: 'transparent' },
    });
}
