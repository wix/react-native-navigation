import { Navigation, AnimationOptions } from 'react-native-navigation';
import { CarItem } from '../../assets/cars';

export const SET_DURATION = 500;
export async function buildSharedElementAnimations(car: CarItem): Promise<AnimationOptions> {
  const { bottomTabsHeight, topBarHeight } = await Navigation.constants();
  return {
    push: {
      content: {
        alpha: {
          from: 0,
          to: 1,
          duration: SET_DURATION,
        },
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}`,
          toId: `image${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: 'overshoot',
        },
        {
          fromId: `title${car.id}`,
          toId: `title${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: 'overshoot',
        },
      ],
      bottomTabs: {
        translationY: {
          from: 0,
          to: bottomTabsHeight,
          duration: SET_DURATION / 3,
        },
      },
      topBar: {
        translationY: {
          from: 0,
          to: -topBarHeight,
          duration: SET_DURATION / 3,
        },
      },
    },
    pop: {
      content: {
        alpha: {
          from: 1,
          to: 0,
          duration: SET_DURATION,
        },
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}Dest`,
          toId: `image${car.id}`,
          duration: SET_DURATION,
          interpolation: 'overshoot',
        },
      ],
      bottomTabs: {
        translationY: {
          from: bottomTabsHeight,
          to: 0,
          duration: SET_DURATION / 2,
        },
      },
      topBar: {
        translationY: {
          from: -topBarHeight,
          to: 0,
          duration: SET_DURATION / 2,
        },
      },
    },
  };
}
