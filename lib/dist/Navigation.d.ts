import { EventsRegistry } from './events/EventsRegistry';
import { ComponentProvider } from 'react-native';
import { NavigationConstants } from './adapters/Constants';
import { TouchablePreview } from './adapters/TouchablePreview';
import { LayoutRoot, Layout } from './interfaces/Layout';
import { Options } from './interfaces/Options';
export declare class NavigationRoot {
    readonly TouchablePreview: typeof TouchablePreview;
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
    private readonly componentWrapper;
    constructor();
    /**
     * Every navigation component in your app must be registered with a unique name.
     * The component itself is a traditional React component extending React.Component.
     */
    registerComponent(componentName: string | number, componentProvider: ComponentProvider, concreteComponentProvider?: ComponentProvider): ComponentProvider;
    /**
     * Utility helper function like registerComponent,
     * wraps the provided component with a react-redux Provider with the passed redux store
     */
    registerComponentWithRedux(componentName: string | number, getComponentClassFunc: ComponentProvider, ReduxProvider: any, reduxStore: any): ComponentProvider;
    /**
     * Reset the app to a new layout
     */
    setRoot(layout: LayoutRoot): Promise<any>;
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
    updateProps(componentId: string, props: object): void;
    /**
     * Show a screen as a modal.
     */
    showModal<P>(layout: Layout<P>): Promise<any>;
    /**
     * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
     */
    dismissModal(componentId: string, mergeOptions?: Options): Promise<any>;
    /**
     * Dismiss all Modals
     */
    dismissAllModals(mergeOptions?: Options): Promise<any>;
    /**
     * Push a new layout into this screen's navigation stack.
     */
    push<P>(componentId: string, layout: Layout<P>): Promise<any>;
    /**
     * Pop a component from the stack, regardless of it's position.
     */
    pop(componentId: string, mergeOptions?: Options): Promise<any>;
    /**
     * Pop the stack to a given component
     */
    popTo(componentId: string, mergeOptions?: Options): Promise<any>;
    /**
     * Pop the component's stack to root.
     */
    popToRoot(componentId: string, mergeOptions?: Options): Promise<any>;
    /**
     * Sets new root component to stack.
     */
    setStackRoot<P>(componentId: string, layout: Layout<P> | Array<Layout<P>>): Promise<any>;
    /**
     * Show overlay on top of the entire app
     */
    showOverlay<P>(layout: Layout<P>): Promise<any>;
    /**
     * dismiss overlay by componentId
     */
    dismissOverlay(componentId: string): Promise<any>;
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
}
