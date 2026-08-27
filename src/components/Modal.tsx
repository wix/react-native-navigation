import React from 'react';
import {
  requireNativeComponent,
  View,
  ViewProps,
  StyleSheet,
  useWindowDimensions,
} from 'react-native';
import { AnimationOptions, ViewAnimationOptions } from '../interfaces/Options';

export interface RNNModalProps extends ViewProps {
  visible: boolean;
  transparent: boolean;
  blurOnUnmount: boolean;
  animationType: 'none' | 'fade' | 'slide';
  onShow?: () => any;
  onRequestClose: () => any;
}

interface AnimatedModalProps extends RNNModalProps {
  animation?: AnimationOptions;
}

const RNNModalViewManager = requireNativeComponent<AnimatedModalProps>('RNNModalViewManager');

const ModalContent = (props: RNNModalProps) => {
  const { width, height } = useWindowDimensions();
  const animation: AnimationOptions =
    props.animationType === 'none'
      ? { showModal: { enabled: false }, dismissModal: { enabled: false } }
      : {
          showModal: {
            enter:
              props.animationType === 'slide'
                ? slideAnimation(Math.round(height), 0)
                : showModalFadeEnterAnimations,
          },
          dismissModal: {
            exit:
              props.animationType === 'slide'
                ? slideAnimation(0, Math.round(height))
                : dismissModalFadeExitAnimations,
          },
        };

  return (
    <RNNModalViewManager {...props} style={styles.modal} animation={animation}>
      <View style={{ width, height }} collapsable={false}>
        {props.children}
      </View>
    </RNNModalViewManager>
  );
};

export class Modal extends React.Component<RNNModalProps> {
  static defaultProps = {
    transparent: false,
    blurOnUnmount: false,
    animationType: 'slide',
  };

  render() {
    return this.props.visible ? <ModalContent {...this.props} /> : null;
  }
}

const SCREEN_ANIMATION_DURATION = 500;

function slideAnimation(from: number, to: number): ViewAnimationOptions {
  return {
    translationY: {
      from,
      to,
      duration: SCREEN_ANIMATION_DURATION,
      interpolation: { type: 'decelerate' },
    },
  };
}

const showModalFadeEnterAnimations: ViewAnimationOptions = {
  alpha: {
    from: 0,
    to: 1,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
};

const dismissModalFadeExitAnimations: ViewAnimationOptions = {
  alpha: {
    from: 1,
    to: 0,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
};

const styles = StyleSheet.create({
  modal: {
    position: 'absolute',
  },
});
