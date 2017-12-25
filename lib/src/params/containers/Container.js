class Container {
  /**
   * @property {string} name The container's registered name
   * @property {object} [passProps] props
   * @property {NavigationOptions} navigationOptions
   */
  constructor(params) {
    if (!params || !params.name) {
      throw new Error('Container name is undefined');
    }
    this.name = params.name;
    this.passProps = params.passProps;
    this.navigationOptions = params.navigationOptions;
  }
}

module.exports = Container;
