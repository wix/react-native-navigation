import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';

export const switchTabByIndex = (bottomTabs: ParentNode | undefined, index: number) => {
  if (bottomTabs) {
    LayoutStore.getVisibleLayout().componentDidDisappear();
    LayoutStore.selectTabIndex(bottomTabs, index);
    LayoutStore.getVisibleLayout().componentDidAppear();
  }
};
