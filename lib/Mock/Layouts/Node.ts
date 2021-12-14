import { Options } from '../../src/index';
import ParentNode from './ParentNode';

interface Data {
  name: string;
  options: Options;
}

export type NodeType =
  | 'Component'
  | 'ExternalComponent'
  | 'Stack'
  | 'BottomTabs'
  | 'TopTabs'
  | 'SideMenuRoot'
  | 'SideMenuLeft'
  | 'SideMenuRight'
  | 'SideMenuCenter'
  | 'SplitView';

export default class Node {
  readonly nodeId: string;
  readonly data: Data;
  readonly type: NodeType;
  // options: Options;
  parentNode?: ParentNode;

  constructor(layout: any, type: NodeType, parentNode?: ParentNode) {
    this.nodeId = layout.id;
    this.data = layout.data;
    // this.options = layout.data.options;
    this.parentNode = parentNode;
    this.type = type;
  }
}
