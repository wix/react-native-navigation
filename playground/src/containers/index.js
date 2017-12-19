const Navigation = require('react-native-navigation');
const WelcomeScreen = require('./WelcomeScreen');
const TextScreen = require('./TextScreen');
const PushedScreen = require('./PushedScreen');
const LifecycleScreen = require('./LifecycleScreen');
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

function registerContainers() {
  Navigation.registerContainer(`navigation.playground.CustomTransitionDestination`, () => CustomTransitionDestination);
  Navigation.registerContainer(`navigation.playground.CustomTransitionOrigin`, () => CustomTransitionOrigin);
  Navigation.registerContainer(`navigation.playground.ScrollViewScreen`, () => ScrollViewScreen);
  Navigation.registerContainer(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
  Navigation.registerContainer(`navigation.playground.ModalScreen`, () => ModalScreen);
  Navigation.registerContainer(`navigation.playground.LifecycleScreen`, () => LifecycleScreen);
  Navigation.registerContainer(`navigation.playground.TextScreen`, () => TextScreen);
  Navigation.registerContainer(`navigation.playground.PushedScreen`, () => PushedScreen);
  Navigation.registerContainer(`navigation.playground.OptionsScreen`, () => OptionsScreen);
  Navigation.registerContainer(`navigation.playground.OrientationSelectScreen`, () => OrientationSelectScreen);
  Navigation.registerContainer(`navigation.playground.OrientationDetectScreen`, () => OrientationDetectScreen);
  Navigation.registerContainer('navigation.playground.CustomDialog', () => CustomDialog);
  Navigation.registerContainer('navigation.playground.BackHandlerScreen', () => BandHandlerScreen);
  Navigation.registerContainer('navigation.playground.SideMenuScreen', () => SideMenuScreen);
  Navigation.registerContainer('navigation.playground.TopTabScreen', () => TopTabScreen);
  Navigation.registerContainer('navigation.playground.TopTabOptionsScreen', () => TopTabOptionsScreen);
}

module.exports = {
  registerContainers
};
