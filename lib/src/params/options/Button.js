class Button {
  /**
   * @property {string} id
   * @property {string} [testID]
   * @property {string} [title]
   * @property {string} [buttonColor]
   * @property {string} [showAsAction]
   * @property {int} [buttonFontWeight]
   * @property {boolean} [disableIconTint]
   * @property {boolean} [disabled]
   */
  constructor(params) {
    this.id = params.id;
    this.testID = params.testID;
    this.title = params.title;
    this.buttonColor = params.buttonColor;
    this.showAsAction = params.showAsAction;
    this.buttonFontWeight = params.buttonFontWeight;
    this.buttonFontSize = params.buttonFontSize;
    this.disableIconTint = params.disableIconTint;
    this.disabled = params.disabled;
  }
}

module.exports = Button;
