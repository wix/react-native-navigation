import { useMemo } from 'react';
import {
  Easing,
  Extrapolate,
  interpolate,
  SharedValue,
  useAnimatedGestureHandler,
  useSharedValue,
  withTiming,
} from 'react-native-reanimated';
import {
  GestureHandlerGestureEvent,
  PanGestureHandlerGestureEvent,
} from 'react-native-gesture-handler';
import { Dimensions } from 'react-native';

const SCREEN_HEIGHT = Dimensions.get('window').height;

type OnGestureEvent<T extends GestureHandlerGestureEvent> = (event: T) => void;

export interface DismissGestureState {
  onGestureEvent: OnGestureEvent<PanGestureHandlerGestureEvent>;
  dismissAnimationProgress: SharedValue<number>;
  controlsOpacity: SharedValue<number>;
  cardBorderRadius: number;
  viewScale: number;
}

/**
 * Use a drag-down-to-dismiss gesture powered by native animations (Reanimated)
 * @param navigateBack The callback to invoke when the view has been dragged down and needs to navigate back to the last screen.
 * @returns An object of animation states (and the gesture handler which needs to be attached to the <PanGestureHandler> component)
 */
export default function useDismissGesture(navigateBack: () => void): DismissGestureState {
  // TODO: Maybe experiment with some translateX?
  const dismissAnimationProgress = useSharedValue(0); // Animation from 0 -> 1, where 1 is dismiss.
  const controlsOpacity = useSharedValue(0); // Extra opacity setting for controls to interactively fade out on drag down
  const enableGesture = useSharedValue<0 | 1>(1); // Overrides gestureHandler.state to not trigger State.END cond() block when already released and navigating back

  const cardBorderRadius = useMemo(() => {
    return interpolate(dismissAnimationProgress.value, [0, 1], [0, 30], Extrapolate.CLAMP);
  }, [dismissAnimationProgress]);

  const viewScale = useMemo(() => {
    return interpolate(dismissAnimationProgress.value, [0, 1], [1, 0.8], Extrapolate.CLAMP);
  }, [dismissAnimationProgress]);

  // TODO: Check that it works correctly
  const onGestureEvent = useAnimatedGestureHandler(
    {
      onStart: () => {
        if (dismissAnimationProgress.value >= 1) {
          enableGesture.value = 0;
          navigateBack();
        }
      },
      onActive: ({ translationY }) => {
        if (enableGesture.value === 1) {
          dismissAnimationProgress.value = interpolate(
            translationY,
            [0, SCREEN_HEIGHT * 0.2],
            [0, 1]
          );
          controlsOpacity.value = interpolate(
            translationY,
            [0, SCREEN_HEIGHT * 0.1, SCREEN_HEIGHT * 0.2],
            [1, 0, 0]
          );
        }
      },
      onEnd: () => {
        if (enableGesture.value === 1) {
          dismissAnimationProgress.value = withTiming(0, {
            duration: 200,
            easing: Easing.out(Easing.ease),
          });
          controlsOpacity.value = withTiming(1, { duration: 200, easing: Easing.linear });
        }
      },
    },
    [controlsOpacity, dismissAnimationProgress, enableGesture, navigateBack]
  );

  return {
    onGestureEvent: onGestureEvent,
    dismissAnimationProgress,
    controlsOpacity,
    cardBorderRadius,
    viewScale,
  };
}
