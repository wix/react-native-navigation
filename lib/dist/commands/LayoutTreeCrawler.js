"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const _ = require("lodash");
const LayoutType_1 = require("./LayoutType");
class LayoutTreeCrawler {
    constructor(store, optionsProcessor) {
        this.store = store;
        this.optionsProcessor = optionsProcessor;
        this.crawl = this.crawl.bind(this);
    }
    crawl(node) {
        if (node.type === LayoutType_1.LayoutType.Component) {
            this.handleComponent(node);
        }
        this.optionsProcessor.processOptions(node.data.options);
        node.children.forEach(this.crawl);
    }
    handleComponent(node) {
        this.assertComponentDataName(node);
        this.savePropsToStore(node);
        this.applyStaticOptions(node);
        node.data.passProps = undefined;
    }
    savePropsToStore(node) {
        this.store.setPropsForId(node.id, node.data.passProps);
    }
    isComponentWithOptions(component) {
        return component.options !== undefined;
    }
    applyStaticOptions(node) {
        node.data.options = _.merge({}, this.staticOptionsIfPossible(node), node.data.options);
    }
    staticOptionsIfPossible(node) {
        const foundReactGenerator = this.store.getComponentClassForName(node.data.name);
        const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
        if (reactComponent && this.isComponentWithOptions(reactComponent)) {
            return _.isFunction(reactComponent.options) ? reactComponent.options(node.data.passProps || {}) : reactComponent.options;
        }
        return {};
    }
    assertComponentDataName(component) {
        if (!component.data.name) {
            throw new Error('Missing component data.name');
        }
    }
}
exports.LayoutTreeCrawler = LayoutTreeCrawler;
