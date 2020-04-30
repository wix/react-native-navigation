"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const isArray_1 = tslib_1.__importDefault(require("lodash/isArray"));
const NativeCommandsSender_1 = require("./adapters/NativeCommandsSender");
const NativeEventsReceiver_1 = require("./adapters/NativeEventsReceiver");
const UniqueIdProvider_1 = require("./adapters/UniqueIdProvider");
const Store_1 = require("./components/Store");
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
const AppRegistryService_1 = require("./adapters/AppRegistryService");
const Deprecations_1 = require("./commands/Deprecations");
class NavigationRoot {
    constructor() {
        this.TouchablePreview = TouchablePreview_1.TouchablePreview;
        this.componentWrapper = new ComponentWrapper_1.ComponentWrapper();
        this.store = new Store_1.Store();
        this.nativeEventsReceiver = new NativeEventsReceiver_1.NativeEventsReceiver();
        this.uniqueIdProvider = new UniqueIdProvider_1.UniqueIdProvider();
        this.componentEventsObserver = new ComponentEventsObserver_1.ComponentEventsObserver(this.nativeEventsReceiver, this.store);
        const appRegistryService = new AppRegistryService_1.AppRegistryService();
        this.componentRegistry = new ComponentRegistry_1.ComponentRegistry(this.store, this.componentEventsObserver, this.componentWrapper, appRegistryService);
        this.layoutTreeParser = new LayoutTreeParser_1.LayoutTreeParser(this.uniqueIdProvider);
        const optionsProcessor = new OptionsProcessor_1.OptionsProcessor(this.store, this.uniqueIdProvider, new ColorService_1.ColorService(), new AssetResolver_1.AssetService(), new Deprecations_1.Deprecations());
        this.layoutTreeCrawler = new LayoutTreeCrawler_1.LayoutTreeCrawler(this.store, optionsProcessor);
        this.nativeCommandsSender = new NativeCommandsSender_1.NativeCommandsSender();
        this.commandsObserver = new CommandsObserver_1.CommandsObserver(this.uniqueIdProvider);
        this.commands = new Commands_1.Commands(this.store, this.nativeCommandsSender, this.layoutTreeParser, this.layoutTreeCrawler, this.commandsObserver, this.uniqueIdProvider, optionsProcessor);
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
     * Utility helper function like registerComponent,
     * wraps the provided component with a react-redux Provider with the passed redux store
     */
    registerComponentWithRedux(componentName, getComponentClassFunc, ReduxProvider, reduxStore) {
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
    updateProps(componentId, props) {
        this.commands.updateProps(componentId, props);
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
        const children = isArray_1.default(layout) ? layout : [layout];
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
        return await Constants_1.Constants.get();
    }
}
exports.NavigationRoot = NavigationRoot;
