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
const FabScreen = require('./FabScreen');
const NestedScrollViewScreen = require('./NestedScrollViewScreen');
const BandHandlerScreen = require('./BackHandlerScreen');

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
  Navigation.registerContainer(`navigation.playground.FabScreen`, () => FabScreen);
  Navigation.registerContainer(`navigation.playground.NestedScrollViewScreen`, () => NestedScrollViewScreen);
  Navigation.registerContainer('navigation.playground.BackHandlerScreen', () => BandHandlerScreen);
}

module.exports = {
  registerContainers
};
