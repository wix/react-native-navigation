import {Navigation} from 'react-native-navigation';

import LoginScreen from './LoginScreen';
import FirstTabScreen from './FirstTabScreen';
import SecondTabScreen from './SecondTabScreen';
import PushedScreen from './PushedScreen';
import ListScreen from './ListScreen';
import SideMenu from './SideMenu';
import BottomTabsSideMenu from './BottomTabsSideMenu';
import TopTabsScreen from './TopTabsScreen';

// register all screens of the app (including internal ones)
export function registerScreens(store, Provider) {
  Navigation.registerComponent('example.LoginScreen', () => LoginScreen, store, Provider);
  Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen, store, Provider);
  Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen, store, Provider);
  Navigation.registerComponent('example.PushedScreen', () => PushedScreen, store, Provider);
  Navigation.registerComponent('example.ListScreen', () => ListScreen, store, Provider);
  Navigation.registerComponent('example.SideMenu', () => SideMenu, store, Provider);
  Navigation.registerComponent('example.BottomTabsSideMenu', () => BottomTabsSideMenu, store, Provider);
  Navigation.registerComponent('example.TopTabsScreen', () => TopTabsScreen);
}
