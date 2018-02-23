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
    Navigation.setDefaultOptions({
      animations: {
        push: {
          y: {
            from: 1000,
            to: 0,
            duration: 500,
            interpolation: 'decelerate',
            startDelay: 100
          },
          alpha: {
            from: 0,
            to: 1,
            duration: 500
          }
        },
        pop : {
          y: {
            from: 0,
            to: 1000,
            duration: 500,
            interpolation: 'accelerate',
            startDelay: 100
          },
          alpha: {
            from: 1,
            to: 0,
            duration: 500
          }
        }
      }
    });

    Navigation.setRoot({
      stack: {
        children: [
          {
            component: {
              name: 'navigation.playground.WelcomeScreen'
            }
          }
        ]
      }
    });
  });
}

module.exports = {
  start
};
