import { NativeCommandsSender } from './adapters/NativeCommandsSender';
import { NativeEventsReceiver } from './adapters/NativeEventsReceiver';
import { UniqueIdProvider } from './adapters/UniqueIdProvider';
import { Store } from './components/Store';
import { ComponentRegistry } from './components/ComponentRegistry';
import { Commands } from './commands/Commands';
import { LayoutTreeParser } from './commands/LayoutTreeParser';
import { LayoutTreeCrawler } from './commands/LayoutTreeCrawler';
import { PublicEventsRegistry } from './events/PublicEventsRegistry';
import { ComponentProvider } from 'react-native';
import { Element } from './adapters/Element';
import { PrivateEventsListener } from './events/PrivateEventsListener';

export class Navigation {
  public readonly Element;

  private readonly store;
  private readonly nativeEventsReceiver;
  private readonly uniqueIdProvider;
  private readonly componentRegistry;
  private readonly layoutTreeParser;
  private readonly layoutTreeCrawler;
  private readonly nativeCommandsSender;
  private readonly commands;
  private readonly publicEventsRegistry;

  constructor() {
    this.Element = Element;

    this.store = new Store();
    this.nativeEventsReceiver = new NativeEventsReceiver();
    this.uniqueIdProvider = new UniqueIdProvider();
    this.componentRegistry = new ComponentRegistry(this.store);
    this.layoutTreeParser = new LayoutTreeParser();
    this.layoutTreeCrawler = new LayoutTreeCrawler(this.uniqueIdProvider, this.store);
    this.nativeCommandsSender = new NativeCommandsSender();
    this.commands = new Commands(this.nativeCommandsSender, this.layoutTreeParser, this.layoutTreeCrawler);
    this.publicEventsRegistry = new PublicEventsRegistry(this.nativeEventsReceiver);

    new PrivateEventsListener(this.nativeEventsReceiver, this.store).listenAndHandlePrivateEvents();
  }

  /**
   * Every navigation component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.
   * @param {string} componentName Unique component name
   * @param {function} getComponentClassFunc generator function, typically `() => require('./myComponent')`
   */
  public registerComponent(componentName: string, getComponentClassFunc: ComponentProvider) {
    this.componentRegistry.registerComponent(componentName, getComponentClassFunc);
  }

  /**
   * Reset the navigation stack to a new component (the stack root is changed).
   * @param {Root} root
   */
  public setRoot(params) {
    return this.commands.setRoot(params);
  }

  /**
   * Set default options to all screens. Useful for declaring a consistent style across the app.
   * @param {options:Options} options
   */
  public setDefaultOptions(options) {
    this.commands.setDefaultOptions(options);
  }

  /**
   * Change a components navigation options
   * @param {string} componentId The component's id.
   * @param {options:Options} options
   */
  public setOptions(componentId, options) {
    this.commands.setOptions(componentId, options);
  }

  /**
   * Show a screen as a modal.
   * @param {object} params
   */
  public showModal(params) {
    return this.commands.showModal(params);
  }

  /**
   * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
   * @param {string} componentId The component's id.
   */
  public dismissModal(componentId) {
    return this.commands.dismissModal(componentId);
  }

  /**
   * Dismiss all Modals
   */
  public dismissAllModals() {
    return this.commands.dismissAllModals();
  }

  /**
   * Push a new screen into this screen's navigation stack.
   * @param {string} componentId The component's id.
   * @param {Component} component
   */
  public push(componentId, component) {
    return this.commands.push(componentId, component);
  }

  /**
   * Pop a component from the stack, regardless of it's position.
   * @param {string} componentId The component's id.
   * @param {*} params
   */
  public pop(componentId, params) {
    return this.commands.pop(componentId, params);
  }

  /**
   * Pop the stack to a given component
   * @param {string} componentId The component's id.
   */
  public popTo(componentId: string) {
    return this.commands.popTo(componentId);
  }

  /**
   * Pop the component's stack to root.
   * @param {*} componentId
   */
  public popToRoot(componentId: string) {
    return this.commands.popToRoot(componentId);
  }

  /**
   * Show overlay on top of the window
   * @param {*} params
   */
  public showOverlay(params) {
    return this.commands.showOverlay(params);
  }

  /**
   * dismiss overlay by componentId
   * @param {string} componentId
   */
  public dismissOverlay(componentId: string) {
    return this.commands.dismissOverlay(componentId);
  }

  /**
   * Obtain the events registry instance
   */
  public events() {
    return this.publicEventsRegistry;
  }
}
