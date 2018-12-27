import * as _ from 'lodash';
import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

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
  constructor(
    private readonly uniqueIdProvider: UniqueIdProvider,
    public readonly store: any,
    private readonly optionsProcessor: OptionsProcessor
  ) {
    this.crawl = this.crawl.bind(this);
  }

  crawl(node: LayoutNode): void {
    node.id = node.id || this.uniqueIdProvider.generate(node.type);
    if (node.type === LayoutType.Component) {
      this.handleComponent(node);
    }
    this.optionsProcessor.processOptions(node.data.options);
    node.children.forEach(this.crawl);
  }

  private handleComponent(node) {
    this.assertComponentDataName(node);
    this.savePropsToStore(node);
    this.applyStaticOptions(node);
    node.data.passProps = undefined;
  }

  private savePropsToStore(node) {
    this.store.setPropsForId(node.id, node.data.passProps);
  }

  private applyStaticOptions(node) {
    const clazz = this.store.getComponentClassForName(node.data.name)
      ? this.store.getComponentClassForName(node.data.name)()
      : {};
    const staticOptions = _.isFunction(clazz.options)
      ? clazz.options(node.data.passProps || {})
      : _.cloneDeep(clazz.options) || {};
    const passedOptions = node.data.options || {};
    node.data.options = _.merge({}, staticOptions, passedOptions);
  }

  private assertComponentDataName(component) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
