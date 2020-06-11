const { component, stack } = require('../commons/Layouts');
const SideMenuLeft = 'SideMenuLeft';
const SideMenuCenter = 'SideMenuCenter';
const SideMenuRight = 'SideMenuRight';
const StatusBarOptions = 'StatusBarOptions';
const StatusBarFirstTab = 'StatusBarFirstTab'

module.exports = {
  Buttons: 'Buttons',
  CocktailDetailsScreen: 'CocktailDetailsScreen',
  CocktailsListScreen: 'CocktailsListScreen',
  CocktailsListMasterScreen: 'CocktailsListMasterScreen',
  ContextScreen: 'ContextScreen',
  ExternalComponent: 'ExternalComponent',
  FullScreenModal: 'FullScreenModal',
  Layouts: 'Layouts',
  Modal: 'Modal',
  Options: 'Options',
  Pushed: 'Pushed',
  Buttons: 'Buttons',
  SharedElement: 'SharedElement',
  Stack: 'Stack',
  SetRoot: 'SetRoot',
  Overlay: 'Overlay',
  OverlayAlert: 'OverlayAlert',
  ScrollViewScreen: 'ScrollViewScreen',
  ScrollViewOverlay: 'ScrollViewOverlay',
  Lifecycle: 'Lifecycle',
  BackHandler: 'BackHandler',
  BottomTabs: 'BottomTabs',
  FirstBottomTabsScreen: 'FirstBottomTabsScreen',
  SecondBottomTabsScreen: 'SecondBottomTabsScreen',
  Navigation: 'Navigation',
  NativeScreen: 'RNNCustomComponent',
  RoundButton: 'CustomRoundedButton',
  LifecycleButton: 'LifecycleButton',
  ReactTitleView: 'ReactTitleView',
  EventsScreen: 'EventsScreen',
  EventsOverlay: 'EventsOverlay',
  SideMenuLeft,
  SideMenuCenter,
  SideMenuRight,
  SideMenu: {
    sideMenu: {
      left: component(SideMenuLeft),
      center: component(SideMenuCenter)
    }
  },
  StatusBar: {
    sideMenu: {
      left: component(SideMenuLeft,
        {
          statusBar: {
            drawBehind: true,
            translucent: true
          }
        },
        { marginTop: 20 }),
      center: stack(StatusBarOptions),
      right: component(SideMenuRight)
    }
  },
  StatusBarBottomTabs: {
    bottomTabs: {
      id: 'StatusBarBottomTabs',
      children: [
        {
          sideMenu: {
            options: {
              bottomTab: {
                text: 'Tab1',
                icon: require('../../img/layouts.png')
              }
            },
            left: component(SideMenuLeft),
            center: stack(component(StatusBarFirstTab, {
              statusBar: {
                translucent: true,
                drawBehind: true,
              },
              topBar: {
                drawBehind: true,
                elevation: 0,
                background: {
                  color: 'transparent'
                }
              }
            }))
          }
        },
        {
          component: {
            id: "Pushed.tab2",
            name: 'Pushed',
            options: {
              bottomTab: {
                text: 'Tab2',
                icon: require('../../img/layouts.png')
              }
            }
          }
        }
      ]
    }
  },
  StatusBarOptions,
  StatusBarFirstTab,
  TopBarBackground: 'TopBarBackground',
  Toast: 'Toast',
  FlatListScreen: 'FlatListScreen',
  Alert: 'Alert',
  Orientation: 'Orientation',
  OrientationDetect: 'OrientationDetect',
  Search: 'Search'
}
