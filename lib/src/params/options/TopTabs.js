class TopTabs {
  /**
   * @property {string} [selectedTabColor] Selected tab color
   * @property {string} unselectedTabColor Unselected tab color
   * @property {int} fontSize
   */
  constructor(topTabs) {
    this.selectedTabColor = topTabs.selectedTabColor;
    this.unselectedTabColor = topTabs.unselectedTabColor;
    this.fontSize = topTabs.fontSize;
  }
}

module.exports = TopTabs;
