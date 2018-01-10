const Container = require('./Container');
const SideMenu = require('./SideMenu');

class Root {
  /**
   * @property {Container} container
   * @property {SideMenu} [sideMenu]
   * @property {Container[]} [bottomTabs]
   */
  constructor(root) {
    this.container = root.container && new Container(root.container);
    this.sideMenu = root.sideMenu && new SideMenu(root.sideMenu);
    if (root.bottomTabs) {
      root.bottomTabs.map((t) => new Container(t.container));
      this.bottomTabs = root.bottomTabs;
    }
  }
}

module.exports = Root;
