"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Store = void 0;
class Store {
    componentsByName = {};
    propsById = {};
    pendingPropsById = {};
    componentsInstancesById = {};
    wrappedComponents = {};
    lazyRegistratorFn;
    updateProps(componentId, props, callback) {
        this.mergeNewPropsForId(componentId, props);
        const component = this.componentsInstancesById[componentId];
        if (component) {
            component.setProps(props, callback);
        }
    }
    setPendingProps(componentId, newProps) {
        this.pendingPropsById[componentId] = newProps;
    }
    getPropsForId(componentId) {
        return this.pendingPropsById[componentId] || this.propsById[componentId] || {};
    }
    consumePendingProps(componentId) {
        if (this.pendingPropsById[componentId]) {
            this.propsById[componentId] = this.pendingPropsById[componentId];
            delete this.pendingPropsById[componentId];
        }
    }
    mergeNewPropsForId(componentId, newProps) {
        const currentProps = this.getPropsForId(componentId);
        this.propsById[componentId] = {
            ...currentProps,
            ...newProps,
        };
    }
    clearComponent(componentId) {
        delete this.propsById[componentId];
        delete this.componentsInstancesById[componentId];
    }
    setComponentClassForName(componentName, ComponentClass) {
        delete this.wrappedComponents[componentName];
        this.componentsByName[componentName.toString()] = ComponentClass;
    }
    getComponentClassForName(componentName) {
        this.ensureClassForName(componentName);
        return this.componentsByName[componentName.toString()];
    }
    ensureClassForName(componentName) {
        if (!this.componentsByName[componentName.toString()] && this.lazyRegistratorFn) {
            this.lazyRegistratorFn(componentName);
        }
    }
    setComponentInstance(id, component) {
        this.componentsInstancesById[id] = component;
    }
    getComponentInstance(id) {
        return this.componentsInstancesById[id];
    }
    setWrappedComponent(componentName, wrappedComponent) {
        this.wrappedComponents[componentName] = wrappedComponent;
    }
    hasRegisteredWrappedComponent(componentName) {
        return componentName in this.wrappedComponents;
    }
    getWrappedComponent(componentName) {
        return this.wrappedComponents[componentName];
    }
    setLazyComponentRegistrator(lazyRegistratorFn) {
        this.lazyRegistratorFn = lazyRegistratorFn;
    }
}
exports.Store = Store;
