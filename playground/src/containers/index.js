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
const CustomDialog = require('./CustomDialog');

function registerContainers() {
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
}

module.exports = {
  registerContainers
};
