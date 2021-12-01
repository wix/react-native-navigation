import React from 'react';
import {
  NavigationComponent,
  NavigationButtonPressedEvent,
  NavigationComponentProps,
  Options,
} from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const {
  OPEN_LEFT_SIDE_MENU_BTN,
  OPEN_RIGHT_SIDE_MENU_BTN,
  CENTER_SCREEN_HEADER,
  CHANGE_LEFT_SIDE_MENU_WIDTH_BTN,
  CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN,
  DISABLE_DRAWERS,
  ENABLE_DRAWERS,
} = testIDs;

// @ts-ignore TSC is unhappy as leftButtons is defined as an object instead of an array. Declaring buttons as a single object is not reflected in Options, but still supported.
export default class SideMenuCenterScreen extends NavigationComponent {
  static options(): Options {
    return {
      sideMenu: {
        left: {
          width: 250,
        },
        right: {
          width: 250,
        },
      },
      topBar: {
        testID: CENTER_SCREEN_HEADER,
        title: {
          text: 'Center',
        },

        leftButtons: [
          {
            id: 'sideMenu',
            icon: require('../../img/menu.png'),
          },
        ],
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
        <Button
          label="Change Left Drawer Width"
          testID={CHANGE_LEFT_SIDE_MENU_WIDTH_BTN}
          onPress={() => this.changeDrawerWidth('left', 50)}
        />
        <Button
          label="Change Right Drawer Width"
          testID={CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN}
          onPress={() => this.changeDrawerWidth('right', 50)}
        />
        <Button
          label="Disable Drawers"
          testID={DISABLE_DRAWERS}
          onPress={() => this.toggleDrawers(false)}
        />
        <Button
          label="Enable Drawers"
          testID={ENABLE_DRAWERS}
          onPress={() => this.toggleDrawers(true)}
        />
      </Root>
    );
  }

  toggleDrawers = (enabled: boolean) => {
    Navigation.mergeOptions(this, {
      sideMenu: {
        left: {
          enabled: enabled,
        },
        right: {
          enabled: enabled,
        },
      },
    });
  };
  open = (side: 'left' | 'right') =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: {
          visible: true,
        },
      },
    });

  changeDrawerWidth = (side: 'left' | 'right', newWidth: number) => {
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: {
          width: newWidth,
        },
      },
    });
  };
}
