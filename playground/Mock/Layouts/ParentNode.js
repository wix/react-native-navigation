"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const LayoutStore_1 = require("../Stores/LayoutStore");
const LayoutNodeFactory_1 = tslib_1.__importDefault(require("./LayoutNodeFactory"));
const Node_1 = tslib_1.__importDefault(require("./Node"));
class ParentNode extends Node_1.default {
    children;
    constructor(layout, type, parentNode) {
        super(layout, type, parentNode);
        this.children = layout.children.map((childLayout) => LayoutNodeFactory_1.default.create(childLayout, this));
    }
    componentDidMount() { }
    componentDidAppear() {
        this.getVisibleLayout().componentDidAppear();
    }
    componentDidDisappear() {
        this.getVisibleLayout().componentDidDisappear();
    }
    getVisibleLayout() {
        return this.children[this.children.length - 1].getVisibleLayout();
    }
    getTopParent() {
        if (this.parentNode)
            return this.parentNode.getTopParent();
        return this;
    }
    applyOptions(_options) {
        this.parentNode?.applyOptions(_options);
    }
    mergeOptions(options) {
        this.data.options = lodash_1.default.mergeWith(this.data.options, options, (objValue, srcValue, key) => {
            if (lodash_1.default.isArray(objValue)) {
                if (key === 'rightButtons' || key === 'leftButtons') {
                    this.buttonsChanged(objValue, srcValue);
                }
                return srcValue;
            }
            if (key === 'title' && srcValue.component) {
                this.titleChanged(objValue, srcValue);
            }
        });
        this.parentNode?.mergeOptions(options);
    }
    buttonsChanged(_oldButtons, _newButtons) { }
    titleChanged(_oldComponent, _newComponent) { }
    resolveOptions() {
        const options = lodash_1.default.merge(lodash_1.default.cloneDeep(this.data.options), this.getVisibleLayout().data.options);
        return lodash_1.default.merge(lodash_1.default.cloneDeep(LayoutStore_1.LayoutStore.getDefaultOptions()), options);
    }
    getStack() {
        if (this.type === 'Stack') {
            return this;
        }
        else if (this.parentNode) {
            return this.parentNode.getStack();
        }
        return undefined;
    }
    getBottomTabs() {
        if (this.type === 'BottomTabs') {
            return this;
        }
        else if (this.parentNode) {
            return this.parentNode.getBottomTabs();
        }
        return undefined;
    }
}
exports.default = ParentNode;
