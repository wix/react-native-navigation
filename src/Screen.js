import React, {Component} from 'react';
import {
  NativeAppEventEmitter,
  DeviceEventEmitter,
  Platform
} from 'react-native';
import platformSpecific from './platformSpecific';
import Navigation from './Navigation';

const _allNavigatorEventHandlers = {};

const _callbackRegistry = {};

class Navigator {
  constructor(navigatorID, navigatorEventID, getScreenInstanceID) {
    this.navigatorID = navigatorID;
    this.navigatorEventID = navigatorEventID;
    this.navigatorEventHandler = null;
    this.navigatorEventSubscription = null;
    this.getScreenInstanceID = getScreenInstanceID;
  }

  push(params = {}) {
    params.registerCallbackID = function(screenInstanceID) {
      _callbackRegistry[screenInstanceID] = params.callback;
    };
    return platformSpecific.navigatorPush(this, params);
  }

  pop(params = {}) {
    return platformSpecific.navigatorPop(this, params);
  }

  popToRoot(params = {}) {
    return platformSpecific.navigatorPopToRoot(this, params);
  }

  resetTo(params = {}) {
    return platformSpecific.navigatorResetTo(this, params);
  }

  showModal(params = {}) {
    params.registerCallbackID = function(screenInstanceID) {
      _callbackRegistry[screenInstanceID] = params.callback;
    };
    return Navigation.showModal(params);
  }

  dismissModal(params = {}) {
    return Navigation.dismissModal(params);
  }

  dismissAllModals(params = {}) {
    return Navigation.dismissAllModals(params);
  }

  showLightBox(params = {}) {
    return Navigation.showLightBox(params);
  }

  dismissLightBox(params = {}) {
    return Navigation.dismissLightBox(params);
  }

  showInAppNotification(params = {}) {
    return Navigation.showInAppNotification(params);
  }

  dismissInAppNotification(params = {}) {
    return Navigation.dismissInAppNotification(params);
  }

  setButtons(params = {}) {
    return platformSpecific.navigatorSetButtons(this, this.navigatorEventID, params);
  }

  setTitle(params = {}) {
    return platformSpecific.navigatorSetTitle(this, params);
  }

  setTitleImage(params = {}) {
    return platformSpecific.navigatorSetTitleImage(this, params);
  }

  toggleDrawer(params = {}) {
    return platformSpecific.navigatorToggleDrawer(this, params);
  }

  toggleTabs(params = {}) {
    return platformSpecific.navigatorToggleTabs(this, params);
  }

  toggleNavBar(params = {}) {
    return platformSpecific.navigatorToggleNavBar(this, params);
  }

  setTabBadge(params = {}) {
    return platformSpecific.navigatorSetTabBadge(this, params);
  }

  switchToTab(params = {}) {
    return platformSpecific.navigatorSwitchToTab(this, params);
  }

  showFAB(params = {}) {
    return platformSpecific.showFAB(params);
  }

  setOnNavigatorEvent(callback) {
    this.navigatorEventHandler = callback;
    if (!this.navigatorEventSubscription) {
      let Emitter = Platform.OS === 'android' ? DeviceEventEmitter : NativeAppEventEmitter;
      this.navigatorEventSubscription = Emitter.addListener(this.navigatorEventID, (event) => this.onNavigatorEvent(event));
      _allNavigatorEventHandlers[this.navigatorEventID] = (event) => this.onNavigatorEvent(event);
    }
  }

  handleDeepLink(params = {}) {
    if (!params.link) return;
    const event = {
      type: 'DeepLink',
      link: params.link
    };
    for (let i in _allNavigatorEventHandlers) {
      _allNavigatorEventHandlers[i](event);
    }
  }

  onNavigatorEvent(event) {
    if (this.navigatorEventHandler) {
      this.navigatorEventHandler(event);
    }
  }

  cleanup() {
    if (this.navigatorEventSubscription) {
      this.navigatorEventSubscription.remove();
      delete _allNavigatorEventHandlers[this.navigatorEventID];
    }
  }

  callback(...values) {
    _callbackRegistry[this.getScreenInstanceID()](...values);
  }
}

export default class Screen extends Component {
  static navigatorStyle = {};
  static navigatorButtons = {};

  constructor(props) {
    super(props);
    if (props.navigatorID) {
      this.navigator = new Navigator(props.navigatorID, props.navigatorEventID, () => this.props.screenInstanceID);
    }
  }

  componentWillUnmount() {
    if (this.navigator) {
      this.navigator.cleanup();
      this.navigator = undefined;
    }
  }
}
