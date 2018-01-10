const Navigation = require('react-native-navigation');
const { registerContainers } = require('./containers');

function start() {
  registerContainers();
  Navigation.events().onAppLaunched(() => {
    Navigation.setRoot({
      container: {
        name: 'navigation.playground.WelcomeScreen'
      }
    });
  });
}

module.exports = {
  start
};
