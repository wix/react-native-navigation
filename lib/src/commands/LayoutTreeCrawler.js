const _ = require('lodash');
const LayoutTypes = require('./LayoutTypes');
const OptionsProcessor = require('./OptionsProcessor');

class LayoutTreeCrawler {
  constructor(uniqueIdProvider, store) {
    this.uniqueIdProvider = uniqueIdProvider;
    this.store = store;
    this.crawl = this.crawl.bind(this);
  }

  crawl(node) {
    this._assertKnownLayoutType(node.type);
    node.id = this.uniqueIdProvider.generate(node.type);
    node.data = node.data || {};
    node.children = node.children || [];
    if (node.type === LayoutTypes.Container) {
      this._handleContainer(node);
    }
    _.forEach(node.children, this.crawl);
  }

  _handleContainer(node) {
    this._assertContainerDataName(node);
    this._savePropsToStore(node);
    this._applyStaticNavigationOptions(node);
    OptionsProcessor.processOptions(node.data.navigationOptions);
  }

  _savePropsToStore(node) {
    this.store.setPropsForContainerId(node.id, node.data.passProps);
  }

  _applyStaticNavigationOptions(node) {
    const clazz = this.store.getOriginalContainerClassForName(node.data.name) || {};
    const staticOptions = _.cloneDeep(clazz.navigationOptions) || {};
    const passedOptions = _.cloneDeep(node.data.navigationOptions) || {};
    node.data.navigationOptions = _.merge({}, staticOptions, passedOptions);
  }

  _assertKnownLayoutType(type) {
    if (!_.includes(LayoutTypes, type)) {
      throw new Error(`Unknown layout type ${type}`);
    }
  }

  _assertContainerDataName(container) {
    if (!container.data.name) {
      throw new Error('Missing container data.name');
    }
  }
}

module.exports = LayoutTreeCrawler;
