"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NavigationRoot = void 0;
const tslib_1 = require("tslib");
const isArray_1 = tslib_1.__importDefault(require("lodash/isArray"));
const UniqueIdProvider_1 = require("./adapters/UniqueIdProvider");
const Store_1 = require("./components/Store");
const OptionProcessorsStore_1 = require("./processors/OptionProcessorsStore");
const ComponentRegistry_1 = require("./components/ComponentRegistry");
const Commands_1 = require("./commands/Commands");
const LayoutTreeParser_1 = require("./commands/LayoutTreeParser");
const LayoutTreeCrawler_1 = require("./commands/LayoutTreeCrawler");
const EventsRegistry_1 = require("./events/EventsRegistry");
const CommandsObserver_1 = require("./events/CommandsObserver");
const Constants_1 = require("./adapters/Constants");
const ComponentEventsObserver_1 = require("./events/ComponentEventsObserver");
const TouchablePreview_1 = require("./adapters/TouchablePreview");
const ComponentWrapper_1 = require("./components/ComponentWrapper");
const OptionsProcessor_1 = require("./commands/OptionsProcessor");
const ColorService_1 = require("./adapters/ColorService");
const AssetResolver_1 = require("./adapters/AssetResolver");
const Deprecations_1 = require("./commands/Deprecations");
const LayoutProcessor_1 = require("./processors/LayoutProcessor");
const LayoutProcessorsStore_1 = require("./processors/LayoutProcessorsStore");
const OptionsCrawler_1 = require("./commands/OptionsCrawler");
class NavigationRoot {
    nativeCommandsSender;
    nativeEventsReceiver;
    appRegistryService;
    TouchablePreview = TouchablePreview_1.TouchablePreview;
    store;
    optionProcessorsStore;
    layoutProcessorsStore;
    uniqueIdProvider;
    componentRegistry;
    layoutTreeParser;
    layoutTreeCrawler;
    commands;
    eventsRegistry;
    commandsObserver;
    componentEventsObserver;
    componentWrapper;
    optionsCrawler;
    constructor(nativeCommandsSender, nativeEventsReceiver, appRegistryService) {
        this.nativeCommandsSender = nativeCommandsSender;
        this.nativeEventsReceiver = nativeEventsReceiver;
        this.appRegistryService = appRegistryService;
        this.componentWrapper = new ComponentWrapper_1.ComponentWrapper();
        this.store = new Store_1.Store();
        this.optionProcessorsStore = new OptionProcessorsStore_1.OptionProcessorsStore();
        this.layoutProcessorsStore = new LayoutProcessorsStore_1.LayoutProcessorsStore();
        this.uniqueIdProvider = new UniqueIdProvider_1.UniqueIdProvider();
        this.componentEventsObserver = new ComponentEventsObserver_1.ComponentEventsObserver(this.nativeEventsReceiver, this.store);
        this.componentRegistry = new ComponentRegistry_1.ComponentRegistry(this.store, this.componentEventsObserver, this.componentWrapper, this.appRegistryService);
        this.layoutTreeParser = new LayoutTreeParser_1.LayoutTreeParser(this.uniqueIdProvider);
        const optionsProcessor = new OptionsProcessor_1.OptionsProcessor(this.store, this.uniqueIdProvider, this.optionProcessorsStore, new ColorService_1.ColorService(), new AssetResolver_1.AssetService(), new Deprecations_1.Deprecations());
        const layoutProcessor = new LayoutProcessor_1.LayoutProcessor(this.layoutProcessorsStore);
        this.layoutTreeCrawler = new LayoutTreeCrawler_1.LayoutTreeCrawler(this.store, optionsProcessor);
        this.commandsObserver = new CommandsObserver_1.CommandsObserver(this.uniqueIdProvider);
        this.optionsCrawler = new OptionsCrawler_1.OptionsCrawler(this.store, this.uniqueIdProvider);
        this.commands = new Commands_1.Commands(this.store, this.nativeCommandsSender, this.layoutTreeParser, this.layoutTreeCrawler, this.commandsObserver, this.uniqueIdProvider, optionsProcessor, layoutProcessor, this.optionsCrawler);
        this.eventsRegistry = new EventsRegistry_1.EventsRegistry(this.nativeEventsReceiver, this.commandsObserver, this.componentEventsObserver);
        this.componentEventsObserver.registerOnceForAllComponentEvents();
    }
    /**
     * Every navigation component in your app must be registered with a unique name.
     * The component itself is a traditional React component extending React.Component.
     */
    registerComponent(componentName, componentProvider, concreteComponentProvider) {
        return this.componentRegistry.registerComponent(componentName, componentProvider, concreteComponentProvider);
    }
    /**
     * Adds an option processor which allows option interpolation by optionPath.
     */
    addOptionProcessor(optionPath, processor) {
        return this.optionProcessorsStore.addProcessor(optionPath, processor);
    }
    /**
     * Method to be invoked when a layout is processed and is about to be created. This can be used to change layout options or even inject props to components.
     */
    addLayoutProcessor(processor) {
        return this.layoutProcessorsStore.addProcessor(processor);
    }
    setLazyComponentRegistrator(lazyRegistratorFn) {
        this.store.setLazyComponentRegistrator(lazyRegistratorFn);
    }
    /**
     * Utility helper function like registerComponent,
     * wraps the provided component with a react-redux Provider with the passed redux store
     * @deprecated
     */
    registerComponentWithRedux(componentName, getComponentClassFunc, ReduxProvider, reduxStore) {
        console.warn('registerComponentWithRedux is deprecated and will be removed in the next version! Please use Navigation.registerComponent instead. Visit the docs for more information https://wix.github.io/react-native-navigation/api/component#registering-a-component-wrapped-with-providers');
        return this.componentRegistry.registerComponent(componentName, getComponentClassFunc, undefined, ReduxProvider, reduxStore);
    }
    /**
     * Reset the app to a new layout
     */
    setRoot(layout) {
        return this.commands.setRoot(layout);
    }
    /**
     * Set default options to all screens. Useful for declaring a consistent style across the app.
     */
    setDefaultOptions(options) {
        this.commands.setDefaultOptions(options);
    }
    /**
     * Change a component's navigation options
     */
    mergeOptions(componentId, options) {
        this.commands.mergeOptions(componentId, options);
    }
    /**
     * Update a mounted component's props
     */
    updateProps(componentId, props, callback) {
        this.commands.updateProps(componentId, props, callback);
    }
    /**
     * Show a screen as a modal.
     */
    showModal(layout) {
        return this.commands.showModal(layout);
    }
    /**
     * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
     */
    dismissModal(componentId, mergeOptions) {
        return this.commands.dismissModal(componentId, mergeOptions);
    }
    /**
     * Dismiss all Modals
     */
    dismissAllModals(mergeOptions) {
        return this.commands.dismissAllModals(mergeOptions);
    }
    /**
     * Push a new layout into this screen's navigation stack.
     */
    push(componentId, layout) {
        return this.commands.push(componentId, layout);
    }
    /**
     * Pop a component from the stack, regardless of it's position.
     */
    pop(componentId, mergeOptions) {
        return this.commands.pop(componentId, mergeOptions);
    }
    /**
     * Pop the stack to a given component
     */
    popTo(componentId, mergeOptions) {
        return this.commands.popTo(componentId, mergeOptions);
    }
    /**
     * Pop the component's stack to root.
     */
    popToRoot(componentId, mergeOptions) {
        return this.commands.popToRoot(componentId, mergeOptions);
    }
    /**
     * Sets new root component to stack.
     */
    setStackRoot(componentId, layout) {
        const children = (0, isArray_1.default)(layout) ? layout : [layout];
        return this.commands.setStackRoot(componentId, children);
    }
    /**
     * Show overlay on top of the entire app
     */
    showOverlay(layout) {
        return this.commands.showOverlay(layout);
    }
    /**
     * dismiss overlay by componentId
     */
    dismissOverlay(componentId) {
        return this.commands.dismissOverlay(componentId);
    }
    /**
     * dismiss all overlays
     */
    dismissAllOverlays() {
        return this.commands.dismissAllOverlays();
    }
    /**
     * Resolves arguments passed on launch
     */
    getLaunchArgs() {
        return this.commands.getLaunchArgs();
    }
    /**
     * Obtain the events registry instance
     */
    events() {
        return this.eventsRegistry;
    }
    /**
     * Constants coming from native
     */
    async constants() {
        return await Constants_1.Constants.get(this.nativeCommandsSender);
    }
    /**
     * Constants coming from native (synchronized call)
     */
    constantsSync() {
        return Constants_1.Constants.getSync(this.nativeCommandsSender);
    }
}
exports.NavigationRoot = NavigationRoot;
