import { Navigation, OptionsTopBar, NavigationButtonPressedEvent } from 'react-native-navigation';

export default function addOptionsProcessors() {
  addDismissModalProcessor();
}

const addDismissModalProcessor = () => {
  Navigation.addOptionProcessor<OptionsTopBar>(
    'topBar',
    (topBar: OptionsTopBar, commandName: string): OptionsTopBar => {
      if (commandName === 'showModal') {
        if (!topBar.leftButtons) {
          topBar.leftButtons = [
            {
              id: 'dismissModalButton',
              icon: require('../../img/x.png'),
              color: 'black',
            },
          ];
        }
      }
      return topBar;
    }
  );

  Navigation.events().registerNavigationButtonPressedListener(
    (event: NavigationButtonPressedEvent) => {
      if (event.buttonId === 'dismissModalButton') {
        Navigation.dismissModal(event.componentId);
      }
    }
  );
};
