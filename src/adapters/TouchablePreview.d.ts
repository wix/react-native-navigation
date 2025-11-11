import * as React from 'react';
import * as PropTypes from 'prop-types';
import { TouchableOpacity, TouchableHighlight, TouchableNativeFeedback, TouchableWithoutFeedback, GestureResponderEvent, NativeTouchEvent, NativeSyntheticEvent } from 'react-native';
interface NativeTouchEventWithForce extends NativeTouchEvent {
    force: number;
}
interface GestureResponderEventWithForce extends NativeSyntheticEvent<NativeTouchEventWithForce> {
}
export interface Props {
    children?: React.ReactNode;
    touchableComponent?: typeof TouchableHighlight | typeof TouchableOpacity | TouchableNativeFeedback | TouchableWithoutFeedback | React.ReactNode;
    onPress?: () => void;
    onPressIn?: (payload: {
        reactTag: number | null;
    }) => void;
    onPeekIn?: () => void;
    onPeekOut?: () => void;
}
export declare class TouchablePreview extends React.PureComponent<Props> {
    static propTypes: {
        children: PropTypes.Requireable<PropTypes.ReactNodeLike>;
        touchableComponent: PropTypes.Requireable<(...args: any[]) => any>;
        onPress: PropTypes.Requireable<(...args: any[]) => any>;
        onPressIn: PropTypes.Requireable<(...args: any[]) => any>;
        onPeekIn: PropTypes.Requireable<(...args: any[]) => any>;
        onPeekOut: PropTypes.Requireable<(...args: any[]) => any>;
        label: PropTypes.Requireable<string>;
    };
    static defaultProps: {
        touchableComponent: typeof TouchableWithoutFeedback;
    };
    static peeking: boolean;
    private timeout;
    private touchStartedAt;
    private onRef;
    onPress: () => void;
    onPressIn: () => void | null;
    onTouchStart: (event: GestureResponderEvent) => void;
    onTouchMove: (event: GestureResponderEventWithForce) => void;
    onTouchEnd: () => void;
    render(): React.JSX.Element;
}
export {};
