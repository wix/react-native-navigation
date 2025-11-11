"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Modal = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const RNNModalViewManager = (0, react_native_1.requireNativeComponent)('RNNModalViewManager');
const Container = (rnnProps) => {
    const viewRef = (0, react_1.useRef)(null);
    (0, react_1.useLayoutEffect)(() => {
        const windowWidth = react_native_1.Dimensions.get('window').width;
        const windowHeight = react_native_1.Dimensions.get('window').height;
        viewRef?.current?.setNativeProps({ width: windowWidth, height: windowHeight });
    }, []);
    return (react_1.default.createElement(react_native_1.View, { ref: viewRef, style: styles.container, collapsable: false }, rnnProps.children));
};
class Modal extends react_1.default.Component {
    static defaultProps = {
        transparent: false,
        blurOnUnmount: false,
        animationType: 'slide',
    };
    constructor(props) {
        super(props);
    }
    render() {
        const processed = this.proccessProps();
        if (this.props.visible) {
            return (react_1.default.createElement(RNNModalViewManager, { ...processed },
                react_1.default.createElement(Container, { ...this.props })));
        }
        else {
            return null;
        }
    }
    proccessProps() {
        const processed = { ...this.props, style: styles.modal };
        if (this.props.animationType === 'none') {
            processed.animation = {
                showModal: { enabled: false },
                dismissModal: { enabled: false },
            };
        }
        else {
            const isSlide = this.props.animationType === 'slide';
            processed.animation = {
                showModal: {
                    enter: isSlide ? showModalSlideEnterAnimations : showModalFadeEnterAnimations,
                },
                dismissModal: {
                    exit: isSlide ? dismissModalSlideExitAnimations : dismissModalFadeExitAnimations,
                },
            };
        }
        return processed;
    }
}
exports.Modal = Modal;
const height = Math.round(react_native_1.Dimensions.get('window').height);
const SCREEN_ANIMATION_DURATION = 500;
const showModalSlideEnterAnimations = {
    translationY: {
        from: height,
        to: 0,
        duration: SCREEN_ANIMATION_DURATION,
        interpolation: { type: 'decelerate' },
    },
};
const dismissModalSlideExitAnimations = {
    translationY: {
        from: 0,
        to: height,
        duration: SCREEN_ANIMATION_DURATION,
        interpolation: { type: 'decelerate' },
    },
};
const showModalFadeEnterAnimations = {
    alpha: {
        from: 0,
        to: 1,
        duration: SCREEN_ANIMATION_DURATION,
        interpolation: { type: 'decelerate' },
    },
};
const dismissModalFadeExitAnimations = {
    alpha: {
        from: 1,
        to: 0,
        duration: SCREEN_ANIMATION_DURATION,
        interpolation: { type: 'decelerate' },
    },
};
const styles = react_native_1.StyleSheet.create({
    modal: {
        position: 'absolute',
    },
    container: {
        top: 0,
        flex: 1,
    },
});
