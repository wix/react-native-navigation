import BottomTabs from './BottomTabsNode';
import ComponentNode from './ComponentNode';
import Stack from './StackNode';
import ParentNode from './ParentNode';

export default class LayoutNodeFactory {
  static create(layout: any, parentNode?: ParentNode) {
    switch (layout.type) {
      case 'Component':
        return new ComponentNode(layout, parentNode);
      case 'Stack':
        return new Stack(layout, parentNode);
      default:
      case 'BottomTabs':
        return new BottomTabs(layout, parentNode);
    }
  }
}
