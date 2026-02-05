import BottomTabs from './BottomTabsNode';
import ComponentNode from './ComponentNode';
import Stack from './StackNode';
import ParentNode from './ParentNode';
import SideMenuRootNode, {
  SideMenuLeftNode,
  SideMenuRightNode,
  SideMenuCenterNode,
} from './SideMenu';

export default class LayoutNodeFactory {
  static create(layout: any, parentNode?: ParentNode) {
    switch (layout.type) {
      case 'Component':
        return new ComponentNode(layout, parentNode);
      case 'Stack':
        return new Stack(layout, parentNode);
      case 'SideMenuRoot':
        return new SideMenuRootNode(layout, parentNode);
      case 'SideMenuLeft':
        return new SideMenuLeftNode(layout, parentNode);
      case 'SideMenuCenter':
        return new SideMenuCenterNode(layout, parentNode);
      case 'SideMenuRight':
        return new SideMenuRightNode(layout, parentNode);
      default: // TODO Undo
      case 'BottomTabs':
        return new BottomTabs(layout, parentNode);
    }
  }
}
