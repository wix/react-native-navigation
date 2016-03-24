import { Component, NativeAppEventEmitter } from 'react-native';
import platformSpecific from './platformSpecific';
import Navigation from './Navigation';

const _allNavigatorEventHandlers = {};

class Navigator {
  constructor(navigatorID, navigatorEventID) {
    this.navigatorID = navigatorID;
    this.navigatorEventID = navigatorEventID;
    this.navigatorEventHandler = null;
    this.navigatorEventSubscription = null;
  }
  push(params = {}) {
    return platformSpecific.navigatorPush(this, Navigation.resolveRoute(params));
  }
  pop(params = {}) {
    return platformSpecific.navigatorPop(this, Navigation.resolveRoute(params));
  }
  popToRoot(params = {}) {
    return platformSpecific.navigatorPopToRoot(this, Navigation.resolveRoute(params));
  }
  resetTo(params = {}) {
    return platformSpecific.navigatorResetTo(this, Navigation.resolveRoute(params));
  }
  showModal(params = {}) {
    return Navigation.showModal(Navigation.resolveRoute(params));
  }
  dismissModal(params = {}) {
    return Navigation.dismissModal(Navigation.resolveRoute(params));
  }
  setButtons(params = {}) {
    return platformSpecific.navigatorSetButtons(this, this.navigatorEventID, Navigation.resolveRoute(params));
  }
  setTitle(params = {}) {
    return platformSpecific.navigatorSetTitle(this, Navigation.resolveRoute(params));
  }
  toggleDrawer(params = {}) {
    return platformSpecific.navigatorToggleDrawer(this, Navigation.resolveRoute(params));
  }
  setOnNavigatorEvent(callback) {
    this.navigatorEventHandler = callback;
    if (!this.navigatorEventSubscription) {
      this.navigatorEventSubscription = NativeAppEventEmitter.addListener(this.navigatorEventID, (event) => this.onNavigatorEvent(event));
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
}

export default class Screen extends Component {
  static navigatorStyle = {};
  static navigatorButtons = {};
  constructor(props) {
    super(props);
    if (props.navigatorID) {
      this.navigator = new Navigator(props.navigatorID, props.navigatorEventID);
    }
  }
  componentWillUnmount() {
    if (this.navigator) {
      this.navigator.cleanup();
      this.navigator = undefined;
    }
  }
}
