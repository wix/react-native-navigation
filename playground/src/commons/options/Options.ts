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
    fab: {
      backgroundColor: Colors.accent,
      rippleColor: Colors.primary,
      clickColor: Colors.secondary,
      iconColor: Colors.iconTint,
    },
    topBar: {
      rightButtonColor: Colors.buttonColor,
      rightButtonDisabledColor: Colors.disabledButtonColor,
      leftButtonColor: Colors.buttonColor,
      leftButtonDisabledColor: Colors.disabledButtonColor,
      background: { color: Colors.barBackground },
      title: { color: Colors.textColor },
    },
    bottomTabs: {
      backgroundColor: Colors.barBackground,
      titleDisplayMode: 'alwaysShow',
    },
    bottomTab: {
      iconColor: Colors.iconTint,
      textColor: Colors.textColor,
      selectedIconColor: Colors.activeIconTint,
      selectedTextColor: Colors.activeTextColor,
    },
    modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
  });

export { setDefaultOptions };
