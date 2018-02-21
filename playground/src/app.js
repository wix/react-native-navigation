const { Navigation } = require('react-native-navigation');
const { registerScreens } = require('./screens');
const { Platform } = require('react-native');

if (Platform.OS === 'android') {
  alert = (title) => {
    Navigation.showOverlay({
      component: {
        name: 'navigation.playground.alert',
        passProps: {
          title
        },
        options: {
          overlay: {
            interceptTouchOutside: true
          }
        }
      }
    });
  };
}

function start() {
  registerScreens();
  Navigation.events().onAppLaunched(() => {
    Navigation.setRoot({
      stack: {
        children: [
          {
            component: {
              name: 'navigation.playground.WelcomeScreen'
            }
          }
        ],
        options: {
          topBar: {
            visible: false,
            animateHide: false
          }
        }
      }
    });
  });
}

module.exports = {
  start
};
