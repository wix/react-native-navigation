import { AnimationOptions } from 'react-native-navigation';
import { CarItem } from '../../assets/cars';

export const SET_DURATION = 500;
export function buildSharedElementAnimations(car: CarItem): AnimationOptions {
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
          interpolation: { type: 'spring', mass: 3, damping: 500, stiffness: 200 },
        },
        {
          fromId: `title${car.id}`,
          toId: `title${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', mass: 3, damping: 500, stiffness: 200 },
        },
      ],
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
          interpolation: { type: 'spring', mass: 3, damping: 500, stiffness: 200 },
        },
      ],
    },
  };
}
