const { forEach } = require('lodash');
const Container = require('./Container');

class BottomTabs {
  /**
   * @property {Container[]} tabs
   */
  constructor(tabs) {
    if (!tabs || tabs.length === 0) {
      throw new Error('BottomTabs undefined');
    }
    this.tabs = [];
    forEach(tabs, (tab) => this.tabs.push({ container: new Container(tab.container) }));
  }
}

module.exports = BottomTabs;
