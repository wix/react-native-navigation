import ParentNode from './ParentNode';
import ComponentNode from './ComponentNode';
import { Options } from '../../src/index';
import * as layoutActions from '../actions/layoutActions';
import { NodeType } from './Node';

const isCenterChild = (child: ParentNode) => child.type === 'SideMenuCenter';
const isLeftChild = (child: ParentNode) => child.type === 'SideMenuLeft';
const isRightChild = (child: ParentNode) => child.type === 'SideMenuRight';

export default class SideMenuRootNode extends ParentNode {
  visibleChild: ParentNode;

  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'SideMenuRoot', parentNode);

    this.visibleChild = this._getCenterChild();
    if (!this.visibleChild) {
      throw new Error('SideMenuRootNode must have a SideMenuCenter child');
    }
  }

  applyOptions(_options: Options) {
    super.applyOptions(_options);

    this._updateVisibility(_options);
  }

  mergeOptions(options: Options) {
    super.mergeOptions(options);

    this._updateVisibility(options);
  }

  /**
   * @override
   */
  getVisibleLayout(): ComponentNode {
    return this.visibleChild.getVisibleLayout();
  }

  _updateVisibility(options: Options) {
    if (options.sideMenu) {
      if (options.sideMenu.left?.visible) {
        this.visibleChild = this._getLeftChild();
        layoutActions.openSideMenu(this.visibleChild);
      } else if (options.sideMenu.right?.visible) {
        this.visibleChild = this._getRightChild();
        layoutActions.openSideMenu(this.visibleChild);
      } else {
        this.visibleChild = this._getCenterChild();
        layoutActions.closeSideMenu(this.visibleChild);
      }
    }
  }

  _getCenterChild = () => this.children.find(isCenterChild) as ParentNode;
  _getLeftChild = () => this.children.find(isLeftChild) as ParentNode;
  _getRightChild = () => this.children.find(isRightChild) as ParentNode;
}

export class SideMenuNode extends ParentNode {
  constructor(layout: any, type: NodeType, parentNode?: ParentNode) {
    super(layout, type, parentNode);
  }

  getVisibleLayout() {
    return this.children[0].getVisibleLayout();
  }
}

export class SideMenuLeftNode extends SideMenuNode {
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'SideMenuLeft', parentNode);
  }
}
export class SideMenuRightNode extends SideMenuNode {
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'SideMenuRight', parentNode);
  }
}

export class SideMenuCenterNode extends SideMenuNode {
  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'SideMenuCenter', parentNode);
  }
}
