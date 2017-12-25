const { isEmpty } = require('lodash');

class BottomTabs {
  /**
   * @property {string} [currentTabId]
   * @property {number} [currentTabIndex]
   * @property {boolean} [hidden]
   * @property {boolean} [animateHide]
   * @property {string} [testID]
   */
  constructor(params) {
    if (isEmpty(params)) {
      return;
    }
    this.currentTabId = params.currentTabId;
    this.currentTabIndex = params.currentTabIndex;
    this.hidden = params.hidden;
    this.animateHide = params.animateHide;
    this.testID = params.testID;
  }
}

module.exports = BottomTabs;
