"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Stack = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const LayoutComponent_1 = require("./LayoutComponent");
const connect_1 = require("../connect");
exports.Stack = (0, connect_1.connect)(class extends react_1.Component {
    render() {
        const children = this.props.layoutNode.children;
        return children.map((child, i) => {
            return react_1.default.createElement(LayoutComponent_1.LayoutComponent, { key: child.nodeId, layoutNode: child, backButton: i > 0 });
        });
    }
});
