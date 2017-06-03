import NativeCommandsSender from './adapters/NativeCommandsSender';
import NativeEventsReceiver from './adapters/NativeEventsReceiver';
import UniqueIdProvider from './adapters/UniqueIdProvider';

import Store from './containers/Store';
import ContainerRegistry from './containers/ContainerRegistry';
import Commands from './commands/Commands';
import LayoutTreeParser from './commands/LayoutTreeParser';
import LayoutTreeCrawler from './commands/LayoutTreeCrawler';
import PrivateEventsListener from './events/PrivateEventsListener';
import PublicEventsRegistry from './events/PublicEventsRegistry';

class Navigation {
  constructor() {
    this.store = new Store();
    this.nativeEventsReceiver = new NativeEventsReceiver();
    this.uniqueIdProvider = new UniqueIdProvider();
    this.containerRegistry = new ContainerRegistry(this.store);
    this.layoutTreeParser = new LayoutTreeParser();
    this.layoutTreeCrawler = new LayoutTreeCrawler(this.uniqueIdProvider, this.store);
    this.nativeCommandsSender = new NativeCommandsSender();
    this.commands = new Commands(this.nativeCommandsSender, this.layoutTreeParser, this.layoutTreeCrawler);
    this.publicEventsRegistry = new PublicEventsRegistry(this.nativeEventsReceiver);
    this.privateEventsListener = new PrivateEventsListener(this.nativeEventsReceiver, this.store);
    this.privateEventsListener.listenAndHandlePrivateEvents();
  }

  registerContainer(containerName, getContainerFunc) {
    this.containerRegistry.registerContainer(containerName, getContainerFunc);
  }

  setRoot(params) {
    return this.commands.setRoot(params);
  }

  showModal(params) {
    return this.commands.showModal(params);
  }

  dismissModal(containerId) {
    return this.commands.dismissModal(containerId);
  }

  dismissAllModals() {
    return this.commands.dismissAllModals();
  }

  push(onContainerId, params) {
    return this.commands.push(onContainerId, params);
  }

  pop(containerId) {
    return this.commands.pop(containerId);
  }

  popTo(containerId) {
    return this.commands.popTo(containerId);
  }

  popToRoot(containerId) {
    return this.commands.popToRoot(containerId);
  }

  events() {
    return this.publicEventsRegistry;
  }
}

const singleton = new Navigation();
export default singleton;
