import * as _ from 'lodash';
import { OptionsProcessor } from './OptionsProcessor';
import { LayoutType } from './LayoutType';

export interface Data {
  name?: string;
  options?: any;
  passProps?: any;
}
export interface LayoutNode {
  id?: string;
  type: LayoutType;
  data: Data;
  children: LayoutNode[];
}

export class LayoutTreeCrawler {
  private optionsProcessor: OptionsProcessor;
  constructor(
    private readonly uniqueIdProvider: any,
    public readonly store: any) {
    this.crawl = this.crawl.bind(this);
    this.processOptions = this.processOptions.bind(this);
    this.optionsProcessor = new OptionsProcessor(store, uniqueIdProvider);
  }

  crawl(node: LayoutNode): void {
    this._assertKnownLayoutType(node.type);
    node.id = node.id || this.uniqueIdProvider.generate(node.type);
    node.data = node.data || {};
    node.children = node.children || [];
    if (node.type === LayoutType.Component) {
      this._handleComponent(node);
    }
    this.processOptions(node.data.options);
    _.forEach(node.children, this.crawl);
  }

  processOptions(options) {
    this.optionsProcessor.processOptions(options);
  }

  _handleComponent(node) {
    this._assertComponentDataName(node);
    this._savePropsToStore(node);
    this._applyStaticOptions(node);
  }

  _savePropsToStore(node) {
    this.store.setPropsForId(node.id, node.data.passProps);
  }

  _applyStaticOptions(node) {
    const clazz = this.store.getOriginalComponentClassForName(node.data.name) || {};
    const staticOptions = _.isFunction(clazz.options) ? clazz.options(node.data.passProps || {}) : (_.cloneDeep(clazz.options) || {});
    const passedOptions = node.data.options || {};
    node.data.options = _.merge({}, staticOptions, passedOptions);
  }

  _assertKnownLayoutType(type) {
    if (!LayoutType[type]) {
      throw new Error(`Unknown layout type ${type}`);
    }
  }

  _assertComponentDataName(component) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
