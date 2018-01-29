const { Navigation } = require('react-native-navigation');
const { registerScreens } = require('./screens');

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
        ]
      }
    });
  });
}

module.exports = {
  start
};
