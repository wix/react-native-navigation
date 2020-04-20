const React = require('react');
const CocktailsView = require('../sharedElementTransition/CocktailsView')
const { Platform } = require('react-native');
const Navigation = require('../../services/Navigation');
const Screens = require('../Screens');
const CocktailsListScreen = require('../sharedElementTransition/CocktailsListScreen');
const {
  PUSH_MASTER_BTN
} = require('../../testIDs');

class CocktailsListMasterScreen extends CocktailsListScreen {
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

  constructor(props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  navigationButtonPressed({buttonId}) {
    if (buttonId === 'pushMaster') {
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

  updateDetailsScreen = (item) => {
    Navigation.updateProps('DETAILS_COMPONENT_ID', item);
  }
}

module.exports = CocktailsListMasterScreen;
