import {
  Easing,
  SharedValue,
  interpolate,
  useSharedValue,
  Extrapolation,
  useDerivedValue,
  withTiming,
  runOnJS,
} from 'react-native-reanimated';
import { State, Gesture, PanGesture } from 'react-native-gesture-handler';
import { Dimensions } from 'react-native';

const SCREEN_HEIGHT = Dimensions.get('window').height;

export type GestureHandlerType = {
  onHandlerStateChange: (...args: unknown[]) => void;
  onGestureEvent: (...args: unknown[]) => void;
};

export interface DismissGestureState {
  gestureHandler: PanGesture;
  dismissAnimationProgress: SharedValue<number>;
  controlsOpacity: SharedValue<number>;
  cardBorderRadius: SharedValue<number>;
  viewScale: SharedValue<number>;
}

const GESTURE_HANDLER_RANGE: number = 20;
const GESTURE_HANDLER_FAIL_RANGE: [failOffsetXStart: number, failOffsetXEnd: number] = [-20, 20];

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

  const cardBorderRadius = useDerivedValue(() => interpolate(dismissAnimationProgress.value, [0, 1], [0, 30], Extrapolation.CLAMP), [dismissAnimationProgress]);

  const viewScale = useDerivedValue(() => interpolate(dismissAnimationProgress.value, [0, 1], [1, 0.8], Extrapolation.CLAMP), [dismissAnimationProgress]);

  const gestureHandler = Gesture.Pan()
    .maxPointers(1)
    .failOffsetX(GESTURE_HANDLER_FAIL_RANGE)
    .activeOffsetY(GESTURE_HANDLER_RANGE)
    .onChange(event => {
      if (!!enableGesture.value && event.state === State.ACTIVE) {
        dismissAnimationProgress.value = interpolate(event.translationY, [0, SCREEN_HEIGHT * 0.2], [0, 1]);
        controlsOpacity.value = interpolate(event.translationY, [0, SCREEN_HEIGHT * 0.1, SCREEN_HEIGHT * 0.2], [1, 0, 0]);

        if (dismissAnimationProgress.value >= 1) {
          enableGesture.value = 0;
          runOnJS(navigateBack)();
        }
      }
    })
    .onEnd(event => {
      if (!!enableGesture.value && event.state === State.END) {
        dismissAnimationProgress.value = withTiming(0, {
          duration: 200,
          easing: Easing.out(Easing.ease)
        });
        controlsOpacity.value = withTiming(1, {
          duration: 200,
          easing: Easing.linear
        })
      }
    });

  return {
    gestureHandler: gestureHandler,
    dismissAnimationProgress,
    controlsOpacity,
    cardBorderRadius,
    viewScale,
  };
}
