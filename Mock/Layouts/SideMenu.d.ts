import ParentNode from './ParentNode';
import ComponentNode from './ComponentNode';
import { Options } from '../../src';
import { NodeType } from './Node';
export default class SideMenuRootNode extends ParentNode {
    visibleChild: ParentNode;
    constructor(layout: any, parentNode?: ParentNode);
    applyOptions(_options: Options): void;
    mergeOptions(options: Options): void;
    /**
     * @override
     */
    getVisibleLayout(): ComponentNode;
    _updateVisibility(options: Options): void;
    _getCenterChild: () => ParentNode;
    _getLeftChild: () => ParentNode;
    _getRightChild: () => ParentNode;
}
export declare class SideMenuNode extends ParentNode {
    constructor(layout: any, type: NodeType, parentNode?: ParentNode);
    getVisibleLayout(): ComponentNode;
}
export declare class SideMenuLeftNode extends SideMenuNode {
    constructor(layout: any, parentNode?: ParentNode);
}
export declare class SideMenuRightNode extends SideMenuNode {
    constructor(layout: any, parentNode?: ParentNode);
}
export declare class SideMenuCenterNode extends SideMenuNode {
    constructor(layout: any, parentNode?: ParentNode);
}
