const Container = require('./Container');
const SideMenu = require('./SideMenu');
const BottomTabs = require('./BottomTabs');

class Root {
  /**
   * @property {Container} container
   * @property {SideMenu} [sideMenu]
   * @property {BottomTabs} [bottomTabs]
   */
  constructor(root) {
    this.container = root.container && new Container(root.container);
    this.sideMenu = root.sideMenu && new SideMenu(root.sideMenu);
    this.bottomTabs = root.bottomTabs && new BottomTabs(root.bottomTabs).tabs;
  }
}

module.exports = Root;
