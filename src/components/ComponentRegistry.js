"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ComponentRegistry = void 0;
class ComponentRegistry {
    store;
    componentEventsObserver;
    componentWrapper;
    appRegistryService;
    constructor(store, componentEventsObserver, componentWrapper, appRegistryService) {
        this.store = store;
        this.componentEventsObserver = componentEventsObserver;
        this.componentWrapper = componentWrapper;
        this.appRegistryService = appRegistryService;
    }
    registerComponent(componentName, componentProvider, concreteComponentProvider, ReduxProvider, reduxStore) {
        const NavigationComponent = () => {
            if (this.store.hasRegisteredWrappedComponent(componentName)) {
                return this.store.getWrappedComponent(componentName);
            }
            else {
                const wrappedComponent = this.componentWrapper.wrap(componentName.toString(), componentProvider, this.store, this.componentEventsObserver, concreteComponentProvider, ReduxProvider, reduxStore);
                this.store.setWrappedComponent(componentName, wrappedComponent);
                return wrappedComponent;
            }
        };
        this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
        this.appRegistryService.registerComponent(componentName.toString(), NavigationComponent);
        return NavigationComponent;
    }
}
exports.ComponentRegistry = ComponentRegistry;
