"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class Store {
    constructor() {
        this.componentsByName = {};
        this.propsById = {};
    }
    setPropsForId(componentId, props) {
        this.propsById[componentId] = props;
    }
    getPropsForId(componentId) {
        return this.propsById[componentId] || {};
    }
    cleanId(componentId) {
        delete this.propsById[componentId];
    }
    setComponentClassForName(componentName, ComponentClass) {
        this.componentsByName[componentName.toString()] = ComponentClass;
    }
    getComponentClassForName(componentName) {
        return this.componentsByName[componentName.toString()];
    }
}
exports.Store = Store;
