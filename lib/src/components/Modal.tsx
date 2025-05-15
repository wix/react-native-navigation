import React, { useLayoutEffect, useRef } from 'react';
import { requireNativeComponent, View, ViewProps, StyleSheet, Dimensions } from 'react-native';
import { AnimationOptions, ViewAnimationOptions } from 'react-native-navigation/interfaces/Options';

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

const RNNModalViewManager = requireNativeComponent('RNNModalViewManager');

const Container = (rnnProps: RNNModalProps) => {
  const viewRef = useRef<View>(null);

  useLayoutEffect(() => {
    const windowWidth = Dimensions.get('window').width;
    const windowHeight = Dimensions.get('window').height;
    viewRef?.current?.setNativeProps({ width: windowWidth, height: windowHeight });
  }, []);

  return (
    <View ref={viewRef} style={styles.container} collapsable={false}>
      {rnnProps.children}
    </View>
  );
};

export class Modal extends React.Component<RNNModalProps> {
  static defaultProps = {
    transparent: false,
    blurOnUnmount: false,
    animationType: 'slide',
  };

  constructor(props: RNNModalProps) {
    super(props);
  }

  render() {
    const processed = this.proccessProps();
    if (this.props.visible) {
      return (
        <RNNModalViewManager {...processed}>
          <Container {...this.props} />
        </RNNModalViewManager>
      );
    } else {
      return null;
    }
  }

  private proccessProps() {
    const processed: AnimatedModalProps = { ...this.props, style: styles.modal };
    if (this.props.animationType === 'none') {
      processed.animation = {
        showModal: { enabled: false },
        dismissModal: { enabled: false },
      };
    } else {
      const isSlide = this.props.animationType === 'slide';
      processed.animation = {
        showModal: {
          enter: isSlide ? showModalSlideEnterAnimations : showModalFadeEnterAnimations,
        },
        dismissModal: {
          exit: isSlide ? dismissModalSlideExitAnimations : dismissModalFadeExitAnimations,
        },
      };
    }
    return processed;
  }
}

const height = Math.round(Dimensions.get('window').height);
const SCREEN_ANIMATION_DURATION = 500;
const showModalSlideEnterAnimations: ViewAnimationOptions = {
  translationY: {
    from: height,
    to: 0,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
};

const dismissModalSlideExitAnimations: ViewAnimationOptions = {
  translationY: {
    from: 0,
    to: height,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
};
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
  container: {
    top: 0,
    flex: 1,
  },
});
