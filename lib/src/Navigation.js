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
const Root = require('./params/Root');
const NavigationOptions = require('./params/NavigationOptions');

/** @constructor */
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

  /**
   * Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.
   * @param {string} containerName Unique container name
   * @param {function} getContainerFunc generator function, typically `() => require('./myContainer')`
   */
  registerContainer(containerName, getContainerFunc) {
    this.containerRegistry.registerContainer(containerName, getContainerFunc);
  }

  /**
   * Reset the navigation stack to a new screen (the stack root is changed).
   * @param {Root} root
   */
  setRoot(params) {
    return this.commands.setRoot(new Root(params));
  }

  /**
   * Set default options to all screens. Useful for declaring a consistent style across the app.
   * @param {NavigationOptions} options
   */
  setDefaultOptions(options) {
    this.commands.setDefaultOptions(options);
  }

  /**
   * Change a containers navigation options
   * @param {string} containerId The container's id.
   * @param {NavigationOptions} options
   */
  setOptions(containerId, options) {
    this.commands.setOptions(containerId, new NavigationOptions(options));
  }

  /**
   * Show a screen as a modal.
   * @param {Object} params
   */
  showModal(params) {
    return this.commands.showModal(params);
  }

  /**
   * Dismiss a modal by containerId. The dismissed modal can be anywhere in the stack.
   * @param {String} containerId The container's id.
   */
  dismissModal(containerId) {
    return this.commands.dismissModal(containerId);
  }

  /**
   * Dismiss all Modals
   */
  dismissAllModals() {
    return this.commands.dismissAllModals();
  }

  /**
   * Push a new screen into this screen's navigation stack.
   * @param {String} containerId The container's id.
   * @param {*} params
   */
  push(containerId, params) {
    return this.commands.push(containerId, params);
  }

  /**
   * Pop a container from the stack, regardless of it's position.
   * @param {String} containerId The container's id.
   * @param {*} params
   */
  pop(containerId, params) {
    return this.commands.pop(containerId, params);
  }

  /**
   * Pop the stack to a given container
   * @param {String} containerId The container's id.
   */
  popTo(containerId) {
    return this.commands.popTo(containerId);
  }

  /**
   * Pop the container's stack to root.
   * @param {*} containerId
   */
  popToRoot(containerId) {
    return this.commands.popToRoot(containerId);
  }

  /**
   * Obtain the events registery instance
   */
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
