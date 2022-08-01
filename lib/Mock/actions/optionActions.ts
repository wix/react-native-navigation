import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';

class OptionActions {
  switchTabByIndex(bottomTabs: ParentNode | undefined, index: number) {
    if (bottomTabs) {
      LayoutStore.getVisibleLayout().componentDidDisappear();
      LayoutStore.selectTabIndex(bottomTabs, index);
      LayoutStore.getVisibleLayout().componentDidAppear();
    }
  }

  setDefaultOptions(options: object) {
    LayoutStore.setDefaultOptions(options);
  }

  mergeOptions(componentId: string, options: object) {
    LayoutStore.mergeOptions(componentId, options);
  }
}

export default new OptionActions();
