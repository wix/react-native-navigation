import { NativeCommandsSender } from './adapters/NativeCommandsSender';
import { NativeEventsReceiver } from './adapters/NativeEventsReceiver';
import { Store } from './components/Store';
import { EventsRegistry } from './events/EventsRegistry';
import { ComponentProvider } from 'react-native';
import { NavigationConstants } from './adapters/Constants';
import { TouchablePreview } from './adapters/TouchablePreview';
import { LayoutRoot, Layout } from './interfaces/Layout';
import { Options } from './interfaces/Options';
import { AppRegistryService } from './adapters/AppRegistryService';
import { ProcessorSubscription } from './interfaces/ProcessorSubscription';
import { CommandName } from './interfaces/CommandName';
import { OptionsProcessor as OptionProcessor } from './interfaces/Processors';
export declare class NavigationRoot {
    private readonly nativeCommandsSender;
    private readonly nativeEventsReceiver;
    private readonly appRegistryService;
    readonly TouchablePreview: typeof TouchablePreview;
    readonly store: Store;
    private readonly optionProcessorsStore;
    private readonly layoutProcessorsStore;
    private readonly uniqueIdProvider;
    private readonly componentRegistry;
    private readonly layoutTreeParser;
    private readonly layoutTreeCrawler;
    private readonly commands;
    private readonly eventsRegistry;
    private readonly commandsObserver;
    private readonly componentEventsObserver;
    private readonly componentWrapper;
    private readonly optionsCrawler;
    constructor(nativeCommandsSender: NativeCommandsSender, nativeEventsReceiver: NativeEventsReceiver, appRegistryService: AppRegistryService);
    /**
     * Every navigation component in your app must be registered with a unique name.
     * The component itself is a traditional React component extending React.Component.
     */
    registerComponent(componentName: string | number, componentProvider: ComponentProvider, concreteComponentProvider?: ComponentProvider): ComponentProvider;
    /**
     * Adds an option processor which allows option interpolation by optionPath.
     */
    addOptionProcessor<T, S = any>(optionPath: string, processor: OptionProcessor<T, S>): ProcessorSubscription;
    /**
     * Method to be invoked when a layout is processed and is about to be created. This can be used to change layout options or even inject props to components.
     */
    addLayoutProcessor(processor: (layout: Layout, commandName: CommandName) => Layout): ProcessorSubscription;
    setLazyComponentRegistrator(lazyRegistratorFn: (lazyComponentRequest: string | number) => void): void;
    /**
     * Utility helper function like registerComponent,
     * wraps the provided component with a react-redux Provider with the passed redux store
     * @deprecated
     */
    registerComponentWithRedux(componentName: string | number, getComponentClassFunc: ComponentProvider, ReduxProvider: any, reduxStore: any): ComponentProvider;
    /**
     * Reset the app to a new layout
     */
    setRoot(layout: LayoutRoot): Promise<string>;
    /**
     * Set default options to all screens. Useful for declaring a consistent style across the app.
     */
    setDefaultOptions(options: Options): void;
    /**
     * Change a component's navigation options
     */
    mergeOptions(componentId: string, options: Options): void;
    /**
     * Update a mounted component's props
     */
    updateProps(componentId: string, props: object, callback?: () => void): void;
    /**
     * Show a screen as a modal.
     */
    showModal<P>(layout: Layout<P>): Promise<string>;
    /**
     * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
     */
    dismissModal(componentId: string, mergeOptions?: Options): Promise<string>;
    /**
     * Dismiss all Modals
     */
    dismissAllModals(mergeOptions?: Options): Promise<string>;
    /**
     * Push a new layout into this screen's navigation stack.
     */
    push<P>(componentId: string, layout: Layout<P>): Promise<string>;
    /**
     * Pop a component from the stack, regardless of it's position.
     */
    pop(componentId: string, mergeOptions?: Options): Promise<string>;
    /**
     * Pop the stack to a given component
     */
    popTo(componentId: string, mergeOptions?: Options): Promise<string>;
    /**
     * Pop the component's stack to root.
     */
    popToRoot(componentId: string, mergeOptions?: Options): Promise<string>;
    /**
     * Sets new root component to stack.
     */
    setStackRoot<P>(componentId: string, layout: Layout<P> | Array<Layout<P>>): Promise<string>;
    /**
     * Show overlay on top of the entire app
     */
    showOverlay<P>(layout: Layout<P>): Promise<string>;
    /**
     * dismiss overlay by componentId
     */
    dismissOverlay(componentId: string): Promise<string>;
    /**
     * dismiss all overlays
     */
    dismissAllOverlays(): Promise<string>;
    /**
     * Resolves arguments passed on launch
     */
    getLaunchArgs(): Promise<any>;
    /**
     * Obtain the events registry instance
     */
    events(): EventsRegistry;
    /**
     * Constants coming from native
     */
    constants(): Promise<NavigationConstants>;
    /**
     * Constants coming from native (synchronized call)
     */
    constantsSync(): NavigationConstants;
}
