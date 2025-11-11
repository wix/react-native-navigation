"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SideMenuRight = exports.SideMenuCenter = exports.SideMenuLeft = exports.SideMenuRoot = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const connect_1 = require("../connect");
const LayoutComponent_1 = require("./LayoutComponent");
exports.SideMenuRoot = (0, connect_1.connect)(class extends react_1.Component {
    render() {
        const children = this.props.layoutNode.children;
        return children.map((child) => {
            return react_1.default.createElement(LayoutComponent_1.LayoutComponent, { key: child.nodeId, layoutNode: child });
        });
    }
});
class SideMenuComponent extends react_1.Component {
    render() {
        const children = this.props.layoutNode.children;
        const component = children[0];
        return react_1.default.createElement(LayoutComponent_1.LayoutComponent, { key: component.nodeId, layoutNode: component });
    }
}
exports.SideMenuLeft = (0, connect_1.connect)(SideMenuComponent);
exports.SideMenuCenter = (0, connect_1.connect)(SideMenuComponent);
exports.SideMenuRight = (0, connect_1.connect)(SideMenuComponent);
