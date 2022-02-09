import { setDefaultOptions } from './commons/options/Options';
import addProcessors from './commons/Processors';
import { registerScreens } from './screens';
import Screens from './screens/Screens';
import Navigation from './services/Navigation';
import testIDs from './testIDs';

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
        id: 'bottomTabs',
        options: {
          bottomTabs: {
            testID: testIDs.MAIN_BOTTOM_TABS,
          },
        },
        children: [
          {
            stack: {
              id: 'LayoutsStack',
              children: [
                {
                  component: {
                    id: 'LayoutsTabMainComponent',
                    name: 'Layouts',
                  },
                },
              ],
              options: {
                topBar: {
                  animate: false,
                  leftButtons: [
                    {
                      id: 'Button1',
                      icon: require('../img/layouts.png'),
                    },
                  ],
                },
                bottomTab: {
                  id: 'LayoutsBottomTab',
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
              id: 'OptionsStack',
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
                  animate: false,
                  leftButtons: [
                    {
                      id: 'Button2',
                      icon: require('../img/layouts.png'),
                    },
                  ],
                },
                bottomTab: {
                  id: 'OptionsBottomTab',
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
              id: 'NavigationStack',
              children: [
                {
                  component: {
                    name: 'Navigation',
                  },
                },
              ],
              options: {
                topBar: {
                  animate: false,
                  leftButtons: [
                    {
                      id: 'Button3',
                      icon: require('../img/layouts.png'),
                    },
                  ],
                },
                bottomTab: {
                  id: 'NavigationBottomTab',
                },
              },
            },
          },
        ],
      },
    },
  });
}

export { start };
