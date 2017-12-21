const { isEmpty } = require('lodash');

class BottomTabs {
  /**
   * @property {string} [currentTabId]
   * @property {number} [currentTabIndex]
   * @property {number} [tabBadge]
   * @property {boolean} [hidden]
   * @property {boolean} [animateHide]
   */
  constructor(params) {
    if (isEmpty(params)) {
      return;
    }
    this.currentTabId = params.currentTabId;
    this.currentTabIndex = params.currentTabIndex;
    this.tabBadge = params.tabBadge;
    this.hidden = params.hidden;
    this.animateHide = params.animateHide;
  }
}

module.exports = BottomTabs;
