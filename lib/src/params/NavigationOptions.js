const TopBar = require('./TopBar');
const BottomTabs = require('./BottomTabs');

 /**
 * A module for adding two values.
 * @module NavigationOptions
 */

 /**
  * NavigationOptions are used by containers to customize their behavior and style.
  * @alias module:NavigationOptions
  */
class NavigationOptions {
  /**
   * @typedef {Object} NavigationOptions
   * @property {TopBar} topBar
   * @property {BottomTabs} bottomTabs
   */
  constructor(options) {
    this.topBar = options.topBar && new TopBar(options.topBar);
    this.tabBar = options.tabBar && new BottomTabs(options.tabBar);
  }
}

module.exports = NavigationOptions;
