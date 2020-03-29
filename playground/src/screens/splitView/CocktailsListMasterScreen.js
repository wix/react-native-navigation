const React = require('react');
const { Component } = require('react');
const CocktailsView = require('../sharedElementTransition/CocktailsView')
const { Platform } = require('react-native');
const Navigation = require('../../services/Navigation');

class CocktailsListMasterScreen extends Component {
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
        }
      }
    }
  }

  render() {
    return (
      <CocktailsView 
        onItemPress={this.updateDetailsScreen}
      />
    );
  }

  updateDetailsScreen = (item) => {
    Navigation.updateProps('DETAILS_COMPONENT_ID', item);
  }
}
module.exports = CocktailsListMasterScreen;
