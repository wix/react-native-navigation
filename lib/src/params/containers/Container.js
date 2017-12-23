class Container {
  /**
   * @property {string} name The container's registered name
   * @property {object} [passProps] props
   */
  constructor(params) {
    if (!params || !params.name) {
      throw new Error('Container name is undefined');
    }
    this.name = params.name;
    this.passProps = params.passProps;
  }
}

module.exports = Container;
