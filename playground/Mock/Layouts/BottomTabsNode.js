"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const layoutActions_1 = require("../actions/layoutActions");
const ParentNode_1 = tslib_1.__importDefault(require("./ParentNode"));
class BottomTabsNode extends ParentNode_1.default {
    selectedIndex;
    constructor(layout, parentNode) {
        super(layout, 'BottomTabs', parentNode);
        this.selectedIndex = layout.data?.options?.bottomTabs?.currentTabIndex || 0;
    }
    mergeOptions(_options) {
        super.mergeOptions(_options);
        const { options } = this.data;
        if (options.bottomTabs?.currentTabIndex) {
            this.selectedIndex = options.bottomTabs?.currentTabIndex;
            (0, layoutActions_1.switchTabByIndex)(this, this.selectedIndex);
        }
        if (options.bottomTabs?.currentTabId) {
            const index = lodash_1.default.findIndex(this.children, (child) => child.nodeId === options?.bottomTabs?.currentTabId);
            if (index !== -1)
                this.selectedIndex = index;
            (0, layoutActions_1.switchTabByIndex)(this, this.selectedIndex);
        }
    }
    getVisibleLayout() {
        return this.children[this.selectedIndex].getVisibleLayout();
    }
}
exports.default = BottomTabsNode;
