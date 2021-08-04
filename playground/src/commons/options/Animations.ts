import {
  AnimationOptions,
  ViewAnimationOptions,
  OptionsAnimationPropertyConfig,
  EnterExitAnimationOptions,
} from 'react-native-navigation';
import { Dimensions } from 'react-native';
import flags from '../../flags';

const { useCustomAnimations, useSlowOpenScreenAnimations, useSlideAnimation } = flags;
const height = Math.round(Dimensions.get('window').height);
const width = Math.round(Dimensions.get('window').width);
const SCREEN_ANIMATION_DURATION = 500 * (useSlowOpenScreenAnimations ? 2.5 : 1);

const baseAnimation: OptionsAnimationPropertyConfig = {
  duration: SCREEN_ANIMATION_DURATION,
  interpolation: {
    type: 'fastOutSlowIn',
  },
};

const slideInFromRight: ViewAnimationOptions = {
  translationX: {
    from: width,
    to: 0,
    ...baseAnimation,
  },
};

const slideInFromLeft: ViewAnimationOptions = {
  translationX: {
    from: -50,
    to: 0,
    ...baseAnimation,
  },
};

const slideOutToLeft: ViewAnimationOptions = {
  translationX: {
    from: 0,
    to: -50,
    ...baseAnimation,
  },
};

const slideOutToRight: ViewAnimationOptions = {
  translationX: {
    from: 0,
    to: width,
    ...baseAnimation,
  },
};

const slideOutAndExit: ViewAnimationOptions = {
  scaleX: {
    from: 1,
    to: 0.9,
    ...baseAnimation,
  },
  scaleY: {
    from: 1,
    to: 0.9,
    ...baseAnimation,
  },
  alpha: {
    ...baseAnimation,
    from: 1,
    to: 0,
    interpolation: {
      type: 'decelerate',
      factor: 0.8,
    },
  },
  translationY: {
    from: 0,
    to: 100,
    ...baseAnimation,
  },
};

const setRootAnimation: EnterExitAnimationOptions = {
  enter: slideInFromRight,
  exit: slideOutAndExit,
};
const slideAnimations: AnimationOptions = {
  push: {
    waitForRender: true,
    content: {
      enter: slideInFromRight,
      exit: slideOutToLeft,
    },
  },
  pop: {
    content: {
      enter: slideInFromLeft,
      exit: slideOutToRight,
    },
  },
  setStackRoot: {
    waitForRender: true,
    content: {
      enter: slideInFromRight,
      exit: slideOutAndExit,
    },
  },
};

const showModalEnterAnimations: ViewAnimationOptions = {
  translationY: {
    from: height,
    to: 0,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
  alpha: {
    from: 0.65,
    to: 1,
    duration: SCREEN_ANIMATION_DURATION * 0.7,
    interpolation: { type: 'decelerate' },
  },
};

const showModalExitAnimations: ViewAnimationOptions = {
  translationY: {
    from: 0,
    to: -height,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
  alpha: {
    from: 1,
    to: 0.65,
    duration: SCREEN_ANIMATION_DURATION * 0.3,
    interpolation: { type: 'decelerate' },
  },
};

const dismissModalEnterAnimations: ViewAnimationOptions = {
  translationY: {
    from: -height,
    to: 0,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
  alpha: {
    from: 0.65,
    to: 1,
    duration: SCREEN_ANIMATION_DURATION * 0.7,
    interpolation: { type: 'decelerate' },
  },
};

const dismissModalExitAnimations: ViewAnimationOptions = {
  translationY: {
    from: 0,
    to: height,
    duration: SCREEN_ANIMATION_DURATION,
    interpolation: { type: 'decelerate' },
  },
  alpha: {
    from: 1,
    to: 0.65,
    duration: SCREEN_ANIMATION_DURATION * 0.3,
    interpolation: { type: 'decelerate' },
  },
};

const customAnimations: AnimationOptions = {
  ...slideAnimations,
  setRoot: setRootAnimation,
  showModal: {
    enter: showModalEnterAnimations,
    exit: showModalExitAnimations,
  },
  dismissModal: {
    enter: dismissModalEnterAnimations,
    exit: dismissModalExitAnimations,
  },
};

export default {
  ...(useSlideAnimation ? slideAnimations : useCustomAnimations ? customAnimations : {}),
};
