/*eslint-disable*/
import React from 'react';
import {AppRegistry, NativeAppEventEmitter, DeviceEventEmitter, Platform} from 'react-native';
import platformSpecific from './deprecated/platformSpecificDeprecated';
import Screen from './Screen';

import PropRegistry from './PropRegistry';

const registeredScreens = {};
const _allNavigatorEventHandlers = {};
const Emitter = Platform.OS === 'android' ? DeviceEventEmitter : NativeAppEventEmitter;

Emitter.addListener('bottomTabSelected', nativeEvent => {
  const event = {
    ...nativeEvent,
    type: 'TabSelected',
  };
  for (let i in _allNavigatorEventHandlers) {
    _allNavigatorEventHandlers[i](event);
  }
});

function registerScreen(screenID, generator) {
  registeredScreens[screenID] = generator;
  AppRegistry.registerComponent(screenID, generator);
}

function registerComponent(screenID, generator, store = undefined, Provider = undefined, options = {}) {
  if (store && Provider) {
    return _registerComponentRedux(screenID, generator, store, Provider, options);
  } else {
    return _registerComponentNoRedux(screenID, generator);
  }
}

function _registerComponentNoRedux(screenID, generator) {
  const generatorWrapper = function() {
    const InternalComponent = generator();
    if (!InternalComponent) {
      console.error(`Navigation: ${screenID} registration result is 'undefined'`);
    }
    
    return class extends Screen {
      static navigatorStyle = InternalComponent.navigatorStyle || {};
      static navigatorButtons = InternalComponent.navigatorButtons || {};
      static navigatorOptions = InternalComponent.navigatorOptions || {};

      constructor(props) {
        super(props);
        this.state = {
          internalProps: {...props, ...PropRegistry.load(props.screenInstanceID)}
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          internalProps: {...PropRegistry.load(this.props.screenInstanceID), ...nextProps}
        })
      }

      render() {
        return (
          <InternalComponent testID={screenID} navigator={this.navigator} {...this.state.internalProps} />
        );
      }
    };
  };
  registerScreen(screenID, generatorWrapper);
  return generatorWrapper;
}

function _registerComponentRedux(screenID, generator, store, Provider, options) {
  const generatorWrapper = function() {
    const InternalComponent = generator();
    return class extends Screen {
      static navigatorStyle = InternalComponent.navigatorStyle || {};
      static navigatorButtons = InternalComponent.navigatorButtons || {};
      static navigatorOptions = InternalComponent.navigatorOptions || {};

      constructor(props) {
        super(props);
        this.state = {
          internalProps: {...props, ...PropRegistry.load(props.screenInstanceID)}
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          internalProps: {...PropRegistry.load(this.props.screenInstanceID), ...nextProps}
        })
      }

      render() {
        return (
          <Provider store={store} {...options}>
            <InternalComponent testID={screenID} navigator={this.navigator} {...this.state.internalProps} />
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
    console.error(`Navigation.getRegisteredScreen: ${screenID} used but not yet registered`);
    return undefined;
  }
  return generator();
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

function showSnackbar(params = {}) {
  return platformSpecific.showSnackbar(params);
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

function startTabBasedApp(params) {
  return platformSpecific.startTabBasedApp(params);
}

function startSingleScreenApp(params) {
  return platformSpecific.startSingleScreenApp(params);
}

function updateRootScreen(params) {
  return platformSpecific.updateRootScreen(params);
}

function updateDrawerToScreen(params) {
  return platformSpecific.updateDrawerToScreen(params);
}

function updateDrawerToTab(params) {
  return platformSpecific.updateDrawerToTab(params);
}

function addSplashScreen() {
  return platformSpecific.addSplashScreen();
}

function removeSplashScreen() {
  return platformSpecific.removeSplashScreen();
}

function setEventHandler(uniqueID, eventHandler) {
  _allNavigatorEventHandlers[uniqueID] = eventHandler;
}

function clearEventHandler(uniqueID) {
  delete _allNavigatorEventHandlers[uniqueID];
}

function handleDeepLink(params = {}) {
  const { link, payload } = params;

  if (!link) return;

  const event = {
    type: 'DeepLink',
    link,
    ...(payload ? { payload } : {})
  };
  for (let i in _allNavigatorEventHandlers) {
    _allNavigatorEventHandlers[i](event);
  }
}

export default {
  getRegisteredScreen,
  registerComponent,
  showModal,
  dismissModal,
  dismissAllModals,
  showSnackbar,
  showLightBox,
  dismissLightBox,
  showInAppNotification,
  dismissInAppNotification,
  startTabBasedApp,
  startSingleScreenApp,
  updateRootScreen,
  updateDrawerToScreen,
  updateDrawerToTab,
  addSplashScreen,
  removeSplashScreen,
  setEventHandler,
  clearEventHandler,
  handleDeepLink
};
