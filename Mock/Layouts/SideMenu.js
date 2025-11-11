"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SideMenuCenterNode = exports.SideMenuRightNode = exports.SideMenuLeftNode = exports.SideMenuNode = void 0;
const tslib_1 = require("tslib");
const ParentNode_1 = tslib_1.__importDefault(require("./ParentNode"));
const layoutActions = tslib_1.__importStar(require("../actions/layoutActions"));
const isCenterChild = (child) => child.type === 'SideMenuCenter';
const isLeftChild = (child) => child.type === 'SideMenuLeft';
const isRightChild = (child) => child.type === 'SideMenuRight';
class SideMenuRootNode extends ParentNode_1.default {
    visibleChild;
    constructor(layout, parentNode) {
        super(layout, 'SideMenuRoot', parentNode);
        this.visibleChild = this._getCenterChild();
        if (!this.visibleChild) {
            throw new Error('SideMenuRootNode must have a SideMenuCenter child');
        }
    }
    applyOptions(_options) {
        super.applyOptions(_options);
        this._updateVisibility(_options);
    }
    mergeOptions(options) {
        super.mergeOptions(options);
        this._updateVisibility(options);
    }
    /**
     * @override
     */
    getVisibleLayout() {
        return this.visibleChild.getVisibleLayout();
    }
    _updateVisibility(options) {
        if (options.sideMenu) {
            if (options.sideMenu.left?.visible) {
                this.visibleChild = this._getLeftChild();
                layoutActions.openSideMenu(this.visibleChild);
            }
            else if (options.sideMenu.right?.visible) {
                this.visibleChild = this._getRightChild();
                layoutActions.openSideMenu(this.visibleChild);
            }
            else {
                this.visibleChild = this._getCenterChild();
                layoutActions.closeSideMenu(this.visibleChild);
            }
        }
    }
    _getCenterChild = () => this.children.find(isCenterChild);
    _getLeftChild = () => this.children.find(isLeftChild);
    _getRightChild = () => this.children.find(isRightChild);
}
exports.default = SideMenuRootNode;
class SideMenuNode extends ParentNode_1.default {
    constructor(layout, type, parentNode) {
        super(layout, type, parentNode);
    }
    getVisibleLayout() {
        return this.children[0].getVisibleLayout();
    }
}
exports.SideMenuNode = SideMenuNode;
class SideMenuLeftNode extends SideMenuNode {
    constructor(layout, parentNode) {
        super(layout, 'SideMenuLeft', parentNode);
    }
}
exports.SideMenuLeftNode = SideMenuLeftNode;
class SideMenuRightNode extends SideMenuNode {
    constructor(layout, parentNode) {
        super(layout, 'SideMenuRight', parentNode);
    }
}
exports.SideMenuRightNode = SideMenuRightNode;
class SideMenuCenterNode extends SideMenuNode {
    constructor(layout, parentNode) {
        super(layout, 'SideMenuCenter', parentNode);
    }
}
exports.SideMenuCenterNode = SideMenuCenterNode;
