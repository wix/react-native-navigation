/*eslint-disable*/
import React from 'react';
import {AppRegistry} from 'react-native';
import platformSpecific from './deprecated/platformSpecificDeprecated';
import {Screen} from './Screen';

import PropRegistry from './PropRegistry';

const registeredScreens = {};
const _allNavigatorEventHandlers = {};

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

      constructor(props) {
        super(props);
        this.state = {
          internalProps: {...props, ...PropRegistry.load(props.screenInstanceID || props.passPropsKey)}
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          internalProps: {...PropRegistry.load(this.props.screenInstanceID || this.props.passPropsKey), ...nextProps}
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

      constructor(props) {
        super(props);
        this.state = {
          internalProps: {...props, ...PropRegistry.load(props.screenInstanceID || props.passPropsKey)}
        }
      }

      componentWillReceiveProps(nextProps) {
        this.setState({
          internalProps: {...PropRegistry.load(this.props.screenInstanceID || this.props.passPropsKey), ...nextProps}
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

function dismissLightBox() {
  return platformSpecific.dismissLightBox();
}

function showInAppNotification(params = {}) {
  return platformSpecific.showInAppNotification(params);
}

function dismissInAppNotification(params = {}) {
  return platformSpecific.dismissInAppNotification(params);
}

async function startTabBasedApp(params) {
  try {
    return await platformSpecific.startTabBasedApp(params);
  } catch(e) {
    console.error(`Error while starting app: ${e}`);
  }
}

async function startSingleScreenApp(params) {
  try {
    return await platformSpecific.startSingleScreenApp(params);
  } catch(e) {
    console.error(`Error while starting app: ${e}`);
  }
}

function setEventHandler(navigatorEventID, eventHandler) {
  _allNavigatorEventHandlers[navigatorEventID] = eventHandler;
}

function clearEventHandler(navigatorEventID) {
  delete _allNavigatorEventHandlers[navigatorEventID];
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

async function isAppLaunched() {
  return await platformSpecific.isAppLaunched();
}

async function isRootLaunched() {
  return await platformSpecific.isRootLaunched();
}

function getCurrentlyVisibleScreenId() {
  return platformSpecific.getCurrentlyVisibleScreenId();
}

async function getLaunchArgs() {
  return await platformSpecific.getLaunchArgs();
}

export default {
  getRegisteredScreen,
  getCurrentlyVisibleScreenId,
  registerComponent,
  showModal: showModal,
  dismissModal: dismissModal,
  dismissAllModals: dismissAllModals,
  showSnackbar: showSnackbar,
  showLightBox: showLightBox,
  dismissLightBox: dismissLightBox,
  showInAppNotification: showInAppNotification,
  dismissInAppNotification: dismissInAppNotification,
  startTabBasedApp: startTabBasedApp,
  startSingleScreenApp: startSingleScreenApp,
  setEventHandler: setEventHandler,
  clearEventHandler: clearEventHandler,
  handleDeepLink: handleDeepLink,
  isAppLaunched: isAppLaunched,
  isRootLaunched: isRootLaunched,
  getLaunchArgs
};
