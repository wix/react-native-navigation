import {Navigation} from 'react-native-navigation';

import FirstTabScreen from './FirstTabScreen';
import SecondTabScreen from './SecondTabScreen';
import PushedScreen from './PushedScreen';
import StyledScreen from './StyledScreen';
import SideMenu from './SideMenu';
import ModalScreen from './ModalScreen';
import NotificationScreen from './NotificationScreen';
import LightBoxScreen from './LightBoxScreen';
import CustomNavBarScreen from './CustomNavBarScreen';
import CustomNavBar from './CustomNavBar';


// register all screens of the app (including internal ones)
export function registerScreens() {
  Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);
  Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen);
  Navigation.registerComponent('example.PushedScreen', () => PushedScreen);
  Navigation.registerComponent('example.StyledScreen', () => StyledScreen);
  Navigation.registerComponent('example.ModalScreen', () => ModalScreen);
  Navigation.registerComponent('example.NotificationScreen', () => NotificationScreen);
  Navigation.registerComponent('example.SideMenu', () => SideMenu);
  Navigation.registerComponent('example.LightBoxScreen', () => LightBoxScreen);

  Navigation.registerComponent('example.CustomNavBarScreen', () => CustomNavBarScreen);
  Navigation.registerComponent('example.CustomNavBar', () => CustomNavBar);
}
