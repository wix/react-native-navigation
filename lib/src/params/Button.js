class Button {
  /**
   * @property {String} id
   * @property {string} [testID]
   * @property {string} [title]
   * @property {string} [color]
   */
  constructor(params) {
    this.id = params.id;
    this.testID = params.testID;
    this.title = params.title;
    this.color = params.color;
  }
}

module.exports = Button;
