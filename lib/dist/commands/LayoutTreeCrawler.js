"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const merge_1 = tslib_1.__importDefault(require("lodash/merge"));
const isFunction_1 = tslib_1.__importDefault(require("lodash/isFunction"));
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
        this.store.updateProps(node.id, node.data.passProps);
    }
    isComponentWithOptions(component) {
        return component.options !== undefined;
    }
    applyStaticOptions(node) {
        node.data.options = merge_1.default({}, this.staticOptionsIfPossible(node), node.data.options);
    }
    staticOptionsIfPossible(node) {
        const foundReactGenerator = this.store.getComponentClassForName(node.data.name);
        const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
        if (reactComponent && this.isComponentWithOptions(reactComponent)) {
            return isFunction_1.default(reactComponent.options) ? reactComponent.options(node.data.passProps || {}) : reactComponent.options;
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
