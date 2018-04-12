import { NativeCommandsSender } from './adapters/NativeCommandsSender';
import { NativeEventsReceiver } from './adapters/NativeEventsReceiver';
import { UniqueIdProvider } from './adapters/UniqueIdProvider';
import { Store } from './components/Store';
import { ComponentRegistry } from './components/ComponentRegistry';
import { Commands } from './commands/Commands';
import { LayoutTreeParser } from './commands/LayoutTreeParser';
import { LayoutTreeCrawler } from './commands/LayoutTreeCrawler';
import { EventsRegistry } from './events/EventsRegistry';
import { ComponentProvider } from 'react-native';
import { Element } from './adapters/Element';
import { ComponentEventsObserver } from './events/ComponentEventsObserver';
import { CommandsObserver } from './events/CommandsObserver';

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
  private readonly eventsRegistry;
  private readonly commandsObserver;
  private readonly componentEventsObserver;

  constructor() {
    this.Element = Element;

    this.store = new Store();
    this.nativeEventsReceiver = new NativeEventsReceiver();
    this.uniqueIdProvider = new UniqueIdProvider();
    this.componentRegistry = new ComponentRegistry(this.store);
    this.layoutTreeParser = new LayoutTreeParser();
    this.layoutTreeCrawler = new LayoutTreeCrawler(this.uniqueIdProvider, this.store);
    this.nativeCommandsSender = new NativeCommandsSender();
    this.commandsObserver = new CommandsObserver();
    this.commands = new Commands(this.nativeCommandsSender, this.layoutTreeParser, this.layoutTreeCrawler, this.commandsObserver);
    this.eventsRegistry = new EventsRegistry(this.nativeEventsReceiver, this.commandsObserver);
    this.componentEventsObserver = new ComponentEventsObserver(this.eventsRegistry, this.store);

    this.componentEventsObserver.registerForAllComponents();
  }

  /**
   * Every navigation component in your app must be registered with a unique name.
   * The component itself is a traditional React component extending React.Component.
   */
  public registerComponent(componentName: string, getComponentClassFunc: ComponentProvider) {
    this.componentRegistry.registerComponent(componentName, getComponentClassFunc);
  }

  /**
   * Reset the app to a new layout
   */
  public setRoot(layout): Promise<any> {
    return this.commands.setRoot(layout);
  }

  /**
   * Set default options to all screens. Useful for declaring a consistent style across the app.
   */
  public setDefaultOptions(options): void {
    this.commands.setDefaultOptions(options);
  }

  /**
   * Change a component's navigation options
   */
  public setOptions(componentId: string, options): void {
    this.commands.setOptions(componentId, options);
  }

  /**
   * Show a screen as a modal.
   */
  public showModal(layout): Promise<any> {
    return this.commands.showModal(layout);
  }

  /**
   * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
   */
  public dismissModal(componentId: string): Promise<any> {
    return this.commands.dismissModal(componentId);
  }

  /**
   * Dismiss all Modals
   */
  public dismissAllModals(): Promise<any> {
    return this.commands.dismissAllModals();
  }

  /**
   * Push a new layout into this screen's navigation stack.
   */
  public push(componentId: string, layout): Promise<any> {
    return this.commands.push(componentId, layout);
  }

  /**
   * Pop a component from the stack, regardless of it's position.
   */
  public pop(componentId: string, params): Promise<any> {
    return this.commands.pop(componentId, params);
  }

  /**
   * Pop the stack to a given component
   */
  public popTo(componentId: string): Promise<any> {
    return this.commands.popTo(componentId);
  }

  /**
   * Pop the component's stack to root.
   */
  public popToRoot(componentId: string): Promise<any> {
    return this.commands.popToRoot(componentId);
  }

  /**
   * Sets new root component to stack.
   */
  public setStackRoot(componentId: string, layout): Promise<any> {
    return this.commands.setStackRoot(componentId, layout);
  }

  /**
   * Show overlay on top of the entire app
   */
  public showOverlay(layout): Promise<any> {
    return this.commands.showOverlay(layout);
  }

  /**
   * dismiss overlay by componentId
   */
  public dismissOverlay(componentId: string): Promise<any> {
    return this.commands.dismissOverlay(componentId);
  }

  /**
   * Obtain the events registry instance
   */
  public events(): EventsRegistry {
    return this.eventsRegistry;
  }
}
