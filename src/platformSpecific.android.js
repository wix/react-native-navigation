import React, {Component} from 'react';
import {AppRegistry, NativeModules} from 'react-native';
import _ from 'lodash';
import PropRegistry from './PropRegistry';

const NativeReactModule = NativeModules.NavigationReactModule;

function startApp(activityParams) {
  savePassProps(activityParams);
  NativeReactModule.startApp(activityParams);
}

function push(screenParams) {
  savePassProps(screenParams);
  NativeReactModule.push(screenParams);
}

function pop(screenParams) {
  NativeReactModule.pop(screenParams);
}

function popToRoot(screenParams) {
  NativeReactModule.popToRoot(screenParams);
}

function newStack(screenParams) {
  savePassProps(screenParams);
  NativeReactModule.newStack(screenParams);
}

function toggleTopBarVisible(screenInstanceID, visible, animated) {
  NativeReactModule.setTopBarVisible(screenInstanceID, visible, animated);
}

function toggleBottomTabsVisible(visible, animated) {
  NativeReactModule.setBottomTabsVisible(visible, animated);
}

function setScreenTitleBarTitle(screenInstanceID, title) {
  NativeReactModule.setScreenTitleBarTitle(screenInstanceID, title);
}

function setScreenTitleBarSubtitle(screenInstanceID, subtitle) {
  NativeReactModule.setScreenTitleBarSubtitle(screenInstanceID, subtitle);
}

function setScreenTitleBarButtons(screenInstanceID, navigatorEventID, rightButtons, leftButton) {
  NativeReactModule.setScreenTitleBarButtons(screenInstanceID, navigatorEventID, rightButtons, leftButton);
}

function showModal(screenParams) {
  savePassProps(screenParams);
  NativeReactModule.showModal(screenParams);
}

function dismissTopModal() {
  NativeReactModule.dismissTopModal();
}

function dismissAllModals() {
  NativeReactModule.dismissAllModals();
}

function savePassProps(params) {
  if (params.navigationParams && params.passProps) {
    PropRegistry.save(params.navigationParams.screenInstanceID, params.passProps);
  }

  if (params.screen && params.screen.passProps) {
    PropRegistry.save(params.screen.navigationParams.screenInstanceID, params.screen.passProps);
  }

  if (_.get(params, 'screen.topTabs')) {
    _.forEach(params.screen.topTabs, (tab) => savePassProps(tab));
  }

  if (params.topTabs) {
    _.forEach(params.topTabs, (tab) => savePassProps(tab));
  }

  if (params.tabs) {
    _.forEach(params.tabs, (tab) => {
      if (!tab.passProps) {
        tab.passProps = params.passProps;
      }
      savePassProps(tab);
    });
  }

  if (params.sideMenu) {
    PropRegistry.save(params.sideMenu.navigationParams.screenInstanceID, params.sideMenu.passProps);
  }
}

function toggleSideMenuVisible(animated) {
  NativeReactModule.toggleSideMenuVisible(animated);
}

function setSideMenuVisible(animated, visible) {
  NativeReactModule.setSideMenuVisible(animated, visible);
}

function selectBottomTabByNavigatorId(navigatorId) {
  NativeReactModule.selectBottomTabByNavigatorId(navigatorId);
}

function selectBottomTabByTabIndex(index) {
  NativeReactModule.selectBottomTabByTabIndex(index);
}

function setBottomTabBadgeByIndex(index, badge) {
  NativeReactModule.setBottomTabBadgeByIndex(index, badge);
}

function setBottomTabBadgeByNavigatorId(navigatorId, badge) {
  NativeReactModule.setBottomTabBadgeByNavigatorId(navigatorId, badge);
}

function showSnackbar(params) {
  NativeReactModule.showSnackbar(params);
}

function showContextualMenu(screenInstanceID, params, onButtonPressed) {
  NativeReactModule.showContextualMenu(screenInstanceID, params, onButtonPressed);
}

function dismissContextualMenu(screenInstanceID) {
  NativeReactModule.dismissContextualMenu(screenInstanceID);
}

module.exports = {
  startApp,
  push,
  pop,
  popToRoot,
  newStack,
  toggleTopBarVisible,
  toggleBottomTabsVisible,
  setScreenTitleBarTitle,
  setScreenTitleBarSubtitle,
  setScreenTitleBarButtons,
  showModal,
  dismissTopModal,
  dismissAllModals,
  toggleSideMenuVisible,
  setSideMenuVisible,
  selectBottomTabByNavigatorId,
  selectBottomTabByTabIndex,
  setBottomTabBadgeByNavigatorId,
  setBottomTabBadgeByIndex,
  showSnackbar,
  showContextualMenu,
  dismissContextualMenu
};
