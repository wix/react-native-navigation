import React, { ReactElement } from 'react';
import Reanimated, { useAnimatedStyle } from 'react-native-reanimated';
import { StyleSheet, ViewProps } from 'react-native';
import { GestureDetector, PanGestureHandler } from 'react-native-gesture-handler';
import { DismissGestureState } from './useDismissGesture';
import { BlurView } from '@react-native-community/blur';

export interface DismissableViewProps extends ViewProps {
  dismissGestureState: DismissGestureState;
  children?: React.ReactNode;
}

export default function DismissableView(props: DismissableViewProps): ReactElement {
  const { dismissGestureState, style, ...passThroughProps } = props;

  const viewStyle = useAnimatedStyle(
    () => ({
      transform: [{ scale: dismissGestureState.viewScale.value }],
      borderRadius: dismissGestureState.cardBorderRadius.value,
    }),
    [dismissGestureState.cardBorderRadius, dismissGestureState.viewScale, style]
  );

  return (
    <>
      <BlurView nativeID='blur' style={StyleSheet.absoluteFill} blurAmount={25} blurRadius={25} blurType='light' />
      <GestureDetector gesture={dismissGestureState.gestureHandler}>
        <Reanimated.View style={[style, viewStyle]} {...passThroughProps} />
      </GestureDetector>
    </>
  );
}
