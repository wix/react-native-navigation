const TopBar = require('./TopBar');
const BottomTabs = require('./BottomTabs');
const BottomTab = require('./BottomTab');
const TopTabs = require('./TopTabs');
const Screen = require('./Screen');

class NavigationOptions {
  /**
   * @property {options:TopBar} [topBar]
   * @property {options:BottomTabs} [bottomTabs]
   * @property {options:BottomTab} [bottomTab]
   * @property {options:Screen} [screen]
   * @property {options:TopTabs} [topTabs]
   */
  constructor(options) {
    this.topBar = options.topBar && new TopBar(options.topBar);
    this.bottomTabs = options.bottomTabs && new BottomTabs(options.bottomTabs);
    this.bottomTab = options.bottomTab && new BottomTab(options.bottomTab);
    this.sideMenu = options.sideMenu;
    this.topTabs = options.topTabs && new TopTabs(options.topTabs);
    this.screen = options.screen && new Screen(options.screen);
  }
}

module.exports = NavigationOptions;
