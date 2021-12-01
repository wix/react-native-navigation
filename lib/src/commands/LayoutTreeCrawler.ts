import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';
import { CommandName } from '../interfaces/CommandName';

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

export class LayoutTreeCrawler {
  constructor(public readonly store: Store, private readonly optionsProcessor: OptionsProcessor) {
    this.crawl = this.crawl.bind(this);
  }

  crawl(node: LayoutNode, commandName: CommandName): void {
    if (node.type === LayoutType.Component) {
      this.handleComponent(node);
    }
    this.optionsProcessor.processOptions(commandName, node.data.options);
    node.children.forEach((value: LayoutNode) => this.crawl(value, commandName));
  }

  private handleComponent(node: LayoutNode) {
    this.assertComponentDataName(node);
    this.savePropsToStore(node);
    node.data.passProps = undefined;
  }

  private savePropsToStore(node: LayoutNode) {
    this.store.setPendingProps(node.id, node.data.passProps);
  }

  private assertComponentDataName(component: LayoutNode) {
    if (!component.data.name) {
      throw new Error('Missing component data.name');
    }
  }
}
