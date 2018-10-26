import { Navigation as NavigationClass } from './Navigation';

const singleton = new NavigationClass();

const Navigation = singleton;
export {
    Navigation,
    NavigationClass
};
