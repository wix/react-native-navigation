const { forEach } = require('lodash');
const Container = require('./Container');

class BottomTabs {
  /**
   * @constructor
   * @typedef {Object} BottomTabs
   * @property {Container[]} tabs
   */
  constructor(tabs) {
    this.tabs = [];
    forEach(tabs, (tab) => this.tabs.push({ container: new Container(tab.container) }));
  }
}

module.exports = BottomTabs;
