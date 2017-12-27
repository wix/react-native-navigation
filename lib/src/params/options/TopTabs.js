class TopTabs {
  /**
   * @property {string} [selectedTabColor] Selected tab color
   * @property {string} unselectedTabColor Unselected tab color
   */
  constructor(topTabs) {
    this.selectedTabColor = topTabs.selectedTabColor;
    this.unselectedTabColor = topTabs.unselectedTabColor;
  }
}

module.exports = TopTabs;
