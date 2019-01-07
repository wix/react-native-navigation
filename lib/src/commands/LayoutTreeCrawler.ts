import * as _ from 'lodash';
import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';
import { Options } from '../interfaces/Options';
import { Service, Inject } from 'typedi';

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

@Service()
export class LayoutTreeCrawler {
  constructor(
    @Inject('Store') private store: Store,
    private optionsProcessor: OptionsProcessor,
  ) {
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
    this.store.setPropsForId(node.id, node.data.passProps);
  }

  private isComponentWithOptions(component: any): component is ComponentWithOptions {
    return (component as ComponentWithOptions).options !== undefined;
  }

  private staticOptionsIfPossible(node: LayoutNode) {
    const foundReactGenerator = this.store.getComponentClassForName(node.data.name!);
    const reactComponent = foundReactGenerator ? foundReactGenerator() : undefined;
    return reactComponent && this.isComponentWithOptions(reactComponent)
      ? reactComponent.options(node.data.passProps || {})
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
