const Button = require('./Button');

class TopBar {
  /**
   * @property {string} [title]
   * @property {string} [backgroundColor]
   * @property {string} [textColor]
   * @property {string} [buttonColor]
   * @property {number} [textFontSize]
   * @property {string} [textFontFamily]
   * @property {string} [testID]
   * @property {boolean} [hidden]
   * @property {boolean} [animateHide]
   * @property {boolean} [hideOnScroll]
   * @property {boolean} [transparent]
   * @property {boolean} [translucent]
   * @property {boolean} [blur]
   * @property {boolean} [noBorder]
   * @property {boolean} [largeTitle]
   * @property {boolean} [drawBehind]
   * @property {options:Button[]} [rightButtons]
   * @property {options:Button[]} [leftButtons]
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
    this.translucent = options.translucent;
    this.buttonColor = options.buttonColor;
    this.blur = options.blur;
    this.noBorder = options.noBorder;
    this.largeTitle = options.largeTitle;
    this.testID = options.testID;
    this.drawBehind = options.drawBehind;
    this.rightButtons = options.rightButtons && options.rightButtons.map((button) => new Button(button));
    this.leftButtons = options.leftButtons && options.leftButtons.map((button) => new Button(button));
  }
}

module.exports = TopBar;
