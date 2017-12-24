const { isEmpty } = require('lodash');

class BottomTab {
  /**
   * @property {string} [title]
   * @property {number} [tag]
   * @property {object} [icon]
   * @property {boolean} [visible]
   * @property {string} [badge]
   */
  constructor(params) {
    if (isEmpty(params)) {
      return;
    }

    this.badge = params.badge;
    this.title = params.title;
    this.icon = params.icon;
    this.visible = params.visible;
  }
}

module.exports = BottomTab;
