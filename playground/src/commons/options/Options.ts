import { Navigation, OptionsModalPresentationStyle } from 'react-native-navigation';
import Colors from '../Colors';
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
    bottomTabs: {
      titleDisplayMode: 'alwaysShow',
    },
    bottomTab: {
      selectedIconColor: Colors.primary,
      selectedTextColor: Colors.primary,
    },
    modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
  });

export { setDefaultOptions };
