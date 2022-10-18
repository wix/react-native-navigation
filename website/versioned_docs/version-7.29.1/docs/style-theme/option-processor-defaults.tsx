import { CommandName, Navigation, OptionsTopBarButton } from 'react-native-navigation';

Navigation.addOptionProcessor<OptionsTopBarButton>(
  'topBar.rightButtons',
  (rightButtons: OptionsTopBarButton[], commandName: CommandName): OptionsTopBarButton => {
    return rightButtons.map((button) => ({
      ...button,
      fontFamily: 'helvetica',
      fontSize: 16,
      color: 'red'
  }));
);
