"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const LayoutType_1 = require("./LayoutType");
describe('LayoutType', () => {
    it('is an enum', () => {
        expect(LayoutType_1.LayoutType.Component).toEqual('Component');
        expect(LayoutType_1.LayoutType.Stack).toEqual('Stack');
        const name = 'Stack';
        expect(LayoutType_1.LayoutType[name]).toEqual(LayoutType_1.LayoutType.Stack);
    });
});
