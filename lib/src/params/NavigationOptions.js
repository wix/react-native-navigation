const TopBar = require('./TopBar');
const TabBar = require('./TabBar');

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
   * @property {TabBar} bottomTabs
   */
  constructor(options) {
    this.topBar = options.topBar && new TopBar(options.topBar);
    this.tabBar = options.tabBar && new TabBar(options.tabBar);
  }
}

module.exports = NavigationOptions;
