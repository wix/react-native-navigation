const Container = require('./Container');

class SideMenu {
  /**
  * @property {Container} [left]
  * @property {Container} [right]
  */
  constructor(params) {
    this.left = params.left && { container: new Container(params.left.container) };
    this.right = params.right && { container: new Container(params.right.container) };
  }
}

module.exports = SideMenu;
