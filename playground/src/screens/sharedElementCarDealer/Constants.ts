import { AnimationOptions } from 'react-native-navigation';
import { CarItem } from '../../assets/cars';

const SPRING_CONFIG = { mass: 3, damping: 500, stiffness: 200 };

export const SET_DURATION = 500;
export function buildSharedElementAnimations(car: CarItem): AnimationOptions {
  return {
    showModal: {
      alpha: {
        from: 0,
        to: 1,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}`,
          toId: `image${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
        {
          fromId: `title${car.id}`,
          toId: `title${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
    dismissModal: {
      alpha: {
        from: 1,
        to: 0,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}Dest`,
          toId: `image${car.id}`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
  };
}

export function buildStorySharedElementAnimations(car: CarItem): AnimationOptions {
  return {
    showModal: {
      alpha: {
        from: 0,
        to: 1,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `story.${car.id}.background.from`,
          toId: `story.${car.id}.background.to`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
        {
          fromId: `story.${car.id}.icon.from`,
          toId: `story.${car.id}.icon.to`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
        {
          fromId: `story.${car.id}.title.from`,
          toId: `story.${car.id}.title.to`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
    dismissModal: {
      alpha: {
        from: 1,
        to: 0,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `story.${car.id}.background.to`,
          toId: `story.${car.id}.background.from`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
        {
          fromId: `story.${car.id}.icon.to`,
          toId: `story.${car.id}.icon.from`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
        {
          fromId: `story.${car.id}.title.to`,
          toId: `story.${car.id}.title.from`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
  };
}

export function buildFullScreenSharedElementAnimations(car: CarItem): AnimationOptions {
  return {
    showModal: {
      alpha: {
        from: 0,
        to: 1,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}Dest`,
          toId: `image${car.id}Full`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
    dismissModal: {
      alpha: {
        from: 1,
        to: 0,
        duration: SET_DURATION,
      },
      sharedElementTransitions: [
        {
          fromId: `image${car.id}Full`,
          toId: `image${car.id}Dest`,
          duration: SET_DURATION,
          interpolation: { type: 'spring', ...SPRING_CONFIG },
        },
      ],
    },
  };
}
