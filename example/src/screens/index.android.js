import {Navigation} from 'react-native-navigation';

import FirstTabScreen from './FirstTabScreen';
import SecondTabScreen from './SecondTabScreen';
import PushedScreen from './PushedScreen';
import StyledScreen from './StyledScreen';
import SideMenu from './SideMenu';
import ModalScreen from './ModalScreen';
import CollapsingTopBarScreen from './CollapsingTopBarScreen';
import InAppNotification from './InAppNotification';
import LightBoxScreen from './LightBoxScreen';
import ListScreen from './set/ListScreen';
import HeroScreen from './set/HeroScreen';
import CardScreen from './set/CardScreen';
import InfoScreen from './set/InformationScreen';

// register all screens of the app (including internal ones)
export function registerScreens() {
  Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);
  Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen);
  Navigation.registerComponent('example.PushedScreen', () => PushedScreen);
  Navigation.registerComponent('example.StyledScreen', () => StyledScreen);
  Navigation.registerComponent('example.ModalScreen', () => ModalScreen);
  Navigation.registerComponent('example.SideMenu', () => SideMenu);
  Navigation.registerComponent('example.CollapsingTopBarScreen', () => CollapsingTopBarScreen);
  Navigation.registerComponent('example.InAppNotification', () => InAppNotification);
  Navigation.registerComponent('example.LightBoxScreen', () => LightBoxScreen);
  Navigation.registerComponent('example.ListScreen', () => ListScreen);
  Navigation.registerComponent('example.HeroScreen', () => HeroScreen);
  Navigation.registerComponent('example.CardScreen', () => CardScreen);
  Navigation.registerComponent('example.infoScreen', () => InfoScreen);
}
