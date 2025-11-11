"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Application = void 0;
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const connect_1 = require("./connect");
exports.Application = (0, connect_1.connect)(class extends React.Component {
    constructor(props) {
        super(props);
        props.entryPoint();
    }
    render() {
        const { LayoutComponent } = require('./Components/LayoutComponent');
        const { LayoutStore } = require('./Stores/LayoutStore');
        const { Modals } = require('./Components/Modals');
        const { Overlays } = require('./Components/Overlays');
        return (React.createElement(react_native_1.View, { testID: 'Application' },
            React.createElement(LayoutComponent, { layoutNode: LayoutStore.getLayout() }),
            React.createElement(Modals, null),
            React.createElement(Overlays, null)));
    }
});
