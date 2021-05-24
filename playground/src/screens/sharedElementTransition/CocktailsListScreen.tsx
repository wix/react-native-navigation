import React from 'react';
import { Platform } from 'react-native';
import { NavigationComponent } from 'react-native-navigation';
import CocktailsView from './CocktailsView';
import Navigation from '../../services/Navigation';
import Screens from '../Screens';
import { CocktailItem } from '../../assets/cocktails';

const MULTIPLIER = 1.2;
const POP_MULTIPLIER = 1.0;
const LONG_DURATION = 540 * MULTIPLIER;
const SHORT_DURATION = 210 * MULTIPLIER;

const SPRING_CONFIG = { mass: 2, damping: 500, stiffness: 200 };

export default class CocktailsListScreen extends NavigationComponent {
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
      },
    };
  }

  render() {
    return <CocktailsView onItemPress={this.pushCocktailDetails} />;
  }

  update = (item: any) => {
    Navigation.updateProps('DETAILS_COMPONENT_ID', item);
  };

  pushCocktailDetails = (item: CocktailItem) => {
    Navigation.push(this, {
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
                  duration: SHORT_DURATION,
                },
              },
              sharedElementTransitions: [
                {
                  fromId: `image${item.id}`,
                  toId: `image${item.id}Dest`,
                  duration: LONG_DURATION,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
                {
                  fromId: `title${item.id}`,
                  toId: `title${item.id}Dest`,
                  duration: LONG_DURATION,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
                {
                  fromId: `backdrop${item.id}`,
                  toId: 'backdrop',
                  duration: LONG_DURATION,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
              ],
              elementTransitions: [
                {
                  id: 'description',
                  alpha: {
                    from: 0,
                    duration: SHORT_DURATION,
                  },
                  translationY: {
                    from: 16,
                    duration: SHORT_DURATION,
                  },
                },
              ],
            },
            pop: {
              content: {
                alpha: {
                  from: 1,
                  to: 0,
                  duration: SHORT_DURATION * POP_MULTIPLIER,
                },
              },
              sharedElementTransitions: [
                {
                  fromId: `image${item.id}Dest`,
                  toId: `image${item.id}`,
                  duration: LONG_DURATION * POP_MULTIPLIER,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
                {
                  fromId: `title${item.id}Dest`,
                  toId: `title${item.id}`,
                  duration: LONG_DURATION * POP_MULTIPLIER,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
                {
                  fromId: 'backdrop',
                  toId: `backdrop${item.id}`,
                  duration: LONG_DURATION * POP_MULTIPLIER,
                  interpolation: { type: 'spring', ...SPRING_CONFIG },
                },
              ],
              elementTransitions: [
                {
                  id: 'description',
                  alpha: {
                    to: 0,
                    duration: SHORT_DURATION,
                  },
                  translationY: {
                    to: 16,
                    duration: SHORT_DURATION,
                  },
                },
              ],
            },
          },
        },
      },
    });
  };
}
