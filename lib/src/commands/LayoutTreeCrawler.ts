import * as _ from 'lodash';
import { OptionsProcessor } from './OptionsProcessor';
import { LayoutType, isLayoutType } from './LayoutType';

export interface LayoutNode {
  id?: string;
  type: LayoutType;
  data: object;
  children: LayoutNode[];
}

export class LayoutTreeCrawler {
  constructor(
    private readonly uniqueIdProvider: any,
    private readonly store: any) {
    this.crawl = this.crawl.bind(this);
  }

  crawl(node: LayoutNode): void {
    this._assertKnownLayoutType(node.type);
    node.id = this.uniqueIdProvider.generate(node.type);
    node.data = node.data || {};
    node.children = node.children || [];
    if (node.type === LayoutType.Component) {
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
    if (!isLayoutType(type)) {
      throw new Error(`Unknown layout type ${type}`);
    }
  }

  _assertComponentDataName(component) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
