const { Navigation } = require('react-native-navigation');
const Colors = require('./Colors');
const {Dimensions} = require('react-native');
const { height } = Dimensions.get('window');
const PUSH_DURATION = 2070;


const setDefaultOptions = () => Navigation.setDefaultOptions({
  layout: {
    componentBackgroundColor: Colors.background,
    orientation: ['portrait']
  },
  bottomTabs: {
    titleDisplayMode: 'alwaysShow'
  },
  bottomTab: {
    selectedIconColor: Colors.primary,
    selectedTextColor: Colors.primary
  },
  _animations: { // Slow push animation
    push: {
      _waitForRender: true,
      content: {
        alpha: {
          from: 0,
          to: 1,
          duration: PUSH_DURATION,
        },
        y: {
          from: height,
          to: 0,
          duration: PUSH_DURATION,
        }
      }
    }
  }
});

module.exports = {
  setDefaultOptions
}