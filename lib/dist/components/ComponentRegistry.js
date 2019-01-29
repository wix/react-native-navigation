"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class ComponentRegistry {
    constructor(store, componentEventsObserver, componentWrapper, appRegistryService) {
        this.store = store;
        this.componentEventsObserver = componentEventsObserver;
        this.componentWrapper = componentWrapper;
        this.appRegistryService = appRegistryService;
    }
    registerComponent(componentName, componentProvider, concreteComponentProvider, ReduxProvider, reduxStore) {
        const NavigationComponent = () => {
            return this.componentWrapper.wrap(componentName.toString(), componentProvider, this.store, this.componentEventsObserver, concreteComponentProvider, ReduxProvider, reduxStore);
        };
        this.store.setComponentClassForName(componentName.toString(), NavigationComponent);
        this.appRegistryService.registerComponent(componentName.toString(), NavigationComponent);
        return NavigationComponent;
    }
}
exports.ComponentRegistry = ComponentRegistry;
