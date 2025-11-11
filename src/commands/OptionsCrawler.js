"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.OptionsCrawler = void 0;
const tslib_1 = require("tslib");
const merge_1 = tslib_1.__importDefault(require("lodash/merge"));
const isFunction_1 = tslib_1.__importDefault(require("lodash/isFunction"));
const LayoutType_1 = require("./LayoutType");
class OptionsCrawler {
    store;
    uniqueIdProvider;
    constructor(store, uniqueIdProvider) {
        this.store = store;
        this.uniqueIdProvider = uniqueIdProvider;
        this.crawl = this.crawl.bind(this);
    }
    crawl(api) {
        if (!api)
            return;
        if (api.topTabs) {
            this.topTabs(api.topTabs);
        }
        else if (api.sideMenu) {
            return this.sideMenu(api.sideMenu);
        }
        else if (api.bottomTabs) {
            return this.bottomTabs(api.bottomTabs);
        }
        else if (api.stack) {
            return this.stack(api.stack);
        }
        else if (api.component) {
            return this.component(api.component);
        }
        else if (api.splitView) {
            return this.splitView(api.splitView);
        }
    }
    topTabs(api) {
        api.children?.map(this.crawl);
    }
    sideMenu(sideMenu) {
        this.crawl(sideMenu.center);
        this.crawl(sideMenu.left);
        this.crawl(sideMenu.right);
    }
    bottomTabs(bottomTabs) {
        bottomTabs.children?.map(this.crawl);
    }
    stack(stack) {
        stack.children?.map(this.crawl);
    }
    splitView(splitView) {
        splitView.detail && this.crawl(splitView.detail);
        splitView.master && this.crawl(splitView.master);
    }
    component(component) {
        this.applyComponentId(component);
        this.applyStaticOptions(component);
    }
    applyComponentId(component) {
        component.id = component.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.Component);
    }
    isComponentWithOptions(component) {
        return component.options !== undefined;
    }
    applyStaticOptions(layout) {
        const staticOptions = this.staticOptionsIfPossible(layout);
        layout.options = (0, merge_1.default)({}, staticOptions, layout.options);
    }
    staticOptionsIfPossible(layout) {
        const foundReactGenerator = this.store.getComponentClassForName(layout.name);
        const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
        if (reactComponent && this.isComponentWithOptions(reactComponent)) {
            return (0, isFunction_1.default)(reactComponent.options)
                ? reactComponent.options({ componentId: layout.id, ...layout.passProps } || {})
                : reactComponent.options;
        }
        return {};
    }
}
exports.OptionsCrawler = OptionsCrawler;
