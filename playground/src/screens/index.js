const React = require('react');
const { Navigation } = require('react-native-navigation');
const ScrollViewScreen = require('./ScrollViewScreen');
const CustomTransitionOrigin = require('./CustomTransitionOrigin');
const CustomTransitionDestination = require('./CustomTransitionDestination');
const CustomDialogWithScroll = require('./complexlayouts/CustomDialogWithScroll');
const TopTabScreen = require('./TopTabScreen');
const TopTabOptionsScreen = require('./TopTabOptionsScreen');
const CustomTextButton = require('./CustomTextButton');
const TopBarBackground = require('./TopBarBackground');
const KeyboardScreen = require('./KeyboardScreen');
const ContextScreen = require('./ContextScreen');
const { ContextProvider } = require('../context');
const Screens = require('./Screens');

function registerScreens() {
  Navigation.registerComponent(Screens.Layouts, () => require('./LayoutsScreen'));
  Navigation.registerComponent(Screens.Options, () => require('./OptionsScreen'));
  Navigation.registerComponent(Screens.Stack, () => require('./StackScreen'));
  Navigation.registerComponent(Screens.Pushed, () => require('./PushedScreen'));
  Navigation.registerComponent(Screens.Modal, () => require('./ModalScreen'))
  Navigation.registerComponent(Screens.SetRoot, () => require('./SetRootScreen'))
  Navigation.registerComponent(Screens.Navigation, () => require('./NavigationScreen'));
  Navigation.registerComponent(Screens.FirstBottomTabsScreen, () => require('./FirstBottomTabScreen'));
  Navigation.registerComponent(Screens.SecondBottomTabsScreen, () => require('./SecondBottomTabScreen'));
  Navigation.registerComponent(Screens.Lifecycle, () => require('./LifecycleScreen'));
  Navigation.registerComponent(Screens.Overlay, () => require('./OverlayScreen'));
  Navigation.registerComponent(Screens.OverlayAlert, () => require('./OverlayAlert'));
  Navigation.registerComponent(Screens.ScrollViewOverlay, () => require('./ScrollViewOverlay'));
  Navigation.registerComponent(Screens.RoundButton, () => require('./RoundedButton'));
  Navigation.registerComponent(Screens.ReactTitleView, () => require('./CustomTopBar'));
  Navigation.registerComponent(Screens.EventsScreen, () => require('./StaticEventsScreen'));
  Navigation.registerComponent(Screens.EventsOverlay, () => require('./StaticLifecycleOverlay'));
  Navigation.registerComponent(Screens.SideMenuLeft, () => require('./SideMenuLeftScreen'));
  Navigation.registerComponent(Screens.SideMenuCenter, () => require('./SideMenuCenterScreen'));
  Navigation.registerComponent(Screens.SideMenuRight, () => require('./SideMenuRightScreen'));
  Navigation.registerComponent(Screens.FlatListScreen, () => require('./FlatListScreen'));
  Navigation.registerComponent(Screens.Alert, () => require('./Alert'));
  Navigation.registerComponent(Screens.Orientation, () => require('./OrientationScreen'));
  Navigation.registerComponent(Screens.OrientationDetect, () => require('./OrientationDetectScreen'));
  Navigation.registerComponent(Screens.Search, () => require('./SearchScreen'));
  Navigation.registerComponent(Screens.ExternalComponent, () => require('./ExternalComponentScreen'));

  Navigation.registerComponent(`navigation.playground.CustomTransitionDestination`, () => CustomTransitionDestination);
  Navigation.registerComponent(`navigation.playground.CustomTransitionOrigin`, () => CustomTransitionOrigin);
  Navigation.registerComponent(`navigation.playground.ScrollViewScreen`, () => ScrollViewScreen);
  Navigation.registerComponent('navigation.playground.ContextScreen', () => (props) =>
    <ContextProvider>
      <ContextScreen {...props} />
    </ContextProvider>,
    () => ContextScreen);
  Navigation.registerComponent('navigation.playground.CustomDialog', () => CustomDialog);
  Navigation.registerComponent('navigation.playground.CustomDialogWithScroll', () => CustomDialogWithScroll);
  Navigation.registerComponent('navigation.playground.TopTabScreen', () => TopTabScreen);
  Navigation.registerComponent('navigation.playground.TopTabOptionsScreen', () => TopTabOptionsScreen);
  Navigation.registerComponent('CustomTextButton', () => CustomTextButton);
  Navigation.registerComponent('TopBarBackground', () => TopBarBackground);
  Navigation.registerComponent('navigation.playground.KeyboardScreen', () => KeyboardScreen);
}

module.exports = {
  registerScreens
};
