const React = require('react');
const CocktailsView = require('../sharedElementTransition/CocktailsView')
const { Platform } = require('react-native');
const Navigation = require('../../services/Navigation');
const Screens = require('../Screens');
import CocktailsListScreen from '../sharedElementTransition/CocktailsListScreen';
import { NavigationButtonPressedEvent } from 'react-native-navigation';

const {
  PUSH_MASTER_BTN
} = require('../../testIDs');

export default class CocktailsListMasterScreen extends CocktailsListScreen {
  static options() {
    return {
      ...Platform.select({
        android: {
          statusBar: {
            style: 'dark',
            backgroundColor: 'white'
          }
        }
      }),
      topBar: {
        title: {
          text: 'Cocktails'
        },
        rightButtons: [{
          id: 'pushMaster',
          testID: PUSH_MASTER_BTN,
          text: 'push'
        }]
      }
    }
  }

  constructor(props: any) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed(event: NavigationButtonPressedEvent) {
    if (event.buttonId === 'pushMaster') {
      Navigation.push(this, Screens.Pushed)
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
  }
}
