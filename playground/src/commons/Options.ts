import {
  Navigation,
  AnimationOptions,
  OptionsModalPresentationStyle,
} from 'react-native-navigation';
import { Dimensions } from 'react-native';
import Colors from './Colors';
import flags from '../flags';

const { useCustomAnimations, useSlowOpenScreenAnimations, useSlideAnimation } = flags;
const height = Math.round(Dimensions.get('window').height);
const width = Math.round(Dimensions.get('window').width);
const SHOW_DURATION = 250 * (useSlowOpenScreenAnimations ? 2.5 : 1);

const setDefaultOptions = () =>
  Navigation.setDefaultOptions({
    window: {
      backgroundColor: Colors.primary,
    },
    layout: {
      componentBackgroundColor: Colors.background,
      orientation: ['portrait'],
    },
    bottomTabs: {
      titleDisplayMode: 'alwaysShow',
    },
    bottomTab: {
      selectedIconColor: Colors.primary,
      selectedTextColor: Colors.primary,
    },
    animations: {
      ...(useSlideAnimation ? slideAnimations : useCustomAnimations ? customAnimations : {}),
    },
    modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
  });

const slideAnimations: AnimationOptions = {
  push: {
    waitForRender: true,
    content: {
      translationX: {
        from: width,
        to: 0,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300,
      },
      alpha: {
        from: 0.7,
        to: 1,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300,
      },
    },
  },
  pop: {
    content: {
      translationX: {
        from: 0,
        to: width,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300,
      },
      alpha: {
        from: 1,
        to: 0.3,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300,
      },
    },
  },
};

const customAnimations: AnimationOptions = {
  showModal: {
    waitForRender: true,
    translationY: {
      from: height,
      to: 0,
      duration: SHOW_DURATION,
      interpolation: 'decelerate',
    },
    alpha: {
      from: 0.65,
      to: 1,
      duration: SHOW_DURATION * 0.7,
      interpolation: 'accelerate',
    },
  },
  dismissModal: {
    translationY: {
      from: 0,
      to: height,
      duration: SHOW_DURATION * 0.9,
    },
  },
  push: {
    waitForRender: true,
    content: {
      alpha: {
        from: 0.65,
        to: 1,
        duration: SHOW_DURATION * 0.7,
        interpolation: 'accelerate',
      },
      translationY: {
        from: height * 0.3,
        to: 0,
        duration: SHOW_DURATION,
        interpolation: 'decelerate',
      },
    },
  },
  pop: {
    content: {
      alpha: {
        from: 1,
        to: 0,
        duration: SHOW_DURATION,
      },
      translationY: {
        from: 0,
        to: height * 0.7,
        duration: SHOW_DURATION * 0.9,
      },
    },
  },
};

export { setDefaultOptions };
