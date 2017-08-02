import _ from 'lodash';
import { processColor } from 'react-native';
import LayoutTypes from './LayoutTypes';
import optionsProcessor from './OptionsProcessor';

export default class LayoutTreeCrawler {
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
    this.store.setPropsForContainerId(node.id, node.data.passProps);
    const clazz = this.store.getOriginalContainerClassForName(node.data.name);
    node.data.navigationOptions = _.cloneDeep(_.get(clazz, 'navigationOptions', {}));
    optionsProcessor(node.data.navigationOptions);
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
