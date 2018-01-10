const TopBar = require('./TopBar');
const BottomTabs = require('./BottomTabs');
const BottomTab = require('./BottomTab');
const TopTabs = require('./TopTabs');

class NavigationOptions {
  /**
   * @property {options:TopBar} [topBar]
   * @property {options:BottomTabs} [bottomTabs]
   * @property {options:BottomTab} [bottomTab]
   * @property {string} [orientation]
   * @property {options:TopTabs} [topTabs]
   */
  constructor(options) {
    this.topBar = options.topBar && new TopBar(options.topBar);
    this.bottomTabs = options.bottomTabs && new BottomTabs(options.bottomTabs);
    this.bottomTab = options.bottomTab && new BottomTab(options.bottomTab);
    this.orientation = options.orientation;
    this.sideMenu = options.sideMenu;
    this.backgroundImage = options.backgroundImage;
    this.rootBackgroundImage = options.rootBackgroundImage;
    this.screenBackgroundColor = options.screenBackgroundColor;
    this.topTabs = options.topTabs && new TopTabs(options.topTabs);
  }
}

module.exports = NavigationOptions;
