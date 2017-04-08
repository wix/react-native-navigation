import {Navigation,NavigationToolBarIOS} from './deprecated/indexDeprecated';
import Router from './Router';
export BootComponents from  './boot';

module.exports = {
    Router,
    Navigation,
    NavigationToolBarIOS,
    ...BootComponents
}
