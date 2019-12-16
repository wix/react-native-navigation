"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const PropTypes = tslib_1.__importStar(require("prop-types"));
const react_native_1 = require("react-native");
const PREVIEW_DELAY = 350;
const PREVIEW_MIN_FORCE = 0.1;
const PREVIEW_TIMEOUT = 1250;
class TouchablePreview extends React.PureComponent {
    constructor() {
        super(...arguments);
        this.touchStartedAt = 0;
        this.onRef = React.createRef();
        this.onPress = () => {
            const { onPress } = this.props;
            if (typeof onPress !== 'function' || TouchablePreview.peeking) {
                return;
            }
            return onPress();
        };
        this.onPressIn = () => {
            if (react_native_1.Platform.OS === 'ios') {
                const { onPressIn } = this.props;
                if (!onPressIn) {
                    return;
                }
                const reactTag = react_native_1.findNodeHandle(this.onRef.current);
                return onPressIn({ reactTag });
            }
            // Other platforms don't support 3D Touch Preview API
            return null;
        };
        this.onTouchStart = (event) => {
            // Store a timstamp of the initial touch start
            this.touchStartedAt = event.nativeEvent.timestamp;
        };
        this.onTouchMove = (event) => {
            clearTimeout(this.timeout);
            const { force, timestamp } = event.nativeEvent;
            const diff = timestamp - this.touchStartedAt;
            if (force > PREVIEW_MIN_FORCE && diff > PREVIEW_DELAY) {
                TouchablePreview.peeking = true;
                if (typeof this.props.onPeekIn === 'function') {
                    this.props.onPeekIn();
                }
            }
            this.timeout = setTimeout(this.onTouchEnd, PREVIEW_TIMEOUT);
        };
        this.onTouchEnd = () => {
            clearTimeout(this.timeout);
            TouchablePreview.peeking = false;
            if (typeof this.props.onPeekOut === 'function') {
                this.props.onPeekOut();
            }
        };
    }
    render() {
        const { children, touchableComponent, onPress, onPressIn, ...props } = this.props;
        // Default to TouchableWithoutFeedback for iOS if set to TouchableNativeFeedback
        const Touchable = (react_native_1.Platform.OS === 'ios' && touchableComponent instanceof react_native_1.TouchableNativeFeedback) ?
            react_native_1.TouchableWithoutFeedback :
            touchableComponent;
        // Wrap component with Touchable for handling platform touches
        // and a single react View for detecting force and timing.
        return (<Touchable ref={this.onRef} onPress={this.onPress} onPressIn={this.onPressIn} {...props}>
        <react_native_1.View onTouchStart={this.onTouchStart} onTouchMove={this.onTouchMove} onTouchEnd={this.onTouchEnd}>
          {children}
        </react_native_1.View>
      </Touchable>);
    }
}
TouchablePreview.propTypes = {
    children: PropTypes.node,
    touchableComponent: PropTypes.func,
    onPress: PropTypes.func,
    onPressIn: PropTypes.func,
    onPeekIn: PropTypes.func,
    onPeekOut: PropTypes.func,
};
TouchablePreview.defaultProps = {
    touchableComponent: react_native_1.TouchableWithoutFeedback,
};
TouchablePreview.peeking = false;
exports.TouchablePreview = TouchablePreview;
