import React from 'react';
import { Platform } from 'react-native';
import { NavigationButtonPressedEvent } from 'react-native-navigation';
import CocktailsView from '../sharedElementTransition/CocktailsView';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import CocktailsListScreen from '../sharedElementTransition/CocktailsListScreen';

import testIDs from '../../testIDs';

const { PUSH_MASTER_BTN } = testIDs;

export default class CocktailsListMasterScreen extends CocktailsListScreen {
  static options() {
    return {
      ...Platform.select({
        android: {
          statusBar: {
            style: 'dark' as const,
            backgroundColor: 'white',
          },
        },
      }),
      topBar: {
        title: {
          text: 'Cocktails',
        },
        rightButtons: [
          {
            id: 'pushMaster',
            testID: PUSH_MASTER_BTN,
            text: 'push',
          },
        ],
      },
    };
  }

  constructor(props: any) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed(event: NavigationButtonPressedEvent) {
    if (event.buttonId === 'pushMaster') {
      Navigation.push(this, Screens.Pushed);
    }
  }

  render() {
    return (
      <CocktailsView
        onItemPress={this.updateDetailsScreen}
        onItemLongPress={this.pushCocktailDetails}
      />
    );
  }

  updateDetailsScreen = (item: any) => {
    Navigation.updateProps('DETAILS_COMPONENT_ID', item);
  };
}
