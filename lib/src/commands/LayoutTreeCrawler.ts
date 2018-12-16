import * as _ from 'lodash';

import { OptionsProcessor } from './OptionsProcessor';
import { LayoutType } from './LayoutType';
import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { AssetService } from '../adapters/AssetResolver';
import { ColorService } from '../adapters/ColorService';

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
    private readonly uniqueIdProvider: UniqueIdProvider,
    public readonly store: Store
  ) {
    this.optionsProcessor = new OptionsProcessor(
      store,
      uniqueIdProvider,
      new AssetService(),
      new ColorService(),
    );
  }

  crawl = (node: LayoutNode): void => {
    node.id = node.id || this.uniqueIdProvider.generate(node.type);
    if (node.type === LayoutType.Component) {
      this._handleComponent(node);
    }
    if (node.data.options) {
      const transformedOptions = this.optionsProcessor.processOptions(node.data.options);
      node.data.options = transformedOptions;
    }
    _.forEach(node.children, this.crawl);
  }

  _handleComponent(node) {
    this._assertComponentDataName(node);
    this._savePropsToStore(node);
    this._applyStaticOptions(node);
    node.data.passProps = undefined;
  }

  _savePropsToStore(node) {
    this.store.setPropsForId(node.id, node.data.passProps);
  }

  _applyStaticOptions(node) {
    const clazz = this.store.getComponentClassForName(node.data.name) ? this.store.getComponentClassForName(node.data.name)() : {};
    const staticOptions = _.isFunction((clazz as any).options) ? (clazz as any).options(node.data.passProps || {}) : (_.cloneDeep((clazz as any).options) || {});
    const passedOptions = node.data.options || {};
    node.data.options = _.merge({}, staticOptions, passedOptions);
  }

  _assertComponentDataName(component) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
