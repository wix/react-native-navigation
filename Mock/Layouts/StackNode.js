"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const ParentNode_1 = tslib_1.__importDefault(require("./ParentNode"));
class StackNode extends ParentNode_1.default {
    constructor(layout, parentNode) {
        super(layout, 'Stack', parentNode);
    }
    getVisibleLayout() {
        return this.children[this.children.length - 1].getVisibleLayout();
    }
}
exports.default = StackNode;
