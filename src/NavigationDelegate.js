"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NavigationDelegate = void 0;
const Navigation_1 = require("./Navigation");
const NativeCommandsSender_1 = require("./adapters/NativeCommandsSender");
const NativeEventsReceiver_1 = require("./adapters/NativeEventsReceiver");
const AppRegistryService_1 = require("./adapters/AppRegistryService");
class NavigationDelegate {
    concreteNavigation;
    constructor() {
        this.concreteNavigation = this.createConcreteNavigation(new NativeCommandsSender_1.NativeCommandsSender(), new NativeEventsReceiver_1.NativeEventsReceiver(), new AppRegistryService_1.AppRegistryService());
    }
    createConcreteNavigation(nativeCommandsSender, nativeEventsReceiver, appRegistryService) {
        return new Navigation_1.NavigationRoot(nativeCommandsSender, nativeEventsReceiver, appRegistryService);
    }
    /**
     * Every navigation component in your app must be registered with a unique name.
     * The component itself is a traditional React component extending React.Component.
     */
    registerComponent(componentName, componentProvider, concreteComponentProvider) {
        return this.concreteNavigation.registerComponent(componentName, componentProvider, concreteComponentProvider);
    }
    /**
     * Adds an option processor which allows option interpolation by optionPath.
     */
    addOptionProcessor(optionPath, processor) {
        return this.concreteNavigation.addOptionProcessor(optionPath, processor);
    }
    /**
     * Method to be invoked when a layout is processed and is about to be created. This can be used to change layout options or even inject props to components.
     */
    addLayoutProcessor(processor) {
        return this.concreteNavigation.addLayoutProcessor(processor);
    }
    setLazyComponentRegistrator(lazyRegistratorFn) {
        this.concreteNavigation.setLazyComponentRegistrator(lazyRegistratorFn);
    }
    /**
     * Utility helper function like registerComponent,
     * wraps the provided component with a react-redux Provider with the passed redux store
     * @deprecated
     */
    registerComponentWithRedux(componentName, getComponentClassFunc, ReduxProvider, reduxStore) {
        return this.concreteNavigation.registerComponentWithRedux(componentName, getComponentClassFunc, ReduxProvider, reduxStore);
    }
    /**
     * Reset the app to a new layout
     */
    setRoot(layout) {
        return this.concreteNavigation.setRoot(layout);
    }
    /**
     * Set default options to all screens. Useful for declaring a consistent style across the app.
     */
    setDefaultOptions(options) {
        this.concreteNavigation.setDefaultOptions(options);
    }
    /**
     * Change a component's navigation options
     */
    mergeOptions(componentId, options) {
        this.concreteNavigation.mergeOptions(componentId, options);
    }
    /**
     * Update a mounted component's props
     */
    updateProps(componentId, props, callback) {
        this.concreteNavigation.updateProps(componentId, props, callback);
    }
    /**
     * Show a screen as a modal.
     */
    showModal(layout) {
        return this.concreteNavigation.showModal(layout);
    }
    /**
     * Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.
     */
    dismissModal(componentId, mergeOptions) {
        return this.concreteNavigation.dismissModal(componentId, mergeOptions);
    }
    /**
     * Dismiss all Modals
     */
    dismissAllModals(mergeOptions) {
        return this.concreteNavigation.dismissAllModals(mergeOptions);
    }
    /**
     * Push a new layout into this screen's navigation stack.
     */
    push(componentId, layout) {
        return this.concreteNavigation.push(componentId, layout);
    }
    /**
     * Pop a component from the stack, regardless of it's position.
     */
    pop(componentId, mergeOptions) {
        return this.concreteNavigation.pop(componentId, mergeOptions);
    }
    /**
     * Pop the stack to a given component
     */
    popTo(componentId, mergeOptions) {
        return this.concreteNavigation.popTo(componentId, mergeOptions);
    }
    /**
     * Pop the component's stack to root.
     */
    popToRoot(componentId, mergeOptions) {
        return this.concreteNavigation.popToRoot(componentId, mergeOptions);
    }
    /**
     * Sets new root component to stack.
     */
    setStackRoot(componentId, layout) {
        return this.concreteNavigation.setStackRoot(componentId, layout);
    }
    /**
     * Show overlay on top of the entire app
     */
    showOverlay(layout) {
        return this.concreteNavigation.showOverlay(layout);
    }
    /**
     * dismiss overlay by componentId
     */
    dismissOverlay(componentId) {
        return this.concreteNavigation.dismissOverlay(componentId);
    }
    /**
     * dismiss all overlays
     */
    dismissAllOverlays() {
        return this.concreteNavigation.dismissAllOverlays();
    }
    /**
     * Resolves arguments passed on launch
     */
    getLaunchArgs() {
        return this.concreteNavigation.getLaunchArgs();
    }
    /**
     * Obtain the events registry instance
     */
    events() {
        return this.concreteNavigation.events();
    }
    /**
     * Constants coming from native
     */
    async constants() {
        return await this.concreteNavigation.constants();
    }
    constantsSync() {
        return this.concreteNavigation.constantsSync();
    }
    get TouchablePreview() {
        return this.concreteNavigation.TouchablePreview;
    }
    mockNativeComponents(mockedNativeCommandsSender, mockedNativeEventsReceiver, mockedAppRegistryService) {
        this.concreteNavigation = this.createConcreteNavigation(mockedNativeCommandsSender, mockedNativeEventsReceiver, mockedAppRegistryService);
    }
    get mock() {
        return {
            store: this.concreteNavigation.store,
        };
    }
}
exports.NavigationDelegate = NavigationDelegate;
