const React = require('react');
import { NavigationComponent } from 'react-native-navigation';
const CocktailsView = require('./CocktailsView')
const { Platform } = require('react-native');
const Navigation = require('../../services/Navigation');
const Screens = require('../Screens')
const MULTIPLIER = 1.15
const LONG_DURATION = 350 * MULTIPLIER
const SHORT_DURATION = 190 * MULTIPLIER

export default class CocktailsListScreen extends NavigationComponent {
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
        onItemPress={this.pushCocktailDetails}
      />
    );
  }

  update = (item: any) => {
    Navigation.updateProps('DETAILS_COMPONENT_ID', item);
  }

  pushCocktailDetails = (item: any) => {
    Navigation.push(
      this,
      {
        component: {
          name: Screens.CocktailDetailsScreen,
          passProps: { ...item },
          options: {
            animations: {
              push: {
                content: {
                  alpha: {
                    from: 0,
                    to: 1,
                    duration: LONG_DURATION
                  }
                },
                sharedElementTransitions: [
                  {
                    fromId: `image${item.id}`,
                    toId: `image${item.id}Dest`,
                    duration: LONG_DURATION
                  },
                  {
                    fromId: `title${item.id}`,
                    toId: `title${item.id}Dest`,
                    duration: LONG_DURATION
                  },
                  {
                    fromId: `backdrop${item.id}`,
                    toId: 'backdrop',
                    duration: LONG_DURATION
                  }
                ],
                elementTransitions: [
                  {
                    id: 'description',
                    alpha: {
                      from: 0,
                      duration: SHORT_DURATION
                    },
                    translationY: {
                      from: 16,
                      duration: SHORT_DURATION
                    }
                  }
                ]
              }
            }
          }
        }
      }
    )
  }
}

