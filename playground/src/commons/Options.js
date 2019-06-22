const { Navigation } = require('react-native-navigation');
const Colors = require('./Colors');
const { Dimensions, PixelRatio } = require('react-native');
const height = PixelRatio.getPixelSizeForLayoutSize(Dimensions.get('window').height) * 0.7;
const { useSlowOpenScreenAnimations } = require('../flags');

const SHOW_DURATION = 230 * 8;

const setDefaultOptions = () => Navigation.setDefaultOptions({
  layout: {
    backgroundColor: Colors.background,
    orientation: ['portrait']
  },
  bottomTabs: {
    titleDisplayMode: 'alwaysShow'
  },
  bottomTab: {
    selectedIconColor: Colors.primary,
    selectedTextColor: Colors.primary
  },
  animations: {
    ...useSlowOpenScreenAnimations ? slowOpenScreenAnimations : {}   
  }
});

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
      y: {
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