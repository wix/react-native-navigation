const NativeCommandsSender = require('./adapters/NativeCommandsSender');
const NativeEventsReceiver = require('./adapters/NativeEventsReceiver');
const UniqueIdProvider = require('./adapters/UniqueIdProvider');
const Store = require('./containers/Store');
const ContainerRegistry = require('./containers/ContainerRegistry');
const Commands = require('./commands/Commands');
const LayoutTreeParser = require('./commands/LayoutTreeParser');
const LayoutTreeCrawler = require('./commands/LayoutTreeCrawler');
const PrivateEventsListener = require('./events/PrivateEventsListener');
const PublicEventsRegistry = require('./events/PublicEventsRegistry');
const Element = require('./adapters/Element');

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
    this.Element = Element;
  }
  registerContainer(containerName, getContainerFunc) {
    this.containerRegistry.registerContainer(containerName, getContainerFunc);
  }

  setRoot(params) {
    return this.commands.setRoot(params);
  }

  setDefaultOptions(options) {
    this.commands.setDefaultOptions(options);
  }

  setOptions(containerId, options) {
    this.commands.setOptions(containerId, options);
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

  pop(containerId, params) {
    return this.commands.pop(containerId, params);
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

  showOverlay(type, options) {
    return this.commands.showOverlay(type, options);
  }

  dismissOverlay() {
    return this.commands.dismissOverlay();
  }
}

const singleton = new Navigation();
module.exports = singleton;
