import { Navigation, OptionsModalPresentationStyle } from 'react-native-navigation';
import Colors from '../Colors';
import RNNTheme from '../RNNTheme';
import animations from './Animations';

const setDefaultOptions = () =>
  Navigation.setDefaultOptions({
    animations,
    window: {
      backgroundColor: Colors.primary,
    },
    layout: {
      componentBackgroundColor: Colors.background,
      orientation: ['portrait'],
      direction: 'locale',
    },
    topBar: {
      background: { color: RNNTheme.topBarBg },
      title: { color: RNNTheme.topBarText },
    },
    bottomTabs: {
      titleDisplayMode: 'alwaysShow',
    },
    bottomTab: {
      iconColor: { light: 'black', dark: '#f0b88a' },
      textColor: { light: 'black', dark: '#f0b88a' },
      selectedIconColor: { light: '#5962e5', dark: '#fffcc2' },
      selectedTextColor: { light: '#5962e5', dark: '#fffcc2' },
    },
    modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
  });

export { setDefaultOptions };
