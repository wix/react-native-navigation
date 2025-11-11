"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const OptionProcessorsStore_1 = require("./OptionProcessorsStore");
describe('Option processors Store', () => {
    let uut;
    beforeEach(() => {
        uut = new OptionProcessorsStore_1.OptionProcessorsStore();
    });
    it('should register processor to store', () => {
        const processor = (value, _commandName) => value;
        uut.addProcessor('topBar', processor);
        expect(uut.getProcessors('topBar')).toEqual([processor]);
    });
    it('should register multiple processors with the same object path', () => {
        const processor = (value, _commandName) => value;
        const secondProcessor = (value, _commandName) => value;
        uut.addProcessor('topBar', processor);
        uut.addProcessor('topBar', secondProcessor);
        expect(uut.getProcessors('topBar')).toEqual([processor, secondProcessor]);
    });
    it('should unregister processor', () => {
        const processor = (value, _commandName) => value;
        const { remove } = uut.addProcessor('topBar', processor);
        expect(uut.getProcessors('topBar')).toEqual([processor]);
        remove();
        expect(uut.getProcessors('topBar')).toEqual([]);
    });
});
