// @ts-check
const { Navigation } = require('react-native-navigation');
const { registerScreens } = require('./screens');
const { Platform } = require('react-native');
const { setDefaultOptions } = require('./commons/Options')
const testIDs = require('./testIDs');
const Screens = require('./screens/Screens');

if (Platform.OS === 'android') {
  alert = (title) => {
    Navigation.showOverlay({
      component: {
        name: Screens.Alert,
        passProps: {
          title
        },
        options: {
          layout: {
            componentBackgroundColor: 'transparent'
          },
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
  Navigation.events().registerAppLaunchedListener(async () => {
    setDefaultOptions();

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
  });
}

module.exports = {
  start
};
