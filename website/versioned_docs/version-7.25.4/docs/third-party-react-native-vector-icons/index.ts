import { Navigation } from 'react-native-navigation';

import { iconsMap } from './NavIcons';

Navigation.events().registerAppLaunchedListener(() => {
  registerScreens();
  const firstTab = {
    component: {
      name: 'MyFirstTab',
      options: {
        topBar: {
          title: {
            text: 'First Tab',
          },
          rightButtons: [{
            icon: iconsMap.add,
            id: 'add',
            color: 'blue',
            accessibilityLabel: 'Add item',
          }],
        },
      },
    },
  };
  const secondTab = {
    component: {
      name: 'MySecondTab',
      options: {
        topBar: {
          title: {
            text: 'Second Tab',
          },
        },
      },
    },
  };
  Navigation.setRoot({
    root: {
      bottomTabs: {
        children: [{
          stack: {
            children: [firstTab],
            bottomTab: {
              text: 'First',
              icon: iconsMap.book,
            },
          },
        },
        {
          stack: {
            children: [secondTab],
            bottomTab: {
              text: 'Second',
              icon: iconsMap.gear,
            },
          },
        }],
      },
    },
  });
});
