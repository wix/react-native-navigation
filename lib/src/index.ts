import { Navigation as NavigationClass } from './Navigation';

const singleton = new NavigationClass();

export const Navigation = singleton;
export * from './adapters/Constants';
export * from './interfaces/ComponentEvents';
export * from './interfaces/Events';
export * from './interfaces/EventSubscription';
export * from './interfaces/Layout';
export * from './interfaces/Options';
