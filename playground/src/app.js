// @ts-check
const Navigation = require('./services/Navigation');
const {registerScreens} = require('./screens');
const {Platform} = require('react-native');
const {setDefaultOptions} = require('./commons/Options')
const testIDs = require('./testIDs');
const Screens = require('./screens/Screens');

// @ts-ignore
alert = (title, message) => Navigation.showOverlay({
  component: {
    name: Screens.Alert,
    passProps: {
      title,
      message
    }
  }
});

function start() {
  registerScreens();
  Navigation.events().registerAppLaunchedListener(async () => {
    setDefaultOptions();
    Navigation.dismissAllModals();
    setRoot();
  });
}

function setRoot() {
  Navigation.setRoot({
    root: {
      bottomTabs: {
        children: [
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'Layouts'
                  }
                }
              ],
              options: {
                bottomTab: {
                  text: 'Layouts',
                  icon: require('../img/layouts.png'),
                  selectedIcon: require('../img/layouts_selected.png'),
                  testID: testIDs.LAYOUTS_TAB
                }
              }
            }
          },
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'Options'
                  }
                }
              ],
              options: {
                topBar: {
                  title: {
                    text: 'Default Title'
                  }
                },
                bottomTab: {
                  text: 'Options',
                  icon: require('../img/options.png'),
                  selectedIcon: require('../img/options_selected.png'),
                  testID: testIDs.OPTIONS_TAB
                }
              }
            }
          },
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'Navigation'
                  }
                }
              ]
            }
          }
        ]
      }
    }
  });
}

module.exports = {
  start
};
