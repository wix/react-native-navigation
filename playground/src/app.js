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
  Navigation.events().registerAppLaunchedListener(() => {
    Navigation.setDefaultOptions({
      _animations: {
        startApp: {
          y: {
            from: 1000,
            to: 0,
            duration: 500,
            interpolation: 'accelerate',
          },
          alpha: {
            from: 0,
            to: 1,
            duration: 500,
            interpolation: 'accelerate'
          }
        },
        push: {
          topBar: {
            id: 'TEST',
            alpha: {
              from: 0,
              to: 1,
              duration: 500,
              interpolation: 'accelerate'
            }
          },
          bottomTabs: {
            y: {
              from: 1000,
              to: 0,
              duration: 500,
              interpolation: 'decelerate',
            },
            alpha: {
              from: 0,
              to: 1,
              duration: 500,
              interpolation: 'decelerate'
            }
          },
          bottomTabs: {
            y: {
              from: 1000,
              to: 0,
              duration: 500,
              interpolation: 'decelerate',
            },
            alpha: {
              from: 0,
              to: 1,
              duration: 500,
              interpolation: 'decelerate'
            }
          },
          content: {
            y: {
              from: 1000,
              to: 0,
              duration: 500,
              interpolation: 'accelerate',
            },
            alpha: {
              from: 0,
              to: 1,
              duration: 500,
              interpolation: 'accelerate'
            }
          }
        },
        pop: {
          topBar: {
            id: 'TEST',
            alpha: {
              from: 1,
              to: 0,
              duration: 500,
              interpolation: 'accelerate'
            }
          },
          bottomTabs: {
            y: {
              from: 0,
              to: 100,
              duration: 500,
              interpolation: 'accelerate',
            },
            alpha: {
              from: 1,
              to: 0,
              duration: 500,
              interpolation: 'accelerate'
            }
          },
          bottomTabs: {
            y: {
              from: 0,
              to: 100,
              duration: 500,
              interpolation: 'decelerate',
            },
            alpha: {
              from: 1,
              to: 0,
              duration: 500,
              interpolation: 'decelerate'
            }
          },
          content: {
            y: {
              from: 0,
              to: 1000,
              duration: 500,
              interpolation: 'decelerate',
            },
            alpha: {
              from: 1,
              to: 0,
              duration: 500,
              interpolation: 'decelerate'
            }
          }
        }
      }
    });

    // Navigation.setRoot({
    //   stack: {
    //     id: 'TEST',
    //     children: [
    //       {
    //         component: {
    //           name: 'navigation.playground.WelcomeScreen'
    //         }
    //       }
    //     ]
    //   },
    // });

    Navigation.setRoot({
      root: {
        splitView: {
          id: 'SPLITVIEW_ID',
          master: {
            stack: {
              id: 'MASTER_ID',
              children: [
                {
                  component: {
                    name: 'navigation.playground.WelcomeScreen'
                  },
                },
              ]
            },
          },
          detail: {
            stack: {
              id: 'DETAILS_ID',
              children: [
                {
                  component: {
                    name: 'navigation.playground.WelcomeScreen'
                  },
                },
              ]
            }
          },
          options: {
            displayMode: 'auto', // auto, hidden, overlay
            primaryEdge: 'leading', // leading, trailing
            minWidth: 150,
            maxWidth: 300,
          },
        },
      },
    });

  });
}

module.exports = {
  start
};
