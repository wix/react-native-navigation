import React from 'react';
import {
  NavigationComponent,
  NavigationButtonPressedEvent,
  NavigationComponentProps,
} from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const { OPEN_LEFT_SIDE_MENU_BTN, OPEN_RIGHT_SIDE_MENU_BTN, CENTER_SCREEN_HEADER } = testIDs;

export default class SideMenuCenterScreen extends NavigationComponent {
  static options() {
    return {
      topBar: {
        testID: CENTER_SCREEN_HEADER,
        title: {
          text: 'Center',
        },
        leftButtons: {
          id: 'sideMenu',
          icon: require('../../img/menu.png'),
        },
      },
    };
  }

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    if (buttonId === 'sideMenu') this.open('left');
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label="Open Left"
          testID={OPEN_LEFT_SIDE_MENU_BTN}
          onPress={() => this.open('left')}
        />
        <Button
          label="Open Right"
          testID={OPEN_RIGHT_SIDE_MENU_BTN}
          onPress={() => this.open('right')}
        />
      </Root>
    );
  }

  open = (side: 'left' | 'right') =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: { visible: true },
      },
    });
}
