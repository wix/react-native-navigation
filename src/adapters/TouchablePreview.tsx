import * as React from 'react';
import * as PropTypes from 'prop-types';
import {
  View,
  Platform,
  findNodeHandle,
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
  touchableComponent?: React.ElementType;
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
    touchableComponent: PropTypes.elementType,
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
  private static peekOwner: TouchablePreview | undefined;

  private timeout: number | undefined;
  private touchStartedAt: number = 0;
  private onRef = React.createRef<any>();

  componentWillUnmount() {
    clearTimeout(this.timeout);
    this.releasePeekOwnership();
  }

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
      TouchablePreview.peekOwner = this;

      if (typeof this.props.onPeekIn === 'function') {
        this.props.onPeekIn();
      }
    }
    //@ts-ignore
    this.timeout = setTimeout(this.onTouchEnd, PREVIEW_TIMEOUT);
  };

  onTouchEnd = () => {
    clearTimeout(this.timeout);
    this.releasePeekOwnership();

    if (typeof this.props.onPeekOut === 'function') {
      this.props.onPeekOut();
    }
  };

  render() {
    const { children, touchableComponent, ...props } = this.props;
    const requestedTouchable = touchableComponent ?? TouchableWithoutFeedback;

    // Default to TouchableWithoutFeedback for iOS if set to TouchableNativeFeedback
    const Touchable =
      Platform.OS === 'ios' && requestedTouchable === TouchableNativeFeedback
        ? TouchableWithoutFeedback
        : requestedTouchable;

    // Wrap component with Touchable for handling platform touches
    // and a single react View for detecting force and timing.
    return (
      <Touchable {...props} ref={this.onRef} onPress={this.onPress} onPressIn={this.onPressIn}>
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

  private releasePeekOwnership() {
    if (TouchablePreview.peekOwner === this) {
      TouchablePreview.peekOwner = undefined;
      TouchablePreview.peeking = false;
    }
  }
}
