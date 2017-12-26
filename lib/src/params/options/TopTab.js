class TopTab {
  /**
   * @property {string} title The tab's title in the TopTabs view
   * @property {string} [titleFontFamily] Change the tab's title font family
   */
  constructor(topTab) {
    this.title = topTab.title;
    this.titleFontFamily = topTab.titleFontFamily;
  }

}

module.exports = TopTab;
