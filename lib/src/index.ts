import { NavigationRoot } from './Navigation';

const navigationSingleton = new NavigationRoot();

export const Navigation = navigationSingleton;
export * from './adapters/Constants';
export * from './interfaces/ComponentEvents';
export * from './interfaces/Events';
export * from './interfaces/EventSubscription';
export * from './interfaces/Layout';
export * from './interfaces/Options';
