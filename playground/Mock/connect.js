"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.connect = void 0;
const remx_1 = require("remx");
function connect(component) {
    // @ts-ignore
    return (0, remx_1.connect)()(component);
}
exports.connect = connect;
