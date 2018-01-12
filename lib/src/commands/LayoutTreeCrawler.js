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
    if (_.isEqual(node.type, LayoutTypes.Component)) {
      this._handleComponent(node);
    }
    _.forEach(node.children, this.crawl);
  }

  _handleComponent(node) {
    this._assertComponentDataName(node);
    this._savePropsToStore(node);
    this._applyStaticOptions(node);
    OptionsProcessor.processOptions(node.data.options);
  }

  _savePropsToStore(node) {
    this.store.setPropsForComponentId(node.id, node.data.passProps);
  }

  _applyStaticOptions(node) {
    const clazz = this.store.getOriginalComponentClassForName(node.data.name) || {};
    const staticOptions = _.cloneDeep(clazz.options) || {};
    const passedOptions = node.data.options || {};
    node.data.options = _.merge({}, staticOptions, passedOptions);
  }

  _assertKnownLayoutType(type) {
    if (!_.includes(LayoutTypes, type)) {
      throw new Error(`Unknown layout type ${type}`);
    }
  }

  _assertComponentDataName(component) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}

module.exports = LayoutTreeCrawler;
