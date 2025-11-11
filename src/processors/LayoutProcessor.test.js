"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const LayoutProcessor_1 = require("./LayoutProcessor");
const LayoutProcessorsStore_1 = require("./LayoutProcessorsStore");
const CommandName_1 = require("../interfaces/CommandName");
describe('Layout processor', () => {
    let uut;
    let layoutProcessorsStore;
    beforeEach(() => {
        layoutProcessorsStore = new LayoutProcessorsStore_1.LayoutProcessorsStore();
        uut = new LayoutProcessor_1.LayoutProcessor(layoutProcessorsStore);
    });
    it('do nothing when store is empty', () => {
        const layout = {
            component: {
                name: 'component',
            },
        };
        uut.process(layout, CommandName_1.CommandName.SetRoot);
        expect(layout).toEqual({
            component: {
                name: 'component',
            },
        });
    });
    it('manipulates layout with external processor', () => {
        const layout = {
            component: {
                name: 'component',
            },
        };
        layoutProcessorsStore.addProcessor((_layout, commandName) => {
            return {
                component: {
                    name: 'component1',
                    passProps: {
                        commandName,
                    },
                },
            };
        });
        const result = uut.process(layout, CommandName_1.CommandName.SetRoot);
        expect(result).toEqual({
            component: {
                name: 'component1',
                passProps: {
                    commandName: CommandName_1.CommandName.SetRoot,
                },
            },
        });
    });
});
