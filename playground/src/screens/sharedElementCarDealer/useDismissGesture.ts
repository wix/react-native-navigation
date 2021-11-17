import { usePanGestureHandler, useValue, timing } from 'react-native-redash';
import { useMemo } from 'react';
import Reanimated, {
  Extrapolate,
  useCode,
  cond,
  eq,
  set,
  Easing,
  greaterOrEq,
  call,
} from 'react-native-reanimated';
import { State } from 'react-native-gesture-handler';
import { Dimensions } from 'react-native';

const SCREEN_HEIGHT = Dimensions.get('window').height;

export type GestureHandlerType = {
  onHandlerStateChange: (...args: unknown[]) => void;
  onGestureEvent: (...args: unknown[]) => void;
};

export interface DismissGestureState {
  gestureHandler: GestureHandlerType;
  dismissAnimationProgress: Reanimated.Value<number>;
  controlsOpacity: Reanimated.Value<number>;
  cardBorderRadius: Reanimated.Node<number>;
  viewScale: Reanimated.Node<number>;
}

/**
 * Use a drag-down-to-dismiss gesture powered by native animations (Reanimated)
 * @param navigateBack The callback to invoke when the view has been dragged down and needs to navigate back to the last screen.
 * @returns An object of animation states (and the gesture handler which needs to be attached to the <PanGestureHandler> component)
 */
export default function useDismissGesture(navigateBack: () => void): DismissGestureState {
  // TODO: Maybe experiment with some translateX?
  const gestureHandler = usePanGestureHandler();
  const dismissAnimationProgress = useValue(0); // Animation from 0 -> 1, where 1 is dismiss.
  const controlsOpacity = useValue(0); // Extra opacity setting for controls to interactively fade out on drag down
  const enableGesture = useValue<0 | 1>(1); // Overrides gestureHandler.state to not trigger State.END cond() block when already released and navigating back

  const cardBorderRadius = useMemo(() => {
    return Reanimated.interpolateNode(dismissAnimationProgress, {
      inputRange: [0, 1],
      outputRange: [0, 30],
      extrapolate: Extrapolate.CLAMP,
    });
  }, [dismissAnimationProgress]);
  const viewScale = useMemo(() => {
    return Reanimated.interpolateNode(dismissAnimationProgress, {
      inputRange: [0, 1],
      outputRange: [1, 0.8],
      extrapolate: Extrapolate.CLAMP,
    });
  }, [dismissAnimationProgress]);

  useCode(
    () => [
      cond(eq(enableGesture, 1), [
        cond(eq(gestureHandler.state, State.ACTIVE), [
          set(
            dismissAnimationProgress,
            Reanimated.interpolateNode(gestureHandler.translation.y, {
              inputRange: [0, SCREEN_HEIGHT * 0.2],
              outputRange: [0, 1],
            })
          ),
          set(
            controlsOpacity,
            Reanimated.interpolateNode(gestureHandler.translation.y, {
              inputRange: [0, SCREEN_HEIGHT * 0.1, SCREEN_HEIGHT * 0.2],
              outputRange: [1, 0, 0],
            })
          ),
        ]),
        cond(eq(gestureHandler.state, State.END), [
          set(
            dismissAnimationProgress,
            timing({
              from: dismissAnimationProgress,
              to: 0,
              duration: 200,
              easing: Easing.out(Easing.ease),
            })
          ),
          set(
            controlsOpacity,
            timing({ from: controlsOpacity, to: 1, duration: 200, easing: Easing.linear })
          ),
        ]),
      ]),
      cond(greaterOrEq(dismissAnimationProgress, 1), [
        set(enableGesture, 0),
        call([], navigateBack),
      ]),
    ],
    [
      controlsOpacity,
      dismissAnimationProgress,
      enableGesture,
      gestureHandler.state,
      gestureHandler.translation.y,
      navigateBack,
    ]
  );

  return {
    gestureHandler: gestureHandler.gestureHandler,
    dismissAnimationProgress,
    controlsOpacity,
    cardBorderRadius,
    viewScale,
  };
}
