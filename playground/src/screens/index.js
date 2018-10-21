const { Navigation } = require('react-native-navigation');
const WelcomeScreen = require('./WelcomeScreen');
const TextScreen = require('./TextScreen');
const PushedScreen = require('./PushedScreen');
const LifecycleScreen = require('./LifecycleScreen');
const StaticLifecycleOverlay = require('./StaticLifecycleOverlay');
const ModalScreen = require('./ModalScreen');
const OptionsScreen = require('./OptionsScreen');
const OrientationSelectScreen = require('./OrientationSelectScreen');
const OrientationDetectScreen = require('./OrientationDetectScreen');
const ScrollViewScreen = require('./ScrollViewScreen');
const CustomTransitionOrigin = require('./CustomTransitionOrigin');
const CustomTransitionDestination = require('./CustomTransitionDestination');
const CustomDialog = require('./CustomDialog');
const BandHandlerScreen = require('./BackHandlerScreen');
const SideMenuScreen = require('./SideMenuScreen');
const TopTabScreen = require('./TopTabScreen');
const TopTabOptionsScreen = require('./TopTabOptionsScreen');
const CustomTopBar = require('./CustomTopBar');
const Alert = require('./Alert');
const BackHandlerModalScreen = require('./BackHandlerModalScreen');
const CustomTextButton = require('./CustomTextButton');
const CustomRoundedButton = require('./CustomRoundedButton');
const TopBarBackground = require('./TopBarBackground');
const ComplexLayout = require('./ComplexLayout');
const SearchScreen = require('./SearchScreen');
const KeyboardScreen = require('./KeyboardScreen');
const BottomTabSideMenuScreen = require('./complexlayouts/BottomTabSideMenuScreen');
const FlatListScreen = require('./FlatListScreen');

function registerScreens() {
  Navigation.registerComponent(`navigation.playground.CustomTransitionDestination`, () => CustomTransitionDestination);
  Navigation.registerComponent(`navigation.playground.CustomTransitionOrigin`, () => CustomTransitionOrigin);
  Navigation.registerComponent(`navigation.playground.ScrollViewScreen`, () => ScrollViewScreen);
  Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
  Navigation.registerComponent(`navigation.playground.ModalScreen`, () => ModalScreen);
  Navigation.registerComponent(`navigation.playground.LifecycleScreen`, () => LifecycleScreen);
  Navigation.registerComponent(`navigation.playground.StaticLifecycleOverlay`, () => StaticLifecycleOverlay);
  Navigation.registerComponent(`navigation.playground.TextScreen`, () => TextScreen);
  Navigation.registerComponent(`navigation.playground.PushedScreen`, () => PushedScreen);
  Navigation.registerComponent(`navigation.playground.OptionsScreen`, () => OptionsScreen);
  Navigation.registerComponent(`navigation.playground.OrientationSelectScreen`, () => OrientationSelectScreen);
  Navigation.registerComponent(`navigation.playground.OrientationDetectScreen`, () => OrientationDetectScreen);
  Navigation.registerComponent('navigation.playground.CustomDialog', () => CustomDialog);
  Navigation.registerComponent('navigation.playground.BackHandlerScreen', () => BandHandlerScreen);
  Navigation.registerComponent('navigation.playground.SideMenuScreen', () => SideMenuScreen);
  Navigation.registerComponent('navigation.playground.TopTabScreen', () => TopTabScreen);
  Navigation.registerComponent('navigation.playground.TopTabOptionsScreen', () => TopTabOptionsScreen);
  Navigation.registerComponent('navigation.playground.CustomTopBar', () => CustomTopBar);
  Navigation.registerComponent('navigation.playground.alert', () => Alert);
  Navigation.registerComponent('navigation.playground.BackHandlerModalScreen', () => BackHandlerModalScreen);
  Navigation.registerComponent('navigation.playground.ComplexLayout', () => ComplexLayout);
  Navigation.registerComponent('CustomTextButton', () => CustomTextButton);
  Navigation.registerComponent('CustomRoundedButton', () => CustomRoundedButton);
  Navigation.registerComponent('TopBarBackground', () => TopBarBackground);
  Navigation.registerComponent('navigation.playground.SearchControllerScreen', () => SearchScreen);
  Navigation.registerComponent('navigation.playground.KeyboardScreen', () => KeyboardScreen);
  Navigation.registerComponent('complexLayout.bottomTabThatOpensSideMenu', () => BottomTabSideMenuScreen);
  Navigation.registerComponent('navigation.playground.FlatListScreen', () => FlatListScreen);
}

module.exports = {
  registerScreens
};
