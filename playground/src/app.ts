import Navigation from './services/Navigation';
import { registerScreens } from './screens';
import addOptionsProcessors from './commons/OptionProcessors';
import { setDefaultOptions } from './commons/Options';
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
  addOptionsProcessors();
  setDefaultOptions();
  Navigation.events().registerAppLaunchedListener(async () => {
    Navigation.dismissAllModals();
    setRoot();
  });
}

function setRoot() {
  Navigation.setRoot({
    root: {
      bottomTabs: {
        options: {
          animations: {
            setRoot: {
              sharedElementTransitions: [
                {
                  fromId: 'rnnLogo',
                  toId: 'topBarLogo',
                  duration: 700,
                  interpolation: 'decelerate',
                },
              ],
              elementTransitions: [
                {
                  id: 'layoutScreenRoot',
                  alpha: {
                    from: 0,
                    to: 1,
                    duration: 150,
                  },
                },
              ],
            },
          },
        },
        children: [
          {
            stack: {
              id: 'layoutStack',
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
