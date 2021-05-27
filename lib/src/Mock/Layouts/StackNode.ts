import ParentNode from './ParentNode';

export default class StackNode extends ParentNode {
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'Stack', parentNode);
  }

  getVisibleLayout() {
    return this.children[this.children.length - 1].getVisibleLayout();
  }
}
