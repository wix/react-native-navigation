import React from 'react';
import {
  NavigationComponent,
  NavigationButtonPressedEvent,
  NavigationProps,
  Options,
} from 'react-native-navigation';
import { Text, TouchableOpacity } from 'react-native';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const {
  OPEN_LEFT_SIDE_MENU_BTN,
  OPEN_RIGHT_SIDE_MENU_BTN,
  TOGGLE_SIDE_MENU_OPEN_MODE_BTN,
  CENTER_SCREEN_HEADER,
  CHANGE_LEFT_SIDE_MENU_WIDTH_BTN,
  CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN,
  DISABLE_DRAWERS,
  ENABLE_DRAWERS,
  SIDE_MENU_CENTER_SCREEN_CONTAINER
} = testIDs;

interface ScreenState {
  openMode: 'aboveContent' | 'pushContent' | undefined;
}

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

  readonly state: ScreenState;

  constructor(props: NavigationProps) {
    super(props);
    this.state = {
      openMode: 'aboveContent',
    };
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
    if (buttonId === 'sideMenu') {
      this.open('left');
    }
  }

  render() {
    const { openMode } = this.state;

    return (
      <Root componentId={this.props.componentId} testID={SIDE_MENU_CENTER_SCREEN_CONTAINER}>
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
          onPress={() => this.changeDrawerWidth('left', 100)}
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

        <TouchableOpacity
          onPress={this.toggleOpenMode}
          testID={TOGGLE_SIDE_MENU_OPEN_MODE_BTN}
          style={{ margin: 10, padding: 10, backgroundColor: '#ddd', borderRadius: 5 }}
        >
          <Text>Open mode: {openMode || 'undefined'}</Text>
        </TouchableOpacity>
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

  toggleOpenMode = () => {
    this.setState((state: ScreenState) => ({
      openMode: state.openMode === 'aboveContent'
        ? 'pushContent'
        : (state.openMode === 'pushContent'
            ? undefined
            : 'aboveContent'
        ),
    }));
  };

  open = (side: 'left' | 'right') =>
    Navigation.mergeOptions(this, {
      sideMenu: {
        [side]: {
          visible: true,
          openMode: this.state.openMode,
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
