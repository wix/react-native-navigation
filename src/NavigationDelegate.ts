import { EventsRegistry } from './events/EventsRegistry';
import { ComponentProvider } from 'react-native';
import { NavigationConstants } from './adapters/Constants';
import { LayoutRoot, Layout } from './interfaces/Layout';
import { Options } from './interfaces/Options';
import { ProcessorSubscription } from './interfaces/ProcessorSubscription';
import { CommandName } from './interfaces/CommandName';
import { OptionsProcessor as OptionProcessor } from './interfaces/Processors';
import { NavigationRoot } from './Navigation';
import { NativeCommandsSender } from './adapters/NativeCommandsSender';
import { NativeEventsReceiver } from './adapters/NativeEventsReceiver';
import { AppRegistryService } from './adapters/AppRegistryService';

export class NavigationDelegate {
  private concreteNavigation: NavigationRoot;
  constructor() {
    this.concreteNavigation = this.createConcreteNavigation(
      new NativeCommandsSender(),
      new NativeEventsReceiver(),
      new AppRegistryService()
    );
  }

  private createConcreteNavigation(
    nativeCommandsSender: NativeCommandsSender,
    nativeEventsReceiver: NativeEventsReceiver,
    appRegistryService: AppRegistryService
  ) {
    return new NavigationRoot(nativeCommandsSender, nativeEventsReceiver, appRegistryService);
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
    return this.concreteNavigation.registerComponent(
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
    return this.concreteNavigation.addOptionProcessor(optionPath, processor);
  }

  /**
   * Method to be invoked when a layout is processed and is about to be created. This can be used to change layout options or even inject props to components.
   */
  public addLayoutProcessor(
    processor: (layout: Layout, commandName: CommandName) => Layout
  ): ProcessorSubscription {
    return this.concreteNavigation.addLayoutProcessor(processor);
  }

  public setLazyComponentRegistrator(
    lazyRegistratorFn: (lazyComponentRequest: string | number) => void
  ) {
    this.concreteNavigation.setLazyComponentRegistrator(lazyRegistratorFn);
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
    return this.concreteNavigation.registerComponentWithRedux(
      componentName,
      getComponentClassFunc,
      ReduxProvider,
      reduxStore
    );
  }

  /**
   * Reset the app to a new layout
   */
  public setRoot(layout: LayoutRoot): Promise<string> {
    return this.concreteNavigation.setRoot(layout);
  }

  /**
   * Set default options to all screens. Useful for declaring a consistent style across the app.
   */
  public setDefaultOptions(options: Options): void {
    this.concreteNavigation.setDefaultOptions(options);
  }

  /**
   * Change a component's navigation options
   */
  public mergeOptions(componentId: string, options: Options): void {
    this.concreteNavigation.mergeOptions(componentId, options);
  }

  /**
   * Update a mounted component's props
   */
  public updateProps(componentId: string, props: object, callback?: () => void) {
    this.concreteNavigation.updateProps(componentId, props, callback);
  }

  /**
   * Show a screen as a modal.
   */
  public showModal<P>(layout: Layout<P>): Promise<string> {
    return this.concreteNavigation.showModal(layout);
  }

  /**
   * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
   */
  public dismissModal(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.concreteNavigation.dismissModal(componentId, mergeOptions);
  }

  /**
   * Dismiss all Modals
   */
  public dismissAllModals(mergeOptions?: Options): Promise<string> {
    return this.concreteNavigation.dismissAllModals(mergeOptions);
  }

  /**
   * Push a new layout into this screen's navigation stack.
   */
  public push<P>(componentId: string, layout: Layout<P>): Promise<string> {
    return this.concreteNavigation.push(componentId, layout);
  }

  /**
   * Pop a component from the stack, regardless of it's position.
   */
  public pop(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.concreteNavigation.pop(componentId, mergeOptions);
  }

  /**
   * Pop the stack to a given component
   */
  public popTo(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.concreteNavigation.popTo(componentId, mergeOptions);
  }

  /**
   * Pop the component's stack to root.
   */
  public popToRoot(componentId: string, mergeOptions?: Options): Promise<string> {
    return this.concreteNavigation.popToRoot(componentId, mergeOptions);
  }

  /**
   * Sets new root component to stack.
   */
  public setStackRoot<P>(
    componentId: string,
    layout: Layout<P> | Array<Layout<P>>
  ): Promise<string> {
    return this.concreteNavigation.setStackRoot(componentId, layout);
  }

  /**
   * Show overlay on top of the entire app
   */
  public showOverlay<P>(layout: Layout<P>): Promise<string> {
    return this.concreteNavigation.showOverlay(layout);
  }

  /**
   * dismiss overlay by componentId
   */
  public dismissOverlay(componentId: string): Promise<string> {
    return this.concreteNavigation.dismissOverlay(componentId);
  }

  /**
   * dismiss all overlays
   */
  public dismissAllOverlays(): Promise<string> {
    return this.concreteNavigation.dismissAllOverlays();
  }

  /**
   * Resolves arguments passed on launch
   */
  public getLaunchArgs(): Promise<any> {
    return this.concreteNavigation.getLaunchArgs();
  }

  /**
   * Obtain the events registry instance
   */
  public events(): EventsRegistry {
    return this.concreteNavigation.events();
  }

  /**
   * Constants coming from native
   */
  public async constants(): Promise<NavigationConstants> {
    return await this.concreteNavigation.constants();
  }

  public constantsSync(): NavigationConstants {
    return this.concreteNavigation.constantsSync();
  }

  get TouchablePreview() {
    return this.concreteNavigation.TouchablePreview;
  }

  public mockNativeComponents(
    mockedNativeCommandsSender: NativeCommandsSender,
    mockedNativeEventsReceiver: NativeEventsReceiver,
    mockedAppRegistryService: AppRegistryService
  ) {
    this.concreteNavigation = this.createConcreteNavigation(
      mockedNativeCommandsSender,
      mockedNativeEventsReceiver,
      mockedAppRegistryService
    );
  }

  public get mock() {
    return {
      store: this.concreteNavigation.store,
    };
  }
}
