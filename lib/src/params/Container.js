class Container {
  /**
   * @typedef {Object} Container
   * @property {string} name The container's registered name
   * @property {Object} [passProps] props
   */
  constructor(params) {
    this.name = params.name;
    this.passProps = params.passProps;
  }
}

module.exports = Container;
