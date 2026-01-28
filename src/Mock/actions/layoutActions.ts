import ParentNode from '../Layouts/ParentNode';
import { SideMenuNode } from '../Layouts/SideMenu';
import { LayoutStore } from '../Stores/LayoutStore';

export const switchTabByIndex = (bottomTabs: ParentNode | undefined, index: number) => {
  if (bottomTabs) {
    LayoutStore.getVisibleLayout().componentDidDisappear();
    LayoutStore.selectTabIndex(bottomTabs, index);
    LayoutStore.getVisibleLayout().componentDidAppear();
  }
};

export const openSideMenu = (sideMenu: SideMenuNode) => {
  LayoutStore.openSideMenu(sideMenu);
  LayoutStore.getVisibleLayout().componentDidAppear();
};

export const closeSideMenu = (layout: SideMenuNode) => {
  LayoutStore.getVisibleLayout().componentDidDisappear();
  LayoutStore.closeSideMenu(layout);
};
