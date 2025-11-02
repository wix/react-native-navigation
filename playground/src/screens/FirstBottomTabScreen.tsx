import React, { Component } from 'react';
import { Text } from 'react-native';
import { NavigationProps, Options } from 'react-native-navigation';

import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from './../services/Navigation';
import Screens from './Screens';
import { component } from '../commons/Layouts';
import testIDs from '../testIDs';
import bottomTabsStruct from './BottomTabsLayoutStructure';

export class MountedBottomTabScreensState {
  static mountedBottomTabScreens: string[] = [];
  static callback: (mountedBottomTabScreens: string[]) => void = () => {};

  static addScreen(screen: string) {
    this.mountedBottomTabScreens.push(screen);
    this.callback(this.mountedBottomTabScreens);
  }
}

const {
  SWITCH_TAB_BY_INDEX_BTN,
  SWITCH_TAB_BY_COMPONENT_ID_BTN,
  SET_BADGE_BTN,
  CLEAR_BADGE_BTN,
  HIDE_TABS_BTN,
  SHOW_TABS_BTN,
  HIDE_TABS_PUSH_BTN,
  STYLIZE_TABS_BTN,
  FIRST_TAB_BAR_BUTTON,
  MOUNTED_SCREENS_TEXT,
  SCREEN_ROOT,
  SET_ROOT_BTN,
  BOTTOM_TABS,
} = testIDs;

interface NavigationState {
  mountedBottomTabScreens: string[];
}

export default class FirstBottomTabScreen extends Component<NavigationProps, NavigationState> {
  static options(): Options {
    return {
      layout: {
        orientation: ['portrait', 'landscape'],
      },
      topBar: {
        title: {
          text: 'First Tab',
        },
      },
      bottomTab: {
        testID: FIRST_TAB_BAR_BUTTON,
        icon: require('../../img/whatshot.png'),
        text: 'Tab 1',
        dotIndicator: { visible: true },
      },
    };
  }

  constructor(props: NavigationProps) {
    super(props);
    this.state = { mountedBottomTabScreens: [] };

    MountedBottomTabScreensState.callback = (mountedBottomTabScreens: string[]) => {
      this.setState({ mountedBottomTabScreens: mountedBottomTabScreens });
    };
  }

  componentDidMount() {
    MountedBottomTabScreensState.addScreen('FirstBottomTabScreen');
  }

  badgeVisible = true;
  bottomTabPressedListener = Navigation.events().registerBottomTabPressedListener((event) => {
    if (event.tabIndex == 2) {
      alert('BottomTabPressed');
    }
  });

  render() {
    return (
      <Root componentId={this.props.componentId} testID={SCREEN_ROOT}>
        <Button
          label="Switch Tab by Index"
          testID={SWITCH_TAB_BY_INDEX_BTN}
          onPress={this.switchTabByIndex}
        />
        <Button
          label="Switch Tab by componentId"
          testID={SWITCH_TAB_BY_COMPONENT_ID_BTN}
          onPress={this.switchTabByComponentId}
        />
        <Button label="Set Badge" testID={SET_BADGE_BTN} onPress={() => this.setBadge('NEW')} />
        <Button label="Clear Badge" testID={CLEAR_BADGE_BTN} onPress={() => this.setBadge('')} />
        <Button label="Show Notification Dot" onPress={() => this.setNotificationDot(true)} />
        <Button label="Hide Notification Dot" onPress={() => this.setNotificationDot(false)} />
        <Button label="Hide Tabs" testID={HIDE_TABS_BTN} onPress={() => this.toggleTabs(false)} />
        <Button label="Show Tabs" testID={SHOW_TABS_BTN} onPress={() => this.toggleTabs(true)} />
        <Button
          label="Hide Tabs on Push"
          testID={HIDE_TABS_PUSH_BTN}
          onPress={this.hideTabsOnPush}
        />
        <Button label="Push" onPress={this.push} />
        <Button label="Add border and shadow" onPress={this.modifyBottomTabs} />
        <Button label="Stylize" testID={STYLIZE_TABS_BTN} onPress={this.stylizeBottomTabs} />
        <Button label="Set Styled Root" testID={SET_ROOT_BTN} onPress={this.setStylizedRoot} />

        <Text testID={MOUNTED_SCREENS_TEXT}>
          Mounted screens: {this.state.mountedBottomTabScreens.join(', ')}
        </Text>
      </Root>
    );
  }

  componentWillUnmount() {
    this.bottomTabPressedListener.remove();
  }

  modifyBottomTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        borderColor: 'red',
        borderWidth: 1,
        shadow: {
          color: '#65C888',
          radius: 20,
          opacity: 0.8,
        },
      },
    });
  };

  stylizeBottomTabs = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        drawBehind: true,
        translucent: true,
        blurRadius: 2.0,
        layoutStyle: 'compact',
        bottomMargin: 15,
        cornerRadius: 15,
        elevation: 1,
        backgroundColor: 'rgba(255, 182, 193, 0.25)',
      },
    });
  };

  setStylizedRoot = () => {
    Navigation.setRoot({
      bottomTabs: {
        children: [...bottomTabsStruct.children],
        options: {
          bottomTabs: {
            testID: BOTTOM_TABS,
            drawBehind: true,
            translucent: true,
            blurRadius: 2.0,
            layoutStyle: 'compact',
            bottomMargin: 15,
            cornerRadius: 15,
            elevation: 1,
            backgroundColor: 'rgba(119,202,212,0.56)',
          },
        },
      },
    });
  };

  switchTabByIndex = () =>
    Navigation.mergeOptions(this, {
      bottomTabs: {
        currentTabIndex: 1,
      },
    });

  switchTabByComponentId = () =>
    Navigation.mergeOptions('SecondTab', {
      bottomTabs: {
        currentTabId: 'SecondTab',
      },
    });

  setBadge = (badge: string) => {
    this.badgeVisible = !!badge;
    Navigation.mergeOptions(this, {
      bottomTab: { badge, animateBadge: true },
    });
  };

  setNotificationDot = (visible: boolean) => {
    Navigation.mergeOptions(this, {
      bottomTab: {
        ...(visible ? { badge: '' } : {}),
        dotIndicator: { visible },
      },
    });
  };

  toggleTabs = (visible: boolean) =>
    Navigation.mergeOptions(this, {
      bottomTabs: { visible },
    });

  hideTabsOnPush = () =>
    Navigation.push(
      this,
      component(Screens.Pushed, {
        bottomTabs: { visible: false },
      })
    );

  push = () => Navigation.push(this, Screens.Pushed);
}
