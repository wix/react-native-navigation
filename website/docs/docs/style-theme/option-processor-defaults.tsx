import { CommandName, Navigation, OptionsTopBarButton } from 'react-native-navigation';

Navigation.addOptionProcessor<OptionsTopBarButton>(
  'topBar.rightButtons',
  (button: OptionsTopBarButton, commandName: CommandName): OptionsTopBarButton => {
    button.fontFamily = 'helvetica';
    button.color = 'red';
    return button;
  }
);
