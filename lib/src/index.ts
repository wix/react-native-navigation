import 'reflect-metadata';
import { Container } from 'typedi';

import { NavigationRoot } from './Navigation';

export const Navigation = Container.get<NavigationRoot>(NavigationRoot.name);
export * from './adapters/Constants';
export * from './interfaces/ComponentEvents';
export * from './interfaces/Events';
export * from './interfaces/EventSubscription';
export * from './interfaces/Layout';
export * from './interfaces/Options';
