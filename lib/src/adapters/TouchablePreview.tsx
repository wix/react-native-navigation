import * as React from 'react';
import * as PropTypes from 'prop-types';
import {
  View,
  Platform,
  findNodeHandle,
  TouchableOpacity,
  TouchableHighlight,
  TouchableNativeFeedback,
  TouchableWithoutFeedback,
  GestureResponderEvent,
  NativeTouchEvent,
  NativeSyntheticEvent,
} from 'react-native';

// Polyfill GestureResponderEvent type with additional `force` property (iOS)
interface NativeTouchEventWithForce extends NativeTouchEvent {
  force: number;
}
interface GestureResponderEventWithForce extends NativeSyntheticEvent<NativeTouchEventWithForce> {}

export interface Props {
  children?: React.ReactNode;
  touchableComponent?:
    | TouchableHighlight
    | TouchableOpacity
    | TouchableNativeFeedback
    | TouchableWithoutFeedback
    | React.ReactNode;
  onPress?: () => void;
  onPressIn?: (payload: { reactTag: number | null }) => void;
  onPeekIn?: () => void;
  onPeekOut?: () => void;
}

const PREVIEW_DELAY = 350;
const PREVIEW_MIN_FORCE = 0.1;
const PREVIEW_TIMEOUT = 1250;

export class TouchablePreview extends React.PureComponent<Props> {
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
    touchableComponent: TouchableWithoutFeedback,
  };

  static peeking = false;

  private timeout: number | undefined;
  private touchStartedAt: number = 0;
  private onRef = React.createRef<any>();
  onPress = () => {
    const { onPress } = this.props;

    if (typeof onPress !== 'function' || TouchablePreview.peeking) {
      return;
    }

    return onPress();
  };

  onPressIn = () => {
    if (Platform.OS === 'ios') {
      const { onPressIn } = this.props;

      if (!onPressIn) {
        return;
      }

      const reactTag = findNodeHandle(this.onRef.current);

      return onPressIn({ reactTag });
    }

    // Other platforms don't support 3D Touch Preview API
    return null;
  };

  onTouchStart = (event: GestureResponderEvent) => {
    // Store a timestamp of the initial touch start
    this.touchStartedAt = event.nativeEvent.timestamp;
  };

  onTouchMove = (event: GestureResponderEventWithForce) => {
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
    const Touchable =
      Platform.OS === 'ios' && touchableComponent instanceof TouchableNativeFeedback
        ? TouchableWithoutFeedback
        : (touchableComponent as typeof React.Component);

    // Wrap component with Touchable for handling platform touches
    // and a single react View for detecting force and timing.
    return (
      /**
       * @TODO (Jin Shin 25 June 2020)
       * Ignoring this for now so that it builds.
       */
      // @ts-ignore
      <Touchable ref={this.onRef} onPress={this.onPress} onPressIn={this.onPressIn} {...props}>
        <View
          onTouchStart={this.onTouchStart}
          onTouchMove={this.onTouchMove as (event: GestureResponderEvent) => void}
          onTouchEnd={this.onTouchEnd}
        >
          {children}
        </View>
      </Touchable>
    );
  }
}
