const { isEmpty } = require('lodash');

class TabItem {
  /**
   * @typedef {Object} TabItem
   * @property {string} [title]
   * @property {number} [tag]
   * @property {Object} [icon]
   * @property {boolean} [visible]
   * @property {string} [tabBadge]
   */
  constructor(params) {
    if (isEmpty(params)) {
      return;
    }

    this.tabBadge = params.tabBadge;
    this.hidden = params.hidden;
    this.animateHide = params.animateHide;
    this.title = params.title;
    this.icon = params.icon;
    this.visible = params.visible;
  }
}

module.exports = TabItem;
