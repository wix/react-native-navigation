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
