import React from 'react';
import { AppRegistry } from 'react-native';
import platformSpecific from './platformSpecific';
import Screen from './Screen';

const registeredScreens = {};

const registeredCallbacks = {};

function registerScreen(screenID, generator) {
  registeredScreens[screenID] = generator;
  AppRegistry.registerComponent(screenID, generator);
}

function registerComponent(screenID, generator, store = undefined, Provider = undefined) {
  if (store && Provider) {
    return _registerComponentRedux(screenID, generator, store, Provider);
  } else {
    return _registerComponentNoRedux(screenID, generator);
  }
}

function _registerComponentNoRedux(screenID, generator) {
  const generatorWrapper = function() {
    const InternalComponent = generator();
    return class extends Screen {
      static navigatorStyle = InternalComponent.navigatorStyle || {};
      static navigatorButtons = InternalComponent.navigatorButtons || {};
      render() {
        return (
          <InternalComponent navigator={this.navigator} {...this.props} />
        );
      }
    };
  };
  registerScreen(screenID, generatorWrapper);
  return generatorWrapper;
}

function _registerComponentRedux(screenID, generator, store, Provider) {
  const generatorWrapper = function() {
    const InternalComponent = generator();
    return class extends Screen {
      static navigatorStyle = InternalComponent.navigatorStyle || {};
      static navigatorButtons = InternalComponent.navigatorButtons || {};
      render() {
        return (
          <Provider store={store}>
            <InternalComponent navigator={this.navigator} {...this.props} />
          </Provider>
        );
      }
    };
  };
  registerScreen(screenID, generatorWrapper);
  return generatorWrapper;
}

function getRegisteredScreen(screenID) {
  const generator = registeredScreens[screenID];
  if (!generator) {
    console.error('Navigation.getRegisteredScreen: ' + screenID + ' used but not yet registered');
    return;
  }
  return generator();
}

function registerCallback(screenInstanceID, callback) {
  registeredCallbacks[screenInstanceID] = callback;
}

function getRegisteredCallback(screenInstanceID) {
  return registeredCallbacks[screenInstanceID];
}

function showModal(params = {}) {
  return platformSpecific.showModal(params);
}

function dismissModal(params = {}) {
  return platformSpecific.dismissModal(params);
}

function dismissAllModals(params = {}) {
  return platformSpecific.dismissAllModals(params);
}

function showLightBox(params = {}) {
  return platformSpecific.showLightBox(params);
}

function dismissLightBox(params = {}) {
  return platformSpecific.dismissLightBox(params);
}

function showInAppNotification(params = {}) {
  return platformSpecific.showInAppNotification(params);
}

function dismissInAppNotification(params = {}) {
  return platformSpecific.dismissInAppNotification(params);
}

export default {
  registerScreen,
  getRegisteredScreen,
  registerComponent,
  registerCallback,
  getRegisteredCallback,
  showModal,
  dismissModal,
  dismissAllModals,
  showLightBox,
  dismissLightBox,
  showInAppNotification,
  dismissInAppNotification,
  startTabBasedApp: platformSpecific.startTabBasedApp,
  startSingleScreenApp: platformSpecific.startSingleScreenApp
}
