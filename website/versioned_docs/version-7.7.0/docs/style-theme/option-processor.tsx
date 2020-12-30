import {
  CommandName,
  Navigation,
  NavigationButtonPressedEvent,
  OptionsTopBar,
} from 'react-native-navigation';

Navigation.addOptionProcessor<OptionsTopBar>(
  'topBar',
  (topBar: OptionsTopBar, commandName: CommandName): OptionsTopBar => {
    if (commandName === CommandName.ShowModal) {
      if (!topBar.leftButtons) {
        topBar.leftButtons = [
          {
            id: 'dismissModalButton',
            icon: require('dismissIcon.png'),
            color: 'black',
          },
        ];
      }
    }
    return topBar;
  }
);

// Now that each modal has a dismiss button, let's handle the button press event and dismiss the modal when needed.
Navigation.events().registerNavigationButtonPressedListener(
  (event: NavigationButtonPressedEvent) => {
    if (event.buttonId === 'dismissModalButton') {
      Navigation.dismissModal(event.componentId);
    }
  }
);
