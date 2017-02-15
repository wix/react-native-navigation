/*eslint-disable*/
import React, {Component} from 'react';
import {AppRegistry, NativeModules, processColor} from 'react-native';
import _ from 'lodash';

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
  screen.topTabs = _.cloneDeep(screen.topTabs);
  _.forEach(_.get(screen, 'topTabs'), (tab) => {
    addNavigatorParams(tab);
    if (navigatorID) {
      tab.navigatorID = navigatorID;
    }
    tab.screen = tab.screenId;
    if (tab.icon) {
      addTabIcon(tab);
    }
    addNavigatorButtons(tab);
    adaptNavigationParams(tab);
    addNavigationStyleParams(tab);
    tab = adaptNavigationStyleToScreenStyle(tab);
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
  const navigatorStyle = screen.navigatorStyle;
  if (!navigatorStyle) {
    return screen;
  }

  screen.styleParams = convertStyleParams(navigatorStyle);

  return _.omit(screen, ['navigatorStyle']);
}

function convertStyleParams(originalStyleObject) {
  if (!originalStyleObject) {
    return null;
  }

  let ret = {
    statusBarColor: processColor(originalStyleObject.statusBarColor),
    topBarColor: processColor(originalStyleObject.navBarBackgroundColor),
    topBarTransparent: originalStyleObject.navBarTransparent,
    topBarTranslucent: originalStyleObject.navBarTranslucent,
    topBarElevationShadowEnabled: originalStyleObject.topBarElevationShadowEnabled,
    topBarCollapseOnScroll: originalStyleObject.topBarCollapseOnScroll,
    collapsingToolBarImage: originalStyleObject.collapsingToolBarImage,
    collapsingToolBarComponent: originalStyleObject.collapsingToolBarComponent,
    collapsingToolBarComponentHeight: originalStyleObject.collapsingToolBarComponentHeight,
    collapsingToolBarCollapsedColor: processColor(originalStyleObject.collapsingToolBarCollapsedColor),
    expendCollapsingToolBarOnTopTabChange: originalStyleObject.expendCollapsingToolBarOnTopTabChange,
    titleBarHidden: originalStyleObject.navBarHidden,
    titleBarHideOnScroll: originalStyleObject.navBarHideOnScroll,
    titleBarTitleColor: processColor(originalStyleObject.navBarTextColor),
    titleBarSubtitleColor: processColor(originalStyleObject.navBarTextSubtitleColor),
    titleBarButtonColor: processColor(originalStyleObject.navBarButtonColor),
    titleBarDisabledButtonColor: processColor(originalStyleObject.titleBarDisabledButtonColor),
    backButtonHidden: originalStyleObject.backButtonHidden,
    topTabsHidden: originalStyleObject.topTabsHidden,
    contextualMenuStatusBarColor: processColor(originalStyleObject.contextualMenuStatusBarColor),
    contextualMenuBackgroundColor: processColor(originalStyleObject.contextualMenuBackgroundColor),
    contextualMenuButtonsColor: processColor(originalStyleObject.contextualMenuButtonsColor),

    drawBelowTopBar: !originalStyleObject.drawUnderNavBar,

    topTabTextColor: processColor(originalStyleObject.topTabTextColor),
    topTabIconColor: processColor(originalStyleObject.topTabIconColor),
    selectedTopTabIconColor: processColor(originalStyleObject.selectedTopTabIconColor),
    selectedTopTabTextColor: processColor(originalStyleObject.selectedTopTabTextColor),
    selectedTopTabIndicatorHeight: originalStyleObject.selectedTopTabIndicatorHeight,
    selectedTopTabIndicatorColor: processColor(originalStyleObject.selectedTopTabIndicatorColor),
    topTabScrollable: originalStyleObject.topTabScollable,
    screenBackgroundColor: processColor(originalStyleObject.screenBackgroundColor),

    drawScreenAboveBottomTabs: !originalStyleObject.drawUnderTabBar,

    bottomTabsColor: processColor(originalStyleObject.tabBarBackgroundColor),
    bottomTabsButtonColor: processColor(originalStyleObject.tabBarButtonColor),
    bottomTabsSelectedButtonColor: processColor(originalStyleObject.tabBarSelectedButtonColor),
    bottomTabsHidden: originalStyleObject.tabBarHidden,
    bottomTabsHiddenOnScroll: originalStyleObject.bottomTabsHiddenOnScroll,
    forceTitlesDisplay: originalStyleObject.forceTitlesDisplay,
    bottomTabBadgeTextColor: processColor(originalStyleObject.bottomTabBadgeTextColor),
    bottomTabBadgeBackgroundColor: processColor(originalStyleObject.bottomTabBadgeBackgroundColor),

    navigationBarColor: processColor(originalStyleObject.navigationBarColor)
  }

  if (originalStyleObject.collapsingToolBarImage) {
    if (_.isString(originalStyleObject.collapsingToolBarImage)) {
      ret.collapsingToolBarImage = originalStyleObject.collapsingToolBarImage;
    }

    const collapsingToolBarImage = resolveAssetSource(originalStyleObject.collapsingToolBarImage)
    if (collapsingToolBarImage) {
      ret.collapsingToolBarImage = collapsingToolBarImage.uri;
    }
  }
  if (_.isUndefined(ret.expendCollapsingToolBarOnTopTabChange)) {
    ret.expendCollapsingToolBarOnTopTabChange = true;
  }
  return ret;
}

function convertDrawerParamsToSideMenuParams(drawerParams) {
  const drawer = Object.assign({}, drawerParams);

  let result = {
    left: {},
    right: {}
  };

  Object.keys(result).forEach((key) => {
    if (drawer[key] && drawer[key].screen) {
      result[key].screenId = drawer[key].screen;
      addNavigatorParams(result[key]);
      result[key] = adaptNavigationParams(result[key]);
      result[key].passProps = drawer[key].passProps;
      result[key].disableOpenGesture = drawer.disableOpenGesture;
    } else {
      result[key] = null;
    }
  })

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

  params.tabs = _.cloneDeep(params.tabs);

  params.tabs.forEach(function(tab, idx) {
    addNavigatorParams(tab, null, idx);
    addNavigatorButtons(tab, params.drawer);
    addNavigationStyleParams(tab);
    addTabIcon(tab);
    if (!tab.passProps) {
      tab.passProps = params.passProps;
    }

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

function navigatorSetButtons(navigator, navigatorEventID, _params) {
  const params = _.cloneDeep(_params);
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
  let leftButton = getLeftButton(params);
  if (leftButton) {
    if (leftButton.icon) {
      const icon = resolveAssetSource(leftButton.icon);
      if (icon) {
        leftButton.icon = icon.uri;
      }
    }
  } else if (shouldRemoveLeftButton(params)) {
    leftButton = {};
  }
  const fab = getFab(params);
  newPlatformSpecific.setScreenButtons(navigator.screenInstanceID, navigatorEventID, params.rightButtons, leftButton, fab);
}

function shouldRemoveLeftButton(params) {
  return params.leftButtons && params.leftButtons.length === 0;
}

function navigatorSetTabBadge(navigator, params) {
  const badge = params.badge ? params.badge.toString() : '';
  if (params.tabIndex >= 0) {
    newPlatformSpecific.setBottomTabBadgeByIndex(params.tabIndex, badge);
  } else {
    newPlatformSpecific.setBottomTabBadgeByNavigatorId(navigator.navigatorID, badge);
  }
}

function navigatorSetTitle(navigator, params) {
  newPlatformSpecific.setScreenTitleBarTitle(navigator.screenInstanceID, params.title);
}

function navigatorSetSubtitle(navigator, params) {
  newPlatformSpecific.setScreenTitleBarSubtitle(navigator.screenInstanceID, params.subtitle);
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
    newPlatformSpecific.setSideMenuVisible(animated, visible, params.side);
  } else {
    newPlatformSpecific.toggleSideMenuVisible(animated, params.side);
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

function showInAppNotification(params) {
  params.navigationParams = {};
  addNavigatorParams(params.navigationParams);

  params.autoDismissTimerSec = params.autoDismissTimerSec || 5;
  if (params.autoDismiss === false) delete params.autoDismissTimerSec;

  newPlatformSpecific.showInAppNotification(params);
}

function dismissInAppNotification(params) {
  newPlatformSpecific.dismissInAppNotification(params);
}

function addNavigatorParams(screen, navigator = null, idx = '') {
  screen.navigatorID = navigator ? navigator.navigatorID : _.uniqueId('navigatorID') + '_nav' + idx;
  screen.screenInstanceID = _.uniqueId('screenInstanceID');
  screen.navigatorEventID = screen.screenInstanceID + '_events';
}

function addNavigatorButtons(screen, sideMenuParams) {

  const Screen = Navigation.getRegisteredScreen(screen.screen);
  if (screen.navigatorButtons == null) {
    screen.navigatorButtons = _.cloneDeep(Screen.navigatorButtons);
  }

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
  if (leftButton) {
    if (leftButton.icon) {
      const icon = resolveAssetSource(leftButton.icon);
      if (icon) {
        leftButton.icon = icon.uri;
      }
    }
  }

  const fab = getFab(screen);
  if (fab) {
    screen.fab = fab;
  }

  if (rightButtons) {
    screen.rightButtons = rightButtons;
  }
  if (leftButton) {
    screen.leftButton = leftButton;
  }
}

function getFab(screen) {
  let fab = screen.fab;
  if (screen.navigatorButtons && screen.navigatorButtons.fab) {
    fab = screen.navigatorButtons.fab;
  }
  if (fab === null || fab === undefined) {
    return;
  }
  if (Object.keys(fab).length === 0 ) {
    return {};
  }

  const collapsedIconUri = resolveAssetSource(fab.collapsedIcon);
  if (!collapsedIconUri) {
    return;
  }
  fab.collapsedIcon = collapsedIconUri.uri;
  if (fab.expendedIcon) {
    const expendedIconUri = resolveAssetSource(fab.expendedIcon);
    if (expendedIconUri) {
      fab.expendedIcon = expendedIconUri.uri;
    }
  }
  if (fab.backgroundColor) {
    fab.backgroundColor = processColor(fab.backgroundColor);
  }

  if (fab.actions) {
    _.forEach(fab.actions, (action) => {
      action.icon = resolveAssetSource(action.icon).uri;
      return action;
    });
  }

  return fab;
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
  const leftButton = getLeftButtonDeprecated(screen);
  if (leftButton) {
    return leftButton;
  }

  if (screen.navigatorButtons && screen.navigatorButtons.leftButtons) {
    return screen.navigatorButtons.leftButtons[0];
  }

  if (screen.leftButtons) {
    if (_.isArray(screen.leftButtons)) {
      return screen.leftButtons[0];
    } else {
      return screen.leftButtons;
    }
  }

  return null;
}

function getLeftButtonDeprecated(screen) {
  if (screen.navigatorButtons && screen.navigatorButtons.leftButton) {
    return screen.navigatorButtons.leftButton;
  }

  return screen.leftButton;
}

function getRightButtons(screen) {
  if (screen.navigatorButtons && screen.navigatorButtons.rightButtons) {
    return screen.navigatorButtons.rightButtons;
  } else if (screen.rightButtons) {
    return screen.rightButtons
  }

  const Screen = Navigation.getRegisteredScreen(screen.screen);

  if (Screen.navigatorButtons && !_.isEmpty(Screen.navigatorButtons.rightButtons)) {
    return _.cloneDeep(Screen.navigatorButtons.rightButtons);
  }

  return null;
}

function addNavigationStyleParams(screen) {
  const Screen = Navigation.getRegisteredScreen(screen.screen);
  screen.navigatorStyle = Object.assign({}, Screen.navigatorStyle, screen.navigatorStyle);
}

function showSnackbar(navigator, params) {
  const adapted = _.cloneDeep(params);
  if (adapted.backgroundColor) {
    adapted.backgroundColor = processColor(adapted.backgroundColor);
  }
  if (adapted.actionColor) {
    adapted.actionColor = processColor(adapted.actionColor);
  }
  if (adapted.textColor) {
    adapted.textColor = processColor(adapted.textColor);
  }
  return newPlatformSpecific.showSnackbar(adapted);
}

function dismissSnackbar() {
  return newPlatformSpecific.dismissSnackbar();
}

function showContextualMenu(navigator, params) {
  const contextualMenu = {
    buttons: [],
    backButton: {id: 'back'},
    navigationParams: {navigatorEventID: navigator.navigatorEventID}
  };

  params.rightButtons.forEach((button, index) => {
    const btn = {
      icon: resolveAssetSource(button.icon),
      showAsAction: button.showAsAction,
      color: processColor(button.color),
      label: button.title,
      index
    };
    if (btn.icon) {
      btn.icon = btn.icon.uri;
    }
    contextualMenu.buttons.push(btn);
  });

  newPlatformSpecific.showContextualMenu(navigator.screenInstanceID, contextualMenu, params.onButtonPressed);
}

function dismissContextualMenu() {
  newPlatformSpecific.dismissContextualMenu();
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
  showInAppNotification,
  dismissInAppNotification,
  navigatorSetButtons,
  navigatorSetTabBadge,
  navigatorSetTitle,
  navigatorSetSubtitle,
  navigatorSwitchToTab,
  navigatorToggleDrawer,
  navigatorToggleTabs,
  navigatorToggleNavBar,
  showSnackbar,
  dismissSnackbar,
  showContextualMenu,
  dismissContextualMenu
};
