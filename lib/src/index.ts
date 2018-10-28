import { Navigation as NavigationClass } from './Navigation';

const singleton = new NavigationClass();

export const Navigation = singleton;
