class TopBar {
  /**
   * @property {string} [title]
   * @property {color} [backgroundColor]
   * @property {color} [textColor]
   * @property {number} [textFontSize]
   * @property {string} [textFontFamily]
   * @property {boolean} [hidden]
   * @property {boolean} [animateHide]
   * @property {boolean} [hideOnScroll]
   * @property {boolean} [transparent]
   */
  constructor(options) {
    this.title = options.title;
    this.backgroundColor = options.backgroundColor;
    this.textColor = options.textColor;
    this.textFontSize = options.textFontSize;
    this.textFontFamily = options.textFontFamily;
    this.hidden = options.hidden;
    this.animateHide = options.animateHide;
    this.hideOnScroll = options.hideOnScroll;
    this.transparent = options.transparent;
  }
}

module.exports = TopBar;
