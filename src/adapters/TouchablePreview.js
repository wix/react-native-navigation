"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.TouchablePreview = void 0;
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const PropTypes = tslib_1.__importStar(require("prop-types"));
const react_native_1 = require("react-native");
const PREVIEW_DELAY = 350;
const PREVIEW_MIN_FORCE = 0.1;
const PREVIEW_TIMEOUT = 1250;
class TouchablePreview extends React.PureComponent {
    static propTypes = {
        children: PropTypes.node,
        touchableComponent: PropTypes.func,
        onPress: PropTypes.func,
        onPressIn: PropTypes.func,
        onPeekIn: PropTypes.func,
        onPeekOut: PropTypes.func,
        label: PropTypes.string,
    };
    static defaultProps = {
        touchableComponent: react_native_1.TouchableWithoutFeedback,
    };
    static peeking = false;
    timeout;
    touchStartedAt = 0;
    onRef = React.createRef();
    onPress = () => {
        const { onPress } = this.props;
        if (typeof onPress !== 'function' || TouchablePreview.peeking) {
            return;
        }
        return onPress();
    };
    onPressIn = () => {
        if (react_native_1.Platform.OS === 'ios') {
            const { onPressIn } = this.props;
            if (!onPressIn) {
                return;
            }
            const reactTag = (0, react_native_1.findNodeHandle)(this.onRef.current);
            return onPressIn({ reactTag });
        }
        // Other platforms don't support 3D Touch Preview API
        return null;
    };
    onTouchStart = (event) => {
        // Store a timestamp of the initial touch start
        this.touchStartedAt = event.nativeEvent.timestamp;
    };
    onTouchMove = (event) => {
        clearTimeout(this.timeout);
        const { force, timestamp } = event.nativeEvent;
        const diff = timestamp - this.touchStartedAt;
        if (force > PREVIEW_MIN_FORCE && diff > PREVIEW_DELAY) {
            TouchablePreview.peeking = true;
            if (typeof this.props.onPeekIn === 'function') {
                this.props.onPeekIn();
            }
        }
        //@ts-ignore
        this.timeout = setTimeout(this.onTouchEnd, PREVIEW_TIMEOUT);
    };
    onTouchEnd = () => {
        clearTimeout(this.timeout);
        TouchablePreview.peeking = false;
        if (typeof this.props.onPeekOut === 'function') {
            this.props.onPeekOut();
        }
    };
    render() {
        const { children, touchableComponent, ...props } = this.props;
        // Default to TouchableWithoutFeedback for iOS if set to TouchableNativeFeedback
        const Touchable = react_native_1.Platform.OS === 'ios' && touchableComponent instanceof react_native_1.TouchableNativeFeedback
            ? react_native_1.TouchableWithoutFeedback
            : touchableComponent;
        // Wrap component with Touchable for handling platform touches
        // and a single react View for detecting force and timing.
        return (
        /**
         * @TODO (Jin Shin 25 June 2020)
         * Ignoring this for now so that it builds.
         */
        // @ts-ignore
        React.createElement(Touchable, { ...props, ref: this.onRef, onPress: this.onPress, onPressIn: this.onPressIn },
            React.createElement(react_native_1.View, { onTouchStart: this.onTouchStart, onTouchMove: this.onTouchMove, onTouchEnd: this.onTouchEnd }, children)));
    }
}
exports.TouchablePreview = TouchablePreview;
