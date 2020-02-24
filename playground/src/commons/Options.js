const { Navigation } = require('react-native-navigation');
const Colors = require('./Colors');
const { Dimensions } = require('react-native');
const height = Math.round(Dimensions.get('window').height) * 0.7;
const width = Math.round(Dimensions.get('window').width);
const {
  useSlowOpenScreenAnimations,
  useSlideAnimation: useSlideAnimation
} = require('../flags');
const SHOW_DURATION = 230 * 3;

const setDefaultOptions = () => Navigation.setDefaultOptions({
  layout: {
    componentBackgroundColor: Colors.background,
    orientation: ['portrait'],
    direction: 'locale'
  },
  bottomTabs: {
    titleDisplayMode: 'alwaysShow'
  },
  bottomTab: {
    selectedIconColor: Colors.primary,
    selectedTextColor: Colors.primary
  },
  animations: {
    ...useSlideAnimation ?
        slideAnimations :
        useSlowOpenScreenAnimations ?
          slowOpenScreenAnimations :
          {}
  },
  modalPresentationStyle: 'fullScreen'
});

const slideAnimations = {
  push: {
    waitForRender: true,
    content: {
      translationX: {
        from: width,
        to: 0,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300
      },
      alpha: {
        from: 0.7,
        to: 1,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300
      }
    }
  },
  pop: {
    content: {
      translationX: {
        from: 0,
        to: width,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300
      },
      alpha: {
        from: 1,
        to: 0.3,
        duration: useSlowOpenScreenAnimations ? SHOW_DURATION : 300
      }
    }
  }
}

const slowOpenScreenAnimations = {
  showModal: {
    waitForRender: true,
    y: {
      from: height,
      to: 0,
      duration: SHOW_DURATION,
      interpolation: 'accelerateDecelerate'
    },
    alpha: {
      from: 0.7,
      to: 1,
      duration: SHOW_DURATION,
      interpolation: 'accelerate'
    }
  },
  push: {
    waitForRender: true,
    content: {
      alpha: {
        from: 0.6,
        to: 1,
        duration: SHOW_DURATION,
      },
      translationY: {
        from: height,
        to: 0,
        duration: SHOW_DURATION,
      }
    }
  }
}

module.exports = {
  setDefaultOptions
}
