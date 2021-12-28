import { component, stack } from '../commons/Layouts';
const SideMenuLeft = 'SideMenuLeft';
const SideMenuCenter = 'SideMenuCenter';
const SideMenuRight = 'SideMenuRight';
const SystemUiOptions = 'SystemUiOptions';
const StatusBarFirstTab = 'StatusBarFirstTab';

const Screens = {
  Buttons: 'Buttons',
  CocktailDetailsScreen: 'CocktailDetailsScreen',
  CocktailsListScreen: 'CocktailsListScreen',
  CocktailsListMasterScreen: 'CocktailsListMasterScreen',
  ImageGalleryListScreen: 'ImageGalleryListScreen',
  ImageGalleryItemScreen: 'ImageGalleryItemScreen',
  CarDetailsScreen: 'CarDetailsScreen',
  CarStoryScreen: 'CarStoryScreen',
  CarsListScreen: 'CarsListScreen',
  ImageFullScreenViewer: 'ImageFullScreenViewer',
  ContextScreen: 'ContextScreen',
  ExternalComponent: 'ExternalComponent',
  FullScreenModal: 'FullScreenModal',
  Layouts: 'Layouts',
  Modal: 'Modal',
  ModalCommands: 'ModalCommands',
  StackCommands: 'StackCommands',
  Options: 'Options',
  Components: 'Components',
  Pushed: 'Pushed',
  BackButton: 'BackButton',
  SharedElement: 'SharedElement',
  Stack: 'Stack',
  SetRoot: 'SetRoot',
  Overlay: 'Overlay',
  OverlayAlert: 'OverlayAlert',
  OverlayBanner: 'OverlayBanner',
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
  LazyTitleView: 'LazyTitleView',
  EventsScreen: 'EventsScreen',
  EventsOverlay: 'EventsOverlay',
  LazilyRegisteredScreen: 'LazilyRegisteredScreen',
  SideMenuLeft,
  SideMenuCenter,
  SideMenuRight,
  SideMenu: {
    sideMenu: {
      left: component(SideMenuLeft),
      center: component(SideMenuCenter),
    },
  },
  SystemUi: {
    sideMenu: {
      left: component(
        SideMenuLeft,
        {
          statusBar: {
            drawBehind: true,
            translucent: true,
          },
        },
        { marginTop: 20 }
      ),
      center: stack(SystemUiOptions),
      right: component(SideMenuRight),
    },
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
                icon: require('../../img/layouts.png'),
              },
            },
            left: component(SideMenuLeft),
            center: stack(
              component(StatusBarFirstTab, {
                statusBar: {
                  translucent: true,
                  drawBehind: true,
                },
                topBar: {
                  drawBehind: true,
                  elevation: 0,
                  background: {
                    color: 'transparent',
                  },
                },
              })
            ),
          },
        },
        {
          component: {
            id: 'Pushed.tab2',
            name: 'Pushed',
            options: {
              bottomTab: {
                text: 'Tab2',
                icon: require('../../img/layouts.png'),
              },
            },
          },
        },
      ],
    },
  },
  SystemUiOptions: SystemUiOptions,
  StatusBarFirstTab,
  KeyboardScreen: 'KeyboardScreen',
  TopBarBackground: 'TopBarBackground',
  Toast: 'Toast',
  FlatListScreen: 'FlatListScreen',
  Alert: 'Alert',
  Orientation: 'Orientation',
  OrientationDetect: 'OrientationDetect',
  Search: 'Search',
  SearchBar: 'SearchBar',
  SearchBarModal: 'SearchBarModal',
  TopBar: 'TopBar',
};

export default Screens;
