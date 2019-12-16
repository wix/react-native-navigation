"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const PropTypes = tslib_1.__importStar(require("prop-types"));
const react_native_1 = require("react-native");
class SharedElement extends React.Component {
    render() {
        return <RnnSharedElement {...this.props}/>;
    }
}
SharedElement.propTypes = {
    elementId: PropTypes.string.isRequired,
    resizeMode: PropTypes.string
};
SharedElement.defaultProps = {
    resizeMode: ''
};
exports.SharedElement = SharedElement;
const RnnSharedElement = react_native_1.requireNativeComponent('RNNElement', SharedElement, {
    nativeOnly: { nativeID: true }
});
