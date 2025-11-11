import ParentNode from './ParentNode';
export default class StackNode extends ParentNode {
    constructor(layout: any, parentNode?: ParentNode);
    getVisibleLayout(): import("./ComponentNode").default;
}
