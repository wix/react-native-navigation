import BottomTabs from './BottomTabsNode';
import ComponentNode from './ComponentNode';
import Stack from './StackNode';
import ParentNode from './ParentNode';
import SideMenuRootNode, { SideMenuLeftNode, SideMenuRightNode, SideMenuCenterNode } from './SideMenu';
export default class LayoutNodeFactory {
    static create(layout: any, parentNode?: ParentNode): ComponentNode | SideMenuRootNode | SideMenuLeftNode | SideMenuRightNode | SideMenuCenterNode | Stack | BottomTabs;
}
