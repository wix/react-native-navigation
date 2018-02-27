import React, {Component} from 'react';
import {AppRegistry, NativeModules} from 'react-native';
import _ from 'lodash';
import PropRegistry from './PropRegistry';

const NativeReactModule = NativeModules.NavigationReactModule;

async function startApp(activityParams) {
  savePassProps(activityParams);
  return await NativeReactModule.startApp(activityParams);
}

function push(screenParams) {
  savePassProps(screenParams);
  return NativeReactModule.push(screenParams);
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

function setScreenButtons(screenInstanceID, navigatorEventID, rightButtons, leftButton, fab) {
  NativeReactModule.setScreenButtons(screenInstanceID, navigatorEventID, rightButtons, leftButton, fab);
}

function showModal(screenParams) {
  savePassProps(screenParams);
  NativeReactModule.showModal(screenParams);
}

function showLightBox(params) {
  savePassProps(params);
  NativeReactModule.showLightBox(params);
}

function dismissLightBox() {
  NativeReactModule.dismissLightBox();
}

function dismissTopModal(params) {
  NativeReactModule.dismissTopModal(params);
}

function dismissAllModals() {
  NativeReactModule.dismissAllModals();
}

function showInAppNotification(params) {
  savePassProps(params);
  NativeReactModule.showSlidingOverlay(params);
}

function dismissInAppNotification(params) {
  NativeReactModule.hideSlidingOverlay(params);
}

// eslint-disable-next-line max-statements
function savePassProps(params) {
  if (params.navigationParams && params.passProps) {
    PropRegistry.save(params.navigationParams.screenInstanceID, params.passProps);
  }

  if (params.screen && params.screen.passProps) {
    PropRegistry.save(params.screen.navigationParams.screenInstanceID, params.screen.passProps);
  }

  if (_.get(params, 'screen.screens')) {
    _.forEach(params.screen.screens, savePassProps);
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
      
      if (tab.screens) {
        _.forEach(tab.screens, savePassProps);
      }
    });
  }

  if (params.sideMenu && params.sideMenu.left) {
    PropRegistry.save(params.sideMenu.left.navigationParams.screenInstanceID, params.sideMenu.left.passProps);
  }
  if (params.sideMenu && params.sideMenu.right) {
    PropRegistry.save(params.sideMenu.right.navigationParams.screenInstanceID, params.sideMenu.right.passProps);
  }
}

function toggleSideMenuVisible(animated, side) {
  NativeReactModule.toggleSideMenuVisible(animated, side);
}

function setSideMenuVisible(animated, visible, side) {
  NativeReactModule.setSideMenuVisible(animated, visible, side);
}

function setSideMenuEnabled(enabled, side) {
  NativeReactModule.setSideMenuEnabled(enabled, side);
}

function selectTopTabByTabIndex(screenInstanceId, index) {
  NativeReactModule.selectTopTabByTabIndex(screenInstanceId, index);
}

function selectTopTabByScreen(screenInstanceId) {
  NativeReactModule.selectTopTabByScreen(screenInstanceId);
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

function setBottomTabButtonByIndex(index, params) {
  NativeReactModule.setBottomTabButtonByIndex(index, params);
}

function setBottomTabButtonByNavigatorId(navigatorId, params) {
  NativeReactModule.setBottomTabButtonByNavigatorId(navigatorId, params);
}

function showSnackbar(params) {
  NativeReactModule.showSnackbar(params);
}

function dismissSnackbar() {
  NativeReactModule.dismissSnackbar();
}

function showContextualMenu(screenInstanceID, params, onButtonPressed) {
  NativeReactModule.showContextualMenu(screenInstanceID, params, onButtonPressed);
}

function dismissContextualMenu(screenInstanceID) {
  NativeReactModule.dismissContextualMenu(screenInstanceID);
}

function setScreenStyle(screenInstanceId, style) {
  NativeReactModule.setScreenStyle(screenInstanceId, style);
}

async function isAppLaunched() {
  return await NativeReactModule.isAppLaunched();
}

async function isRootLaunched() {
  return await NativeReactModule.isRootLaunched();
}

async function getCurrentlyVisibleScreenId() {
  return await NativeReactModule.getCurrentlyVisibleScreenId();
}

async function getLaunchArgs() {
  return await NativeReactModule.getLaunchArgs();
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
  setScreenButtons,
  showModal,
  dismissTopModal,
  dismissAllModals,
  showLightBox,
  dismissLightBox,
  selectTopTabByScreen,
  selectTopTabByTabIndex,
  showInAppNotification,
  dismissInAppNotification,
  toggleSideMenuVisible,
  setSideMenuVisible,
  setSideMenuEnabled,
  selectBottomTabByNavigatorId,
  selectBottomTabByTabIndex,
  setBottomTabBadgeByNavigatorId,
  setBottomTabBadgeByIndex,
  setBottomTabButtonByNavigatorId,
  setBottomTabButtonByIndex,
  showSnackbar,
  dismissSnackbar,
  showContextualMenu,
  dismissContextualMenu,
  setScreenStyle,
  isAppLaunched,
  isRootLaunched,
  getCurrentlyVisibleScreenId,
  getLaunchArgs
};
