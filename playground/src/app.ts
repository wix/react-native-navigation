import { Navigation as RNNavigation } from 'react-native-navigation';
import Navigation from './services/Navigation';
import { registerScreens } from './screens';
import addProcessors from './commons/Processors';
import { setDefaultOptions } from './commons/options/Options';
import testIDs from './testIDs';
import Screens from './screens/Screens';

// @ts-ignore
alert = (title, message) =>
  Navigation.showOverlay({
    component: {
      name: Screens.Alert,
      passProps: {
        title,
        message,
      },
    },
  });

function start() {
  registerScreens();
  addProcessors();
  setDefaultOptions();
  configureDeepLinking();
  Navigation.events().registerAppLaunchedListener(async () => {
    Navigation.dismissAllModals();
    setRoot();
  });
}

function configureDeepLinking() {
  RNNavigation.setLinking({
    prefixes: ['rnnplayground://'],
    config: {
      screens: {
        Pushed: {
          path: 'pushed/:id',
          screens: {
            Pushed: 'detail/:detailId',
          },
        },
        BackButton: 'back-button',
      },
    },
    // Wrap the default modal so the first screen has a close button.
    // Without this, a single-segment match would not provide a way to
    // dismiss the modal back to the app.
    getModal: (match) => ({
      stack: {
        children: match.path.map((segment, index) => ({
          component: {
            name: segment.screen,
            passProps: filterReservedProps({ ...match.queryParams, ...segment.params }),
            options:
              index === 0
                ? {
                    topBar: {
                      leftButtons: [
                        {
                          id: 'deepLinkClose',
                          testID: testIDs.DEEP_LINK_CLOSE_BTN,
                          text: 'Close',
                        },
                      ],
                    },
                  }
                : undefined,
          },
        })),
      },
    }),
    fallback: (url) => {
      console.warn('Unmatched deep link:', url);
    },
  });

  RNNavigation.events().registerNavigationButtonPressedListener(({ buttonId, componentId }) => {
    if (buttonId === 'deepLinkClose') {
      RNNavigation.dismissModal(componentId);
    }
  });
}

const RESERVED_PROPS = new Set(['ref', 'key']);
function filterReservedProps(props: Record<string, string>): Record<string, string> {
  const out: Record<string, string> = {};
  Object.keys(props).forEach((k) => {
    if (!RESERVED_PROPS.has(k)) out[k] = props[k];
  });
  return out;
}

function setRoot() {
  Navigation.setRoot({
    root: {
      bottomTabs: {
        options: {
          bottomTabs: {
            testID: testIDs.MAIN_BOTTOM_TABS,
          },
        },
        children: [
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'Layouts',
                  },
                },
              ],
              options: {
                bottomTab: {
                  text: 'Layouts',
                  icon: require('../img/layouts.png'),
                  selectedIcon: require('../img/layouts_selected.png'),
                  testID: testIDs.LAYOUTS_TAB,
                },
              },
            },
          },
          {
            stack: {
              children: [
                {
                  component: {
                    name: 'Options',
                  },
                },
              ],
              options: {
                topBar: {
                  title: {
                    text: 'Default Title',
                  },
                },
                bottomTab: {
                  text: 'Options',
                  icon: require('../img/options.png'),
                  selectedIcon: require('../img/options_selected.png'),
                  testID: testIDs.OPTIONS_TAB,
                },
              },
            },
          },
          {
            stack: {
              id: 'NavigationTabStack',
              children: [
                {
                  component: {
                    name: 'Navigation',
                  },
                },
              ],
            },
          },
        ],
      },
    },
  });
}

export { start };
