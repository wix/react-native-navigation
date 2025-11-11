"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const LayoutProcessorsStore_1 = require("./LayoutProcessorsStore");
describe('Layout processors Store', () => {
    let uut;
    beforeEach(() => {
        uut = new LayoutProcessorsStore_1.LayoutProcessorsStore();
    });
    it('should register processor to store', () => {
        const processor = (value, _commandName) => value;
        uut.addProcessor(processor);
        expect(uut.getProcessors()).toEqual([processor]);
    });
    it('should register multiple processors', () => {
        const processor = (value, _commandName) => value;
        const secondProcessor = (value, _commandName) => value;
        uut.addProcessor(processor);
        uut.addProcessor(secondProcessor);
        expect(uut.getProcessors()).toEqual([processor, secondProcessor]);
    });
    it('should unregister processor', () => {
        const processor = (value, _commandName) => value;
        const { remove } = uut.addProcessor(processor);
        expect(uut.getProcessors()).toEqual([processor]);
        remove();
        expect(uut.getProcessors()).toEqual([]);
    });
});
