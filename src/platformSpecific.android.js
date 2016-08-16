import React, {Component} from 'react';
import {AppRegistry} from 'react-native';

import Navigation from './Navigation';
import utils from './utils';

import {RctActivity} from 'react-native-navigation';

const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

function startSingleScreenApp(params) {
  let screen = params.screen;
  if (!screen.screen) {
    console.error('startSingleScreenApp(params): screen must include a screen property');
    return;
  }

  addNavigatorParams(screen);
  addNavigatorButtons(screen);
  addNavigationStyleParams(screen);
  screen.passProps = params.passProps;
  const drawer = setupDrawer(params.drawer);
  RctActivity.startSingleScreenApp(screen, drawer);
}

function startTabBasedApp(params) {
  if (!params.tabs) {
    console.error('startTabBasedApp(params): params.tabs is required');
    return;
  }

  params.tabs.forEach(function(tab, idx) {
    addNavigatorParams(tab, null, idx);
    addNavigatorButtons(tab);
    addNavigationStyleParams(tab);
    addTabIcon(tab);
    tab.passProps = params.passProps;
  });

  const drawer = setupDrawer(params.drawer);
  RctActivity.startTabBasedApp(params.tabs, params.tabsStyle, drawer);
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

function navigatorPush(navigator, params) {
  addNavigatorParams(params, navigator);
  addNavigatorButtons(params);
  addNavigationStyleParams(params);
  RctActivity.navigatorPush(params);
}

function navigatorSetButtons(navigator, navigatorEventID, params) {
  if (params.rightButtons) {
    params.rightButtons.forEach(function(button) {
      if (button.icon) {
        const icon = resolveAssetSource(button.icon);
        if (icon) {
          button.icon = icon.uri;
        }
      }
    });
  }
  RctActivity.setNavigatorButtons(params);
}

function navigatorPop(navigator, params) {
  addNavigatorParams(params, navigator);
  RctActivity.navigatorPop(params);
}

function navigatorPopToRoot(navigator, params) {
  RctActivity.navigatorPopToRoot({
    navigatorID: navigator.navigatorID,
    animated: !(params.animated === false)
  });
}

function navigatorResetTo(navigator, params) {
  addNavigatorParams(params, navigator);
  addNavigatorButtons(params);
  addNavigationStyleParams(params);
  RctActivity.navigatorResetTo(params);
}

function navigatorSetTabBadge(navigator, params) {
  RctActivity.setTabBadge({
    tabIndex: params.tabIndex,
    badge: params.badge
  });
}

function navigatorSetTitle(navigator, params) {
  RctActivity.setNavigatorTitle(params);
}

function navigatorSwitchToTab(navigator, params) {
  RctActivity.switchToTab({
    navigatorID: navigator.navigatorID,
    tabIndex: params.tabIndex
  });
}

function navigatorToggleDrawer(navigator, params) {
  RctActivity.toggleDrawer({
    side: params.side,
    animated: !(params.animated === false),
    to: params.to || ''
  });
}

function navigatorToggleNavBar(navigator, params) {
  RctActivity.toggleNavigationBar({
    hidden: params.to === 'hidden',
    animated: !(params.animated === false)
  });
}

function navigatorToggleTabs(navigator, params) {
  RctActivity.toggleNavigatorTabs({
    hidden: params.to === 'hidden',
    animated: !(params.animated === false)
  });
}

function showModal(params) {
  addNavigatorParams(params);
  addNavigatorButtons(params);
  addNavigationStyleParams(params);
  RctActivity.showModal(params);
}

function dismissModal() {
  RctActivity.dismissModal();
}

function dismissAllModals(params) {
  RctActivity.dismissAllModals(params.animationType);
}

function addNavigatorParams(screen, navigator = null, idx = '') {
  screen.navigatorID = navigator ? navigator.navigatorID : utils.getRandomId() + '_nav' + idx;
  screen.screenInstanceID = utils.getRandomId();
  screen.navigatorEventID = screen.screenInstanceID + '_events';
  Navigation.registerCallback(screen.screenInstanceID, screen.callback);
}

function addNavigatorButtons(screen) {
  const Screen = Navigation.getRegisteredScreen(screen.screen);
  Object.assign(screen, Screen.navigatorButtons);

  // Get image uri from image id
  const rightButtons = screen.rightButtons ? screen.rightButtons : screen.navigatorButtons ?
    screen.navigatorButtons.rightButtons : null;
  if (rightButtons) {
    rightButtons.forEach(function(button) {
      if (button.icon) {
        const icon = resolveAssetSource(button.icon);
        if (icon) {
          button.icon = icon.uri;
        }
      }
    });
  }
}

function addNavigationStyleParams(screen) {
  const Screen = Navigation.getRegisteredScreen(screen.screen);
  screen.navigatorStyle = Object.assign({}, screen.navigatorStyle, Screen.navigatorStyle);
}

function setupDrawer(drawerParams) {
  const drawer = Object.assign({}, drawerParams);
  [drawer.left, drawer.right].forEach(side => {
    if (!side) {
      return;
    }
    const icon = resolveAssetSource(side.icon);
    if (icon) {
      side.icon = icon.uri;
    }
  });
  if (drawer.disableOpenGesture === undefined) {
    drawer.disableOpenGesture = false;
  };

  return drawer;
}

function showFAB(params) {
  params.icon = resolveAssetSource(params.icon).uri;
  RctActivity.showFAB(params);
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
  showFAB,
  navigatorSetButtons,
  navigatorSetTabBadge,
  navigatorSetTitle,
  navigatorSwitchToTab,
  navigatorToggleDrawer,
  navigatorToggleTabs,
  navigatorToggleNavBar
}
