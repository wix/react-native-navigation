"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.BottomTabs = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const LayoutComponent_1 = require("./LayoutComponent");
const connect_1 = require("../connect");
exports.BottomTabs = (0, connect_1.connect)(class extends react_1.Component {
    render() {
        return this.props.layoutNode.children.map((child) => {
            return react_1.default.createElement(LayoutComponent_1.LayoutComponent, { key: child.nodeId, layoutNode: child });
        });
    }
});
