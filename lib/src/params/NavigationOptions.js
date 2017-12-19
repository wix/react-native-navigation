const TopBar = require('./TopBar');
const TabBar = require('./TabBar');
const Button = require('./Button');

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
   * @property {String} [orientation]
   * @property {Button} [rightButtons]
   * @property {Button} [leftButtons]
   */
  constructor(options) {
    this.topBar = options.topBar && new TopBar(options.topBar);
    this.bottomTabs = options.bottomTabs && new TabBar(options.bottomTabs);
    this.orientation = options.orientation;
    this.rightButtons = options.rightButtons && options.rightButtons.map((button) => new Button(button));
    this.leftButtons = options.leftButtons && options.leftButtons.map((button) => new Button(button));
    this.sideMenu = options.sideMenu;
  }
}

module.exports = NavigationOptions;
