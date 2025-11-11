"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutTreeCrawler = void 0;
const LayoutType_1 = require("./LayoutType");
class LayoutTreeCrawler {
    store;
    optionsProcessor;
    constructor(store, optionsProcessor) {
        this.store = store;
        this.optionsProcessor = optionsProcessor;
        this.crawl = this.crawl.bind(this);
    }
    crawl(node, commandName) {
        if (node.type === LayoutType_1.LayoutType.Component) {
            this.handleComponent(node);
        }
        const componentProps = this.store.getPropsForId(node.id) || undefined;
        this.optionsProcessor.processOptions(commandName, node.data.options, componentProps);
        node.children.forEach((value) => this.crawl(value, commandName));
    }
    handleComponent(node) {
        this.assertComponentDataName(node);
        this.savePropsToStore(node);
        node.data.passProps = undefined;
    }
    savePropsToStore(node) {
        this.store.setPendingProps(node.id, node.data.passProps);
    }
    assertComponentDataName(component) {
        if (!component.data.name) {
            throw new Error('Missing component data.name');
        }
    }
}
exports.LayoutTreeCrawler = LayoutTreeCrawler;
