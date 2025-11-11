import _ from 'lodash';
import { Options } from '../../src/index';
import { switchTabByIndex } from '../actions/layoutActions';
import ParentNode from './ParentNode';

export default class BottomTabsNode extends ParentNode {
  selectedIndex: number;
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'BottomTabs', parentNode);
    this.selectedIndex = layout.data?.options?.bottomTabs?.currentTabIndex || 0;
  }

  mergeOptions(_options: Options) {
    super.mergeOptions(_options);

    const { options } = this.data;
    if (options.bottomTabs?.currentTabIndex) {
      this.selectedIndex = options.bottomTabs?.currentTabIndex;
      switchTabByIndex(this, this.selectedIndex);
    }
    if (options.bottomTabs?.currentTabId) {
      const index = _.findIndex(
        this.children,
        (child) => child.nodeId === options?.bottomTabs?.currentTabId
      );
      if (index !== -1) this.selectedIndex = index;
      switchTabByIndex(this, this.selectedIndex);
    }
  }

  getVisibleLayout() {
    return this.children[this.selectedIndex].getVisibleLayout();
  }
}
