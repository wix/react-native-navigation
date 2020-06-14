const React = require('react');
const { Navigation } = require('react-native-navigation');
const CustomDialogWithScroll = require('./complexlayouts/CustomDialogWithScroll');
const TopTabScreen = require('./TopTabScreen');
const TopTabOptionsScreen = require('./TopTabOptionsScreen');
const CustomTextButton = require('./CustomTextButton');
const KeyboardScreen = require('./KeyboardScreen');
const Screens = require('./Screens');

function registerScreens() {
  Navigation.registerComponent(Screens.Alert, () => require('./Alert'));
  Navigation.registerComponent(Screens.CocktailDetailsScreen, () => require('./sharedElementTransition/CocktailDetailsScreen'));
  Navigation.registerComponent(Screens.CocktailsListScreen, () => require('./sharedElementTransition/CocktailsListScreen').default);
  Navigation.registerComponent(Screens.CocktailsListMasterScreen, () => require('./splitView/CocktailsListMasterScreen').default);
  Navigation.registerComponent(Screens.EventsOverlay, () => require('./StaticLifecycleOverlay').StaticLifecycleOverlay);
  Navigation.registerComponent(Screens.EventsScreen, () => require('./StaticEventsScreen'));
  Navigation.registerComponent(Screens.ExternalComponent, () => require('./ExternalComponentScreen'));
  Navigation.registerComponent(Screens.FirstBottomTabsScreen, () => require('./FirstBottomTabScreen'));
  Navigation.registerComponent(Screens.FlatListScreen, () => require('./FlatListScreen'));
  Navigation.registerComponent(Screens.Layouts, () => require('./LayoutsScreen'));
  Navigation.registerComponent(Screens.Lifecycle, () => require('./LifecycleScreen'));
  Navigation.registerComponent(Screens.Modal, () => require('./ModalScreen'))
  Navigation.registerComponent(Screens.FullScreenModal, () => require('./FullScreenModalScreen'))
  Navigation.registerComponent(Screens.Navigation, () => require('./NavigationScreen'));
  Navigation.registerComponent(Screens.Options, () => require('./OptionsScreen'));
  Navigation.registerComponent(Screens.Buttons, () => require('./ButtonsScreen'));
  Navigation.registerComponent(Screens.Orientation, () => require('./OrientationScreen'));
  Navigation.registerComponent(Screens.OrientationDetect, () => require('./OrientationDetectScreen'));
  Navigation.registerComponent(Screens.Overlay, () => require('./OverlayScreen'));
  Navigation.registerComponent(Screens.OverlayAlert, () => require('./OverlayAlert'));
  Navigation.registerComponent(Screens.Pushed, () => require('./PushedScreen'));
  Navigation.registerComponent(Screens.ScrollViewOverlay, () => require('./ScrollViewOverlay'));
  Navigation.registerComponent(Screens.LifecycleButton, () => require('./LifecycleButton'));
  Navigation.registerComponent(Screens.ReactTitleView, () => require('./CustomTopBar'));
  Navigation.registerComponent(Screens.RoundButton, () => require('./RoundedButton'));
  Navigation.registerComponent(Screens.ScrollViewScreen, () => require('./ScrollViewScreen'));
  Navigation.registerComponent(Screens.ScrollViewOverlay, () => require('./ScrollViewOverlay'));
  Navigation.registerComponent(Screens.SecondBottomTabsScreen, () => require('./SecondBottomTabScreen'));
  Navigation.registerComponent(Screens.Search, () => require('./SearchScreen'));
  Navigation.registerComponent(Screens.SetRoot, () => require('./SetRootScreen'))
  Navigation.registerComponent(Screens.SideMenuCenter, () => require('./SideMenuCenterScreen'));
  Navigation.registerComponent(Screens.SideMenuLeft, () => require('./SideMenuLeftScreen'));
  Navigation.registerComponent(Screens.SideMenuRight, () => require('./SideMenuRightScreen'));
  Navigation.registerComponent(Screens.Stack, () => require('./StackScreen'));
  Navigation.registerComponent(Screens.StatusBarOptions, () => require('./StatusBarOptionsScreen'));
  Navigation.registerComponent(Screens.StatusBarFirstTab, () => require('./StatusBarFirstTab'));
  Navigation.registerComponent(Screens.TopBarBackground, () => require('../components/TopBarBackground'));
  Navigation.registerComponent(Screens.Toast, () => require('./Toast'));

  const { ContextProvider } = require('../context');
  const ContextScreen = require('./ContextScreen');
  Navigation.registerComponent(Screens.ContextScreen, () => (props) =>
    <ContextProvider>
      <ContextScreen {...props} />
    </ContextProvider>,
    () => ContextScreen);

  Navigation.registerComponent('navigation.playground.CustomDialog', () => CustomDialog);
  Navigation.registerComponent('navigation.playground.CustomDialogWithScroll', () => CustomDialogWithScroll);
  Navigation.registerComponent('navigation.playground.TopTabScreen', () => TopTabScreen);
  Navigation.registerComponent('navigation.playground.TopTabOptionsScreen', () => TopTabOptionsScreen);
  Navigation.registerComponent('CustomTextButton', () => CustomTextButton);
  Navigation.registerComponent('navigation.playground.KeyboardScreen', () => KeyboardScreen);
}

module.exports = {
  registerScreens
};
