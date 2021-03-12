import { Navigation, OptionsTopBarButton } from 'react-native-navigation';

Navigation.addOptionProcessor<OptionsTopBarButton>(
  'topBar.rightButtons',
  (button: OptionsTopBarButton): OptionsTopBarButton => {
    button.fontFamily = 'helvetica';
    button.color = 'red';
    return button;
  }
);
