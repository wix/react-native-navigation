"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const CommandsObserver_1 = require("./CommandsObserver");
const UniqueIdProvider_1 = require("../adapters/UniqueIdProvider");
describe('CommandsObserver', () => {
    let uut;
    let cb1;
    let cb2;
    beforeEach(() => {
        cb1 = jest.fn();
        cb2 = jest.fn();
        uut = new CommandsObserver_1.CommandsObserver(new UniqueIdProvider_1.UniqueIdProvider());
    });
    it('register and notify listener', () => {
        const theCommandName = 'theCommandName';
        const theParams = { x: 1 };
        uut.register(cb1);
        uut.register(cb2);
        expect(cb1).toHaveBeenCalledTimes(0);
        expect(cb2).toHaveBeenCalledTimes(0);
        uut.notify(theCommandName, theParams);
        expect(cb1).toHaveBeenCalledTimes(1);
        expect(cb1).toHaveBeenCalledWith(theCommandName, theParams);
        expect(cb2).toHaveBeenCalledTimes(1);
        expect(cb2).toHaveBeenCalledWith(theCommandName, theParams);
    });
    it('remove listener', () => {
        expect(cb1).toHaveBeenCalledTimes(0);
        expect(cb2).toHaveBeenCalledTimes(0);
        uut.register(cb1);
        const cb2Subscription = uut.register(cb2);
        uut.notify('commandName', {});
        expect(cb1).toHaveBeenCalledTimes(1);
        expect(cb2).toHaveBeenCalledTimes(1);
        cb2Subscription.remove();
        uut.notify('commandName', {});
        expect(cb1).toHaveBeenCalledTimes(2);
        expect(cb2).toHaveBeenCalledTimes(1);
    });
});
