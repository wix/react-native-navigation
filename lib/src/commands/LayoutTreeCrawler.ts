import merge from 'lodash/merge'
import isFunction from 'lodash/isFunction'
import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';
import { Options } from '../interfaces/Options';

export interface Data {
  name?: string;
  options?: any;
  passProps?: any;
}
export interface LayoutNode {
  id: string;
  type: LayoutType;
  data: Data;
  children: LayoutNode[];
}

type ComponentWithOptions = React.ComponentType<any> & { options(passProps: any): Options };

export class LayoutTreeCrawler {
  constructor(public readonly store: Store, private readonly optionsProcessor: OptionsProcessor) {
    this.crawl = this.crawl.bind(this);
  }

  crawl(node: LayoutNode): void {
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
    this.store.updateProps(node.id, node.data.passProps);
  }

  private isComponentWithOptions(component: any): component is ComponentWithOptions {
    return (component as ComponentWithOptions).options !== undefined;
  }

  private applyStaticOptions(node: LayoutNode) {
    node.data.options = merge({}, this.staticOptionsIfPossible(node), node.data.options);
  }

  private staticOptionsIfPossible(node: LayoutNode) {
    const foundReactGenerator = this.store.getComponentClassForName(node.data.name!);
    const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
    if (reactComponent && this.isComponentWithOptions(reactComponent)) {
      return isFunction(reactComponent.options) ? reactComponent.options(node.data.passProps || {}) : reactComponent.options;
    }
    return {};
  }

  private assertComponentDataName(component: LayoutNode) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
