"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutComponent = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const BottomTabs_1 = require("./BottomTabs");
const ComponentScreen_1 = require("./ComponentScreen");
const Stack_1 = require("./Stack");
const SideMenu_1 = require("./SideMenu");
const LayoutComponent = class extends react_1.Component {
    render() {
        switch (this.props.layoutNode.type) {
            case 'BottomTabs':
                return react_1.default.createElement(BottomTabs_1.BottomTabs, { layoutNode: this.props.layoutNode });
            case 'Stack':
                return react_1.default.createElement(Stack_1.Stack, { layoutNode: this.props.layoutNode });
            case 'Component':
                return react_1.default.createElement(ComponentScreen_1.ComponentScreen, { layoutNode: this.props.layoutNode });
            case 'SideMenuRoot':
                return react_1.default.createElement(SideMenu_1.SideMenuRoot, { layoutNode: this.props.layoutNode });
            case 'SideMenuLeft':
                return react_1.default.createElement(SideMenu_1.SideMenuLeft, { layoutNode: this.props.layoutNode });
            case 'SideMenuCenter':
                return react_1.default.createElement(SideMenu_1.SideMenuCenter, { layoutNode: this.props.layoutNode });
            case 'SideMenuRight':
                return react_1.default.createElement(SideMenu_1.SideMenuRight, { layoutNode: this.props.layoutNode });
        }
        return react_1.default.createElement(react_native_1.View, null);
    }
    componentDidCatch(error, errorInfo) {
        const err = new Error(`Error while trying to render layout ${this.props.layoutNode.nodeId} of type ${this.props.layoutNode.type}: ${error}\n${errorInfo?.componentStack}`);
        err.cause = error;
        throw err;
    }
};
exports.LayoutComponent = LayoutComponent;
