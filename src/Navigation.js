import React, { AppRegistry } from 'react-native';
import platformSpecific from './platformSpecific';
import Screen from './Screen';

const registeredScreens = {};
const _routeResolver = null;

function resolveRoute(params) {
  if (!_routeResolver) return params;
  return _routeResolver(params);
}

function registerScreen(screenID, generator) {
  registeredScreens[screenID] = generator;
  AppRegistry.registerComponent(screenID, generator);
}

function registerComponent(screenID, generator, store = undefined, Provider = undefined) {
  if (store && Provider) return _registerComponentRedux(screenID, generator, store, Provider);
  else return _registerComponentNoRedux(screenID, generator);
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
  }
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
  }
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

function showModal(params = {}) {
  return platformSpecific.showModal(params);
}

function dismissModal(params = {}) {
  return platformSpecific.dismissModal(params);
}

function startTabBasedApp(params) {
  if (params.routeResolver) _routeResolver = params.routeResolver;
  return platformSpecific.startTabBasedApp(params);
}

function startSingleScreenApp(params) {
  if (params.routeResolver) _routeResolver = params.routeResolver;
  return platformSpecific.startSingleScreenApp(params);
}

export default {
  resolveRoute,
  registerScreen,
  getRegisteredScreen,
  registerComponent,
  showModal,
  dismissModal,
  startTabBasedApp,
  startSingleScreenApp
}
