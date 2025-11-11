import { OptionsTopBarButton } from '../../src/interfaces/Options';
import { Options } from '../../src';
import ComponentNode from './ComponentNode';
import Node, { NodeType } from './Node';
export default class ParentNode extends Node {
    children: ParentNode[];
    constructor(layout: any, type: NodeType, parentNode?: ParentNode);
    componentDidMount(): void;
    componentDidAppear(): void;
    componentDidDisappear(): void;
    getVisibleLayout(): ComponentNode;
    getTopParent(): Node;
    applyOptions(_options: Options): void;
    mergeOptions(options: Options): void;
    buttonsChanged(_oldButtons: OptionsTopBarButton[], _newButtons: OptionsTopBarButton[]): void;
    titleChanged(_oldComponent: any, _newComponent: any): void;
    resolveOptions(): Options;
    getStack(): ParentNode | undefined;
    getBottomTabs(): ParentNode | undefined;
}
