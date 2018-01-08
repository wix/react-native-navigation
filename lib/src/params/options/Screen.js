
class Screen {
  /**
   * @property {string} [orientation]
   * @property {object} [backgroundImage]
   * @property {object} [rootBackgroundImage]
   * @property {string} [screenBackgroundColor]
   * @property {boolean} [statusBarHidden]
   * @property {string} [backButtonTransition]
   * @property {boolean} [statusBarBlur]
   * @property {boolean} [statusBarHideWithTopBar]
   * @property {boolean} [popGesture]
   */
  constructor(options) {
    this.orientation = options.orientation;
    this.backgroundImage = options.backgroundImage;
    this.rootBackgroundImage = options.rootBackgroundImage;
    this.screenBackgroundColor = options.screenBackgroundColor;
    this.statusBarHidden = options.statusBarHidden;
    this.backBarHidden = options.backBarHidden;
    this.statusBarBlur = options.statusBarBlur;
    this.statusBarHideWithTopBar = options.statusBarHideWithTopBar;
    this.popGesture = options.popGesture;
    this.backButtonTransition = options.backButtonTransition;
  }
}

module.exports = Screen;
