import * as _ from 'lodash';
import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';

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
    public readonly store: Store,
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

  private handleComponent(node: LayoutNode) {
    this.assertComponentDataName(node);
    this.savePropsToStore(node);
    this.applyStaticOptions(node);
    node.data.passProps = undefined;
  }

  private savePropsToStore(node: LayoutNode) {
    this.store.setPropsForId(node.id!, node.data.passProps);
  }

  private staticOptionsIfPossible(node: LayoutNode) {
    const foundReactGenerator = this.store.getComponentClassForName(node.data.name!);
    const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
    return reactComponent && (reactComponent as any).options
      ? (reactComponent as any).options(node.data.passProps || {})
      : {};
  }

  private applyStaticOptions(node: LayoutNode) {
    node.data.options = _.merge({}, this.staticOptionsIfPossible(node), node.data.options);
  }

  private assertComponentDataName(component: LayoutNode) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
