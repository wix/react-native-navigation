import React, { useMemo } from 'react';
import Animated from 'react-native-reanimated';
import { ViewProps } from 'react-native';
import { PanGestureHandler } from 'react-native-gesture-handler';
import { DismissGestureState } from './useDismissGesture';

export interface DismissableViewProps extends ViewProps {
  dismissGestureState: DismissGestureState;
  children?: React.ReactNode;
}

const GESTURE_HANDLER_RANGE = 20;
const GESTURE_HANDLER_FAIL_RANGE = [-20, 20];

export default function DismissableView(props: DismissableViewProps): JSX.Element {
  const { dismissGestureState, style, ...passThroughProps } = props;

  const viewStyle = useMemo(
    () => [
      style,
      {
        transform: [{ scale: dismissGestureState.viewScale }],
        borderRadius: dismissGestureState.cardBorderRadius,
      },
    ],
    [dismissGestureState.cardBorderRadius, dismissGestureState.viewScale, style]
  );

  return (
    <PanGestureHandler
      {...dismissGestureState.gestureHandler}
      maxPointers={1}
      failOffsetX={GESTURE_HANDLER_FAIL_RANGE}
      activeOffsetY={GESTURE_HANDLER_RANGE}
    >
      <Animated.View style={viewStyle} {...passThroughProps} />
    </PanGestureHandler>
  );
}
