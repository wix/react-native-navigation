class TopBar {
  /**
   * @typedef {Object} TopBar
   * @property {string} [title]
   * @property {color} [backgroundColor]
   * @property {color} [textColor]
   * @property {number} [textFontSize]
   * @property {string} [textFontFamily]
   * @property {boolean} [hidden]
   * @property {boolean} [animateHide]
   */
  constructor(options) {
    this.title = options.title;
    this.backgroundColor = options.backgroundColor;
    this.textColor = options.textColor;
    this.textFontSize = options.textFontSize;
    this.textFontFamily = options.textFontFamily;
    this.hidden = options.hidden;
    this.animateHide = options.animateHide;
  }
}

module.exports = TopBar;
