const { isEmpty } = require('lodash');

class BottomTab {
  /**
   * @typedef {Object} BottomTab
   * @property {string} [title]
   * @property {number} [tag]
   * @property {Object} [icon]
   * @property {boolean} [visible]
   * @property {string} [badge]
   */
  constructor(params) {
    if (isEmpty(params)) {
      return;
    }

    this.badge = params.badge;
    this.hidden = params.hidden;
    this.animateHide = params.animateHide;
    this.title = params.title;
    this.icon = params.icon;
    this.visible = params.visible;
  }
}

module.exports = BottomTab;
