import React, {Component} from 'react';
import {AppRegistry, NativeModules} from 'react-native';
import uniqueId from 'lodash/uniqueId';
import get from 'lodash/get';
import forEach from 'lodash/each';

import Navigation from './../Navigation';

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

import * as newPlatformSpecific from './../platformSpecific';

function startSingleScreenApp(params) {
  const screen = params.screen;
  if (!screen.screen) {
    console.error('startSingleScreenApp(params): screen must include a screen property');
    return;
  }
  addNavigatorParams(screen);
  addNavigatorButtons(screen, params.drawer);
  addNavigationStyleParams(screen);
  screen.passProps = params.passProps;

  /*
   * adapt to new API
   */
  adaptTopTabs(screen, screen.navigatorID);
  screen.screenId = screen.screen;
  params.screen = adaptNavigationStyleToScreenStyle(screen);
  params.screen = adaptNavigationParams(screen);
  params.appStyle = convertStyleParams(params.appStyle);
  params.sideMenu = convertDrawerParamsToSideMenuParams(params.drawer);
  params.overrideBackPress = screen.overrideBackPress;
  params.animateShow = convertAnimationType(params.animationType);

  newPlatformSpecific.startApp(params);
}

function adaptTopTabs(screen, navigatorID) {
  forEach(get(screen, 'topTabs'), (tab) => {
    addNavigatorParams(tab);
    if (navigatorID) {
      tab.navigatorID = navigatorID;
    }
    adaptNavigationParams(tab);
  });
}

function navigatorPush(navigator, params) {
  addNavigatorParams(params, navigator);
  addNavigatorButtons(params);
  addTitleBarBackButtonIfNeeded(params);
  addNavigationStyleParams(params);

  adaptTopTabs(params, params.navigatorID);

  params.screenId = params.screen;
  let adapted = adaptNavigationStyleToScreenStyle(params);
  adapted = adaptNavigationParams(adapted);
  adapted.overrideBackPress = params.overrideBackPress;
  
  newPlatformSpecific.push(adapted);
}

function navigatorPop(navigator, params) {
  addNavigatorParams(params, navigator);

  params.screenId = params.screen;
  let adapted = adaptNavigationStyleToScreenStyle(params);
  adapted = adaptNavigationParams(adapted);

  newPlatformSpecific.pop(adapted);
}

function navigatorPopToRoot(navigator, params) {
  addNavigatorParams(params, navigator);

  params.screenId = params.screen;
  let adapted = adaptNavigationStyleToScreenStyle(params);
  adapted = adaptNavigationParams(adapted);

  newPlatformSpecific.popToRoot(adapted);
}

function navigatorResetTo(navigator, params) {
  addNavigatorParams(params, navigator);
  addNavigatorButtons(params);
  addNavigationStyleParams(params);

  adaptTopTabs(params, params.navigatorID);

  params.screenId = params.screen;
  let adapted = adaptNavigationStyleToScreenStyle(params);
  adapted = adaptNavigationParams(adapted);

  newPlatformSpecific.newStack(adapted);
}

function adaptNavigationStyleToScreenStyle(screen) {
  const { navigatorStyle, ...props } = screen;

  if (!navigatorStyle) {
    return screen;
  }

  screen.styleParams = convertStyleParams(navigatorStyle);

  return props;
}

function convertStyleParams(originalStyleObject) {
  if (!originalStyleObject) {
    return null;
  }

  return {
    statusBarColor: originalStyleObject.statusBarColor,
    topBarColor: originalStyleObject.navBarBackgroundColor,
    titleBarHidden: originalStyleObject.navBarHidden,
    titleBarTitleColor: originalStyleObject.navBarTextColor,
    titleBarButtonColor: originalStyleObject.navBarButtonColor,
    titleBarDisabledButtonColor: originalStyleObject.titleBarDisabledButtonColor,
    backButtonHidden: originalStyleObject.backButtonHidden,
    topTabsHidden: originalStyleObject.topTabsHidden,

    drawBelowTopBar: !originalStyleObject.drawUnderNavBar,

    topTabTextColor: originalStyleObject.topTabTextColor,
    selectedTopTabTextColor: originalStyleObject.selectedTopTabTextColor,
    selectedTopTabIndicatorHeight: originalStyleObject.selectedTopTabIndicatorHeight,
    selectedTopTabIndicatorColor: originalStyleObject.selectedTopTabIndicatorColor,

    drawScreenAboveBottomTabs: !originalStyleObject.drawUnderTabBar,

    bottomTabsColor: originalStyleObject.tabBarBackgroundColor,
    bottomTabsButtonColor: originalStyleObject.tabBarButtonColor,
    bottomTabsSelectedButtonColor: originalStyleObject.tabBarSelectedButtonColor,
    bottomTabsHidden: originalStyleObject.tabBarHidden,
    bottomTabsHiddenOnScroll: originalStyleObject.bottomTabsHiddenOnScroll,
    forceTitlesDisplay: originalStyleObject.forceTitlesDisplay,
    bottomTabBadgeTextColor: originalStyleObject.bottomTabBadgeTextColor,
    bottomTabBadgeBackgroundColor: originalStyleObject.bottomTabBadgeBackgroundColor,

    navigationBarColor: originalStyleObject.navigationBarColor
  }
}

function convertDrawerParamsToSideMenuParams(drawerParams) {
  const drawer = Object.assign({}, drawerParams);
  if (!drawer.left || !drawer.left.screen) {
    return null;
  }

  let result = {};
  result.disableOpenGesture = drawer.disableOpenGesture !== undefined;
  result.screenId = drawer.left.screen;
  addNavigatorParams(result);
  result = adaptNavigationParams(result);
  result.passProps = drawer.passProps;
  return result;
}

function adaptNavigationParams(screen) {
  screen.navigationParams = {
    screenInstanceID: screen.screenInstanceID,
    navigatorID: screen.navigatorID,
    navigatorEventID: screen.navigatorEventID
  };
  return screen;
}

function startTabBasedApp(params) {
  if (!params.tabs) {
    console.error('startTabBasedApp(params): params.tabs is required');
    return;
  }

  const newTabs = [];

  params.tabs.forEach(function(tab, idx) {
    addNavigatorParams(tab, null, idx);
    addNavigatorButtons(tab, params.drawer);
    addNavigationStyleParams(tab);
    addTabIcon(tab);
    tab.passProps = params.passProps;

    adaptTopTabs(tab, tab.navigatorID);

    tab.screenId = tab.screen;

    let newtab = adaptNavigationStyleToScreenStyle(tab);
    newtab = adaptNavigationParams(tab);
    newtab.overrideBackPress = tab.overrideBackPress;
    newTabs.push(newtab);
  });
  params.tabs = newTabs;

  params.appStyle = convertStyleParams(params.appStyle);
  params.sideMenu = convertDrawerParamsToSideMenuParams(params.drawer);
  params.animateShow = convertAnimationType(params.animationType);

  newPlatformSpecific.startApp(params);
}

function addTabIcon(tab) {
  if (tab.icon) {
    const icon = resolveAssetSource(tab.icon);
    if (icon) {
      tab.icon = icon.uri;
    }
  }

  if (!tab.icon) {
    throw new Error("No icon defined for tab " + tab.screen);
  }
}

function convertAnimationType(animationType) {
  return animationType !== 'none';
}

function navigatorSetButtons(navigator, navigatorEventID, params) {
  if (params.rightButtons) {
    params.rightButtons.forEach(function(button) {
      button.enabled = !button.disabled;
      if (button.icon) {
        const icon = resolveAssetSource(button.icon);
        if (icon) {
          button.icon = icon.uri;
        }
      }
    });
  }
  if (params.leftButton) {
    if (params.leftButton.icon) {
      const icon = resolveAssetSource(params.leftButton.icon);
      if (icon) {
        params.leftButton.icon = icon.uri;
      }
    }
  }
  newPlatformSpecific.setScreenTitleBarButtons(navigator.screenInstanceID, navigatorEventID, params.rightButtons, params.leftButton);
}

function navigatorSetTabBadge(navigator, params) {
  const badge = params.badge.toString();
  if (params.tabIndex >= 0) {
    newPlatformSpecific.setBottomTabBadgeByIndex(params.tabIndex, badge);
  } else {
    newPlatformSpecific.setBottomTabBadgeByNavigatorId(navigator.navigatorID, badge);
  }
}

function navigatorSetTitle(navigator, params) {
  newPlatformSpecific.setScreenTitleBarTitle(navigator.screenInstanceID, params.title);
}

function navigatorSwitchToTab(navigator, params) {
  if (params.tabIndex >= 0) {
    newPlatformSpecific.selectBottomTabByTabIndex(params.tabIndex);
  } else {
    newPlatformSpecific.selectBottomTabByNavigatorId(navigator.navigatorID);
  }
}

function navigatorToggleDrawer(navigator, params) {
  const animated = !(params.animated === false);
  if (params.to) {
    const visible = params.to === 'open';
    newPlatformSpecific.setSideMenuVisible(animated, visible);
  } else {
    newPlatformSpecific.toggleSideMenuVisible(animated);
  }
}

function navigatorToggleNavBar(navigator, params) {
  const screenInstanceID = navigator.screenInstanceID;
  const visible = params.to === 'shown';
  const animated = !(params.animated === false);

  newPlatformSpecific.toggleTopBarVisible(
    screenInstanceID,
    visible,
    animated
  );
}

function navigatorToggleTabs(navigator, params) {
  const visibility = params.to === 'hidden';
  const animated = !(params.animated === false);
  newPlatformSpecific.toggleBottomTabsVisible(visibility, animated);
}

function showModal(params) {
  addNavigatorParams(params);
  addNavigatorButtons(params);
  addTitleBarBackButtonIfNeeded(params);
  addNavigationStyleParams(params);

  /*
   * adapt to new API
   */
  adaptTopTabs(params, params.navigatorID);
  params.screenId = params.screen;
  let adapted = adaptNavigationStyleToScreenStyle(params);
  adapted = adaptNavigationParams(adapted);
  adapted.overrideBackPress = params.overrideBackPress;

  newPlatformSpecific.showModal(adapted);
}

function dismissModal() {
  newPlatformSpecific.dismissTopModal();
}

function dismissAllModals(params) {
  newPlatformSpecific.dismissAllModals();
}

function addNavigatorParams(screen, navigator = null, idx = '') {
  screen.navigatorID = navigator ? navigator.navigatorID : uniqueId('navigatorID') + '_nav' + idx;
  screen.screenInstanceID = uniqueId('screenInstanceID');
  screen.navigatorEventID = screen.screenInstanceID + '_events';
}

function addNavigatorButtons(screen, sideMenuParams) {
  const Screen = Navigation.getRegisteredScreen(screen.screen);
  Object.assign(screen, Screen.navigatorButtons);

  // Get image uri from image id
  const rightButtons = getRightButtons(screen);
  if (rightButtons) {
    rightButtons.forEach(function(button) {
      button.enabled = !button.disabled;
      if (button.icon) {
        const icon = resolveAssetSource(button.icon);
        if (icon) {
          button.icon = icon.uri;
        }
      }
    });
  }

  let leftButton = getLeftButton(screen);
  if (sideMenuParams && !leftButton) {
    leftButton = createSideMenuButton();
  }
  if (leftButton) {
    if (leftButton.icon) {
      const icon = resolveAssetSource(leftButton.icon);
      if (icon) {
        leftButton.icon = icon.uri;
      }
    }
  }

  if (rightButtons) {
    screen.rightButtons = rightButtons;
  }
  if (leftButton) {
    screen.leftButton = leftButton;
  }
}

function createSideMenuButton() {
  return {
    id: "sideMenu"
  };
}

function addTitleBarBackButtonIfNeeded(screen) {
  const leftButton = getLeftButton(screen);
  if (!leftButton) {
    screen.leftButton = {
      id: 'back'
    }
  }
}

function getLeftButton(screen) {
  if (screen.navigatorButtons && screen.navigatorButtons.leftButton) {
    return screen.navigatorButtons.leftButton;
  }

  return screen.leftButton;
}

function getRightButtons(screen) {
  if (screen.navigatorButtons && screen.navigatorButtons.rightButtons) {
    return screen.navigatorButtons.rightButtons;
  }

  return screen.rightButtons;
}

function addNavigationStyleParams(screen) {
  const Screen = Navigation.getRegisteredScreen(screen.screen);
  screen.navigatorStyle = Object.assign({}, screen.navigatorStyle, Screen.navigatorStyle);
}

export default {
  startTabBasedApp,
  startSingleScreenApp,
  navigatorPush,
  navigatorPop,
  navigatorPopToRoot,
  navigatorResetTo,
  showModal,
  dismissModal,
  dismissAllModals,
  navigatorSetButtons,
  navigatorSetTabBadge,
  navigatorSetTitle,
  navigatorSwitchToTab,
  navigatorToggleDrawer,
  navigatorToggleTabs,
  navigatorToggleNavBar
};
