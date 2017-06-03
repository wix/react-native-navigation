import Navigation from 'react-native-navigation';

import WelcomeScreen from './WelcomeScreen';
import TextScreen from './TextScreen';
import PushedScreen from './PushedScreen';
import LifecycleScreen from './LifecycleScreen';
import ModalScreen from './ModalScreen';
import OptionsScreen from './OptionsScreen';

export function registerContainers() {
  Navigation.registerContainer(`navigation.playground.WelcomeScreen`, () => WelcomeScreen);
  Navigation.registerContainer(`navigation.playground.ModalScreen`, () => ModalScreen);
  Navigation.registerContainer(`navigation.playground.LifecycleScreen`, () => LifecycleScreen);
  Navigation.registerContainer(`navigation.playground.TextScreen`, () => TextScreen);
  Navigation.registerContainer(`navigation.playground.PushedScreen`, () => PushedScreen);
  Navigation.registerContainer(`navigation.playground.OptionsScreen`, () => OptionsScreen);
}
