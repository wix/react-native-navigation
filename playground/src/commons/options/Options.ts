import { Navigation, OptionsModalPresentationStyle } from 'react-native-navigation';
import Colors from '../Colors';
import animations from './Animations';

const setDefaultOptions = () =>
  Navigation.setDefaultOptions({
    animations,
    statusBar: {
      backgroundColor: Colors.statusBarColor,
    },
    window: {
      backgroundColor: Colors.primary,
    },
    layout: {
      componentBackgroundColor: Colors.background,
      orientation: ['portrait'],
      direction: 'locale',
    },
    topBar: {
      backButton: { color: Colors.buttonColor },
      rightButtonColor: Colors.buttonColor,
      rightButtonDisabledColor: Colors.disabledButtonColor,
      leftButtonColor: Colors.buttonColor,
      leftButtonDisabledColor: Colors.disabledButtonColor,
      background: { color: Colors.barBackground },
      title: { color: Colors.textColor },
    },
    bottomTabs: {
      backgroundColor: Colors.barBackground,
      tabsAttachMode: 'afterInitialTab',
      elevation: 8,
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
