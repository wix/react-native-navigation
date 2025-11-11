"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const BottomTabsNode_1 = tslib_1.__importDefault(require("./BottomTabsNode"));
const ComponentNode_1 = tslib_1.__importDefault(require("./ComponentNode"));
const StackNode_1 = tslib_1.__importDefault(require("./StackNode"));
const SideMenu_1 = tslib_1.__importStar(require("./SideMenu"));
class LayoutNodeFactory {
    static create(layout, parentNode) {
        switch (layout.type) {
            case 'Component':
                return new ComponentNode_1.default(layout, parentNode);
            case 'Stack':
                return new StackNode_1.default(layout, parentNode);
            case 'SideMenuRoot':
                return new SideMenu_1.default(layout, parentNode);
            case 'SideMenuLeft':
                return new SideMenu_1.SideMenuLeftNode(layout, parentNode);
            case 'SideMenuCenter':
                return new SideMenu_1.SideMenuCenterNode(layout, parentNode);
            case 'SideMenuRight':
                return new SideMenu_1.SideMenuRightNode(layout, parentNode);
            default: // TODO Undo
            case 'BottomTabs':
                return new BottomTabsNode_1.default(layout, parentNode);
        }
    }
}
exports.default = LayoutNodeFactory;
