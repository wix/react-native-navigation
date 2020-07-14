import isArray from 'lodash/isArray';
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
import { CommandsObserver } from './events/CommandsObserver';
import { Constants, NavigationConstants } from './adapters/Constants';
import { ComponentEventsObserver } from './events/ComponentEventsObserver';
import { TouchablePreview } from './adapters/TouchablePreview';
import { LayoutRoot, Layout } from './interfaces/Layout';
import { Options } from './interfaces/Options';
import { ComponentWrapper } from './components/ComponentWrapper';
import { OptionsProcessor } from './commands/OptionsProcessor';
import { ColorService } from './adapters/ColorService';
import { AssetService } from './adapters/AssetResolver';
import { AppRegistryService } from './adapters/AppRegistryService';
import { Deprecations } from './commands/Deprecations';

export class NavigationRoot {
  public readonly TouchablePreview = TouchablePreview;

  private readonly store: Store;
  private readonly nativeEventsReceiver: NativeEventsReceiver;
  private readonly uniqueIdProvider: UniqueIdProvider;
  private readonly componentRegistry: ComponentRegistry;
  private readonly layoutTreeParser: LayoutTreeParser;
  private readonly layoutTreeCrawler: LayoutTreeCrawler;
  private readonly nativeCommandsSender: NativeCommandsSender;
  private readonly commands: Commands;
  private readonly eventsRegistry: EventsRegistry;
  private readonly commandsObserver: CommandsObserver;
  private readonly componentEventsObserver: ComponentEventsObserver;
  private readonly componentWrapper: ComponentWrapper;

  constructor() {
    this.componentWrapper = new ComponentWrapper();
    this.store = new Store();
    this.nativeEventsReceiver = new NativeEventsReceiver();
    this.uniqueIdProvider = new UniqueIdProvider();
    this.componentEventsObserver = new ComponentEventsObserver(this.nativeEventsReceiver, this.store);
    const appRegistryService = new AppRegistryService();
    this.componentRegistry = new ComponentRegistry(
      this.store,
      this.componentEventsObserver,
      this.componentWrapper,
      appRegistryService
    );
    this.layoutTreeParser = new LayoutTreeParser(this.uniqueIdProvider);
    const optionsProcessor = new OptionsProcessor(this.store, this.uniqueIdProvider, new ColorService(), new AssetService(), new Deprecations());
    this.layoutTreeCrawler = new LayoutTreeCrawler(this.store, optionsProcessor);
    this.nativeCommandsSender = new NativeCommandsSender();
    this.commandsObserver = new CommandsObserver(this.uniqueIdProvider);
    this.commands = new Commands(
      this.store,
      this.nativeCommandsSender,
      this.layoutTreeParser,
      this.layoutTreeCrawler,
      this.commandsObserver,
      this.uniqueIdProvider,
      optionsProcessor
    );
    this.eventsRegistry = new EventsRegistry(this.nativeEventsReceiver, this.commandsObserver, this.componentEventsObserver);

    this.componentEventsObserver.registerOnceForAllComponentEvents();
  }

  /**
   * Every navigation component in your app must be registered with a unique name.
   * The component itself is a traditional React component extending React.Component.
   */
  public registerComponent(componentName: string | number, componentProvider: ComponentProvider, concreteComponentProvider?: ComponentProvider): ComponentProvider {
    return this.componentRegistry.registerComponent(componentName, componentProvider, concreteComponentProvider);
  }

  public setLazyComponentRegistrator(lazyRegistratorFn: (lazyComponentRequest: string | number) => void) {
    this.store.setLazyComponentRegistrator(lazyRegistratorFn);
  }

  /**
   * Utility helper function like registerComponent,
   * wraps the provided component with a react-redux Provider with the passed redux store
   */
  public registerComponentWithRedux(
    componentName: string | number,
    getComponentClassFunc: ComponentProvider,
    ReduxProvider: any,
    reduxStore: any
  ): ComponentProvider {
    return this.componentRegistry.registerComponent(componentName, getComponentClassFunc, undefined, ReduxProvider, reduxStore);
  }

  /**
   * Reset the app to a new layout
   */
  public setRoot(layout: LayoutRoot): Promise<any> {
    return this.commands.setRoot(layout);
  }

  /**
   * Set default options to all screens. Useful for declaring a consistent style across the app.
   */
  public setDefaultOptions(options: Options): void {
    this.commands.setDefaultOptions(options);
  }

  /**
   * Change a component's navigation options
   */
  public mergeOptions(componentId: string, options: Options): void {
    this.commands.mergeOptions(componentId, options);
  }

  /**
   * Update a mounted component's props
   */
  public updateProps(componentId: string, props: object) {
    this.commands.updateProps(componentId, props);
  }

  /**
   * Show a screen as a modal.
   */
  public showModal<P>(layout: Layout<P>): Promise<any> {
    return this.commands.showModal(layout);
  }

  /**
   * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
   */
  public dismissModal(componentId: string, mergeOptions?: Options): Promise<any> {
    return this.commands.dismissModal(componentId, mergeOptions);
  }

  /**
   * Dismiss all Modals
   */
  public dismissAllModals(mergeOptions?: Options): Promise<any> {
    return this.commands.dismissAllModals(mergeOptions);
  }

  /**
   * Push a new layout into this screen's navigation stack.
   */
  public push<P>(componentId: string, layout: Layout<P>): Promise<any> {
    return this.commands.push(componentId, layout);
  }

  /**
   * Pop a component from the stack, regardless of it's position.
   */
  public pop(componentId: string, mergeOptions?: Options): Promise<any> {
    return this.commands.pop(componentId, mergeOptions);
  }

  /**
   * Pop the stack to a given component
   */
  public popTo(componentId: string, mergeOptions?: Options): Promise<any> {
    return this.commands.popTo(componentId, mergeOptions);
  }

  /**
   * Pop the component's stack to root.
   */
  public popToRoot(componentId: string, mergeOptions?: Options): Promise<any> {
    return this.commands.popToRoot(componentId, mergeOptions);
  }

  /**
   * Sets new root component to stack.
   */
  public setStackRoot<P>(componentId: string, layout: Layout<P> | Array<Layout<P>>): Promise<any> {
    const children: Layout[] = isArray(layout) ? layout : [layout];
    return this.commands.setStackRoot(componentId, children);
  }

  /**
   * Show overlay on top of the entire app
   */
  public showOverlay<P>(layout: Layout<P>): Promise<any> {
    return this.commands.showOverlay(layout);
  }

  /**
   * dismiss overlay by componentId
   */
  public dismissOverlay(componentId: string): Promise<any> {
    return this.commands.dismissOverlay(componentId);
  }

  /**
   * Resolves arguments passed on launch
   */
  public getLaunchArgs(): Promise<any> {
    return this.commands.getLaunchArgs();
  }

  /**
   * Obtain the events registry instance
   */
  public events(): EventsRegistry {
    return this.eventsRegistry;
  }

  /**
   * Constants coming from native
   */
  public async constants(): Promise<NavigationConstants> {
    return await Constants.get();
  }
}
