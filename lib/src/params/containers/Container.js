const NavigationOptions = require('./../options/NavigationOptions');

class Container {
  /**
   * @property {string} name The container's registered name
   * @property {Container[]} [topTabs]
   * @property {object} [passProps] props
   * @property {object} [customTransition]
   * @property {NavigationOptions} navigationOptions
   */
  constructor(params) {
    this.name = params.name;
    if (params.topTabs) {
      params.topTabs.map((t) => new Container(t));
      this.topTabs = params.topTabs;
    }
    this.passProps = params.passProps;
    this.customTransition = params.customTransition;
    this.navigationOptions = params.navigationOptions && new NavigationOptions(params.navigationOptions);
  }
}

module.exports = Container;
