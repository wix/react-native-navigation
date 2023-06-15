import React, { useCallback, useMemo } from 'react';
import {
  GestureResponderEvent,
  TouchableWithoutFeedback,
  TouchableWithoutFeedbackProps,
  View,
} from 'react-native';
import Reanimated, { useSharedValue, withSpring, WithSpringConfig } from 'react-native-reanimated';

export interface PressableScaleProps
  extends TouchableWithoutFeedbackProps,
    Partial<Omit<WithSpringConfig, 'mass'>> {
  children: React.ReactNode;
  /**
   * The value to scale to when the Pressable is being pressed.
   * @default 0.95
   */
  activeScale?: number;

  /**
   * The weight physics of this button
   * @default 'heavy'
   */
  weight?: 'light' | 'medium' | 'heavy';
}

const ReanimatedTouchableWithoutFeedback = Reanimated.createAnimatedComponent(
  TouchableWithoutFeedback
);

/**
 * A Pressable that scales down when pressed. Uses the JS Pressability API.
 */
export default function PressableScale(props: PressableScaleProps): React.ReactElement {
  const {
    activeScale = 0.95,
    weight = 'heavy',
    damping = 15,
    stiffness = 150,
    overshootClamping = true,
    restSpeedThreshold = 0.001,
    restDisplacementThreshold = 0.001,
    style,
    onPressIn: _onPressIn,
    onPressOut: _onPressOut,
    delayPressIn = 0,
    children,
    ...passThroughProps
  } = props;

  const mass = useMemo(() => {
    switch (weight) {
      case 'light':
        return 0.15;
      case 'medium':
        return 0.2;
      case 'heavy':
      default:
        return 0.3;
    }
  }, [weight]);

  const scale = useSharedValue(1);
  const touchableStyle = useMemo(() => ({ transform: [{ scale: scale.value }] }), [scale]);
  const springConfig = useMemo<WithSpringConfig>(
    () => ({
      damping,
      mass,
      stiffness,
      overshootClamping,
      restSpeedThreshold,
      restDisplacementThreshold,
    }),
    [damping, mass, overshootClamping, restDisplacementThreshold, restSpeedThreshold, stiffness]
  );

  const onPressIn = useCallback(
    (event: GestureResponderEvent) => {
      scale.value = withSpring(activeScale, springConfig);
      _onPressIn?.(event);
    },
    [_onPressIn, activeScale, scale, springConfig]
  );
  const onPressOut = useCallback(
    (event: GestureResponderEvent) => {
      scale.value = withSpring(1, springConfig);
      _onPressOut?.(event);
    },
    [_onPressOut, scale, springConfig]
  );

  return (
    <ReanimatedTouchableWithoutFeedback
      delayPressIn={delayPressIn}
      onPressIn={onPressIn}
      onPressOut={onPressOut}
      style={touchableStyle}
      {...passThroughProps}
    >
      <View style={style}>{children}</View>
    </ReanimatedTouchableWithoutFeedback>
  );
}
