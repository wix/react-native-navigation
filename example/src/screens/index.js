import { Navigation } from 'react-native-navigation';

// import FirstTabScreen from './FirstTabScreen';
// import SecondTabScreen from './SecondTabScreen';
// import PushedScreen from './PushedScreen';
// import StyledScreen from './StyledScreen';
// import SideMenu from './SideMenu';
// import ModalScreen from './ModalScreen';
// import NotificationScreen from './NotificationScreen';
// import LightBoxScreen from './LightBoxScreen';

// register all screens of the app (including internal ones)
// export function registerScreens() {
//   Navigation.registerComponent('example.FirstTabScreen', () => FirstTabScreen);
//   Navigation.registerComponent('example.SecondTabScreen', () => SecondTabScreen);
//   Navigation.registerComponent('example.PushedScreen', () => PushedScreen);
//   Navigation.registerComponent('example.StyledScreen', () => StyledScreen);
//   Navigation.registerComponent('example.ModalScreen', () => ModalScreen);
//   Navigation.registerComponent('example.NotificationScreen', () => NotificationScreen);
//   Navigation.registerComponent('example.SideMenu', () => SideMenu);
//   Navigation.registerComponent('example.LightBoxScreen', () => LightBoxScreen);
// }

import Types from './Types';
import Actions from './Actions';
import Transitions from './Transitions';

import Push from './types/Push';
import Drawer from './types/Drawer';
import LightBox from './types/LightBox';
import Notification from './types/Notification';
import Modal from './types/Modal';
import TopTabs from './types/TopTabs';
import TabOne from './types/tabs/TabOne';
import TabTwo from './types/tabs/TabTwo';

import CollapsingHeader from './transitions/CollapsingHeader';
import SharedElementTransitions from './transitions/SharedElementTransitions';

import Cards from './transitions/sharedElementTransitions/Cards/Cards';
import CardsInfo from './transitions/sharedElementTransitions/Cards/Info';

// import Profiles from './transitions/sharedElementTransitions/Profiles/Profiles';

import Masonry from './transitions/sharedElementTransitions/Masonry/Masonry';
import MasonryItem from './transitions/sharedElementTransitions/Masonry/Item';

export default function () {
    Navigation.registerComponent('example.Types', () => Types);
    Navigation.registerComponent('example.Actions', () => Actions);
    Navigation.registerComponent('example.Transitions', () => Transitions);

    Navigation.registerComponent('example.Types.Push', () => Push);
    Navigation.registerComponent('example.Types.Drawer', () => Drawer);
    Navigation.registerComponent('example.Types.Screen', () => Drawer);
    Navigation.registerComponent('example.Types.Modal', () => Modal);
    Navigation.registerComponent('example.Types.LightBox', () => LightBox);
    Navigation.registerComponent('example.Types.Notification', () => Notification);
    Navigation.registerComponent('example.Types.TopTabs', () => TopTabs);
    Navigation.registerComponent('example.Types.TopTabs.TabOne', () => TabOne);
    Navigation.registerComponent('example.Types.TopTabs.TabTwo', () => TabTwo);

    Navigation.registerComponent('example.Transitions.CollapsingHeader', () => CollapsingHeader);
    Navigation.registerComponent('example.Transitions.SharedElementTransitions', () => SharedElementTransitions);
    Navigation.registerComponent('example.Transitions.SharedElementTransitions.Cards', () => Cards);
    Navigation.registerComponent('example.Transitions.SharedElementTransitions.Cards.Info', () => CardsInfo);
    // Navigation.registerComponent('example.Transitions.SharedElementTransitions.Profiles', () => Profiles);
    Navigation.registerComponent('example.Transitions.SharedElementTransitions.Masonry', () => Masonry);
    Navigation.registerComponent('example.Transitions.SharedElementTransitions.Masonry.Item', () => MasonryItem);
}
