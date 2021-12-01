import isArray from 'lodash/isArray';
import { NativeCommandsSender } from './adapters/NativeCommandsSender';
import { NativeEventsReceiver } from './adapters/NativeEventsReceiver';
import { UniqueIdProvider } from './adapters/UniqueIdProvider';
import { Store } from './components/Store';
import { OptionProcessorsStore } from './processors/OptionProcessorsStore';
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
import { ProcessorSubscription } from './interfaces/ProcessorSubscription';
import { LayoutProcessor } from './processors/LayoutProcessor';
import { LayoutProcessorsStore } from './processors/LayoutProcessorsStore';
import { CommandName } from './interfaces/CommandName';
import { OptionsCrawler } from './commands/OptionsCrawler';
import { OptionsProcessor as OptionProcessor } from './interfaces/Processors';

export class NavigationRoot {
  public readonly TouchablePreview = TouchablePreview;

  public readonly store: Store;
  private readonly optionProcessorsStore: OptionProcessorsStore;
  private readonly layoutProcessorsStore: LayoutProcessorsStore;
  private readonly uniqueIdProvider: UniqueIdProvider;
  private readonly componentRegistry: ComponentRegistry;
  private readonly layoutTreeParser: LayoutTreeParser;
  private readonly layoutTreeCrawler: LayoutTreeCrawler;
  private readonly commands: Commands;
  private readonly eventsRegistry: EventsRegistry;
  private readonly commandsObserver: CommandsObserver;
  private readonly componentEventsObserver: ComponentEventsObserver;
  private readonly componentWrapper: ComponentWrapper;
  private readonly optionsCrawler: OptionsCrawler;

  constructor(
    private readonly nativeCommandsSender: NativeCommandsSender,
    private readonly nativeEventsReceiver: NativeEventsReceiver,
    private readonly appRegistryService: AppRegistryService
  ) {
    this.componentWrapper = new ComponentWrapper();
    this.store = new Store();
    this.optionProcessorsStore = new OptionProcessorsStore();
    this.layoutProcessorsStore = new LayoutProcessorsStore();
    this.uniqueIdProvider = new UniqueIdProvider();
    this.componentEventsObserver = new ComponentEventsObserver(
      this.nativeEventsReceiver,
      this.store
    );

    this.componentRegistry = new ComponentRegistry(
      this.store,
      this.componentEventsObserver,
      this.componentWrapper,
      this.appRegistryService
    );
    this.layoutTreeParser = new LayoutTreeParser(this.uniqueIdProvider);
    const optionsProcessor = new OptionsProcessor(
      this.store,
      this.uniqueIdProvider,
      this.optionProcessorsStore,
      new ColorService(),
      new AssetService(),
      new Deprecations()
    );
    const layoutProcessor = new LayoutProcessor(this.layoutProcessorsStore);
    this.layoutTreeCrawler = new LayoutTreeCrawler(this.store, optionsProcessor);
    this.commandsObserver = new CommandsObserver(this.uniqueIdProvider);
    this.optionsCrawler = new OptionsCrawler(this.store, this.uniqueIdProvider);
    this.commands = new Commands(
      this.store,
      this.nativeCommandsSender,
      this.layoutTreeParser,
      this.layoutTreeCrawler,
      this.commandsObserver,
      this.uniqueIdProvider,
      optionsProcessor,
      layoutProcessor,
      this.optionsCrawler
    );
    this.eventsRegistry = new EventsRegistry(
      this.nativeEventsReceiver,
      this.commandsObserver,
      this.componentEventsObserver
    );

    this.componentEventsObserver.registerOnceForAllComponentEvents();
  }

  /**
   * Every navigation component in your app must be registered with a unique name.
   * The component itself is a traditional React component extending React.Component.
   */
  public registerComponent(
    componentName: string | number,
    componentProvider: ComponentProvider,
    concreteComponentProvider?: ComponentProvider
  ): ComponentProvider {
    return this.componentRegistry.registerComponent(
      componentName,
      componentProvider,
      concreteComponentProvider
    );
  }

  /**
   * Adds an option processor which allows option interpolation by optionPath.
   */
  public addOptionProcessor<T, S = any>(
    optionPath: string,
    processor: OptionProcessor<T, S>
  ): ProcessorSubscription {
    return this.optionProcessorsStore.addProcessor(optionPath, processor);
  }

  /**
   * Method to be invoked when a layout is processed and is about to be created. This can be used to change layout options or even inject props to components.
   */
  public addLayoutProcessor(
    processor: (layout: Layout, commandName: CommandName) => Layout
  ): ProcessorSubscription {
    return this.layoutProcessorsStore.addProcessor(processor);
  }

  public setLazyComponentRegistrator(
    lazyRegistratorFn: (lazyComponentRequest: string | number) => void
  ) {
    this.store.setLazyComponentRegistrator(lazyRegistratorFn);
  }

  /**
   * Utility helper function like registerComponent,
   * wraps the provided component with a react-redux Provider with the passed redux store
   * @deprecated
   */
  public registerComponentWithRedux(
    componentName: string | number,
    getComponentClassFunc: ComponentProvider,
    ReduxProvider: any,
    reduxStore: any
  ): ComponentProvider {
    console.warn(
      'registerComponentWithRedux is deprecated and will be removed in the next version! Please use Navigation.registerComponent instead. Visit the docs for more information https://wix.github.io/react-native-navigation/api/component#registering-a-component-wrapped-with-providers'
    );
    return this.componentRegistry.registerComponent(
      componentName,
      getComponentClassFunc,
      undefined,
      ReduxProvider,
      reduxStore
    );
  }

  /**
   * Reset the app to a new layout
   */
  public setRoot(layout: LayoutRoot): Promise<string> {
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
  public updateProps(componentId: string, props: object, callback?: () => void) {
    this.commands.updateProps(componentId, props, callback);
  }

  /**
   * Show a screen as a modal.
   */
  public showModal<P>(layout: Layout<P>): Promise<string> {
    return this.commands.showModal(layout);
  }

  /**
   * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
   */
  public dismissModal(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.commands.dismissModal(componentId, mergeOptions);
  }

  /**
   * Dismiss all Modals
   */
  public dismissAllModals(mergeOptions?: Options): Promise<string> {
    return this.commands.dismissAllModals(mergeOptions);
  }

  /**
   * Push a new layout into this screen's navigation stack.
   */
  public push<P>(componentId: string, layout: Layout<P>): Promise<string> {
    return this.commands.push(componentId, layout);
  }

  /**
   * Pop a component from the stack, regardless of it's position.
   */
  public pop(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.commands.pop(componentId, mergeOptions);
  }

  /**
   * Pop the stack to a given component
   */
  public popTo(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.commands.popTo(componentId, mergeOptions);
  }

  /**
   * Pop the component's stack to root.
   */
  public popToRoot(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.commands.popToRoot(componentId, mergeOptions);
  }

  /**
   * Sets new root component to stack.
   */
  public setStackRoot<P>(
    componentId: string,
    layout: Layout<P> | Array<Layout<P>>
  ): Promise<string> {
    const children: Layout[] = isArray(layout) ? layout : [layout];
    return this.commands.setStackRoot(componentId, children);
  }

  /**
   * Show overlay on top of the entire app
   */
  public showOverlay<P>(layout: Layout<P>): Promise<string> {
    return this.commands.showOverlay(layout);
  }

  /**
   * dismiss overlay by componentId
   */
  public dismissOverlay(componentId: string): Promise<string> {
    return this.commands.dismissOverlay(componentId);
  }

  /**
   * dismiss all overlays
   */
  public dismissAllOverlays(): Promise<string> {
    return this.commands.dismissAllOverlays();
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

  /**
   * Constants coming from native (synchronized call)
   */
  public constantsSync(): NavigationConstants {
    return Constants.getSync();
  }
}
