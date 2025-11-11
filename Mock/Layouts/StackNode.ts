import ParentNode from './ParentNode';

export default class StackNode extends ParentNode {
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'Stack', parentNode);
  }

  getVisibleLayout() {
    const lastChild = this.children[this.children.length - 1];
    if (!lastChild) {
      throw new Error('No visible layout found');
    }
    return lastChild.getVisibleLayout();
  }
}
