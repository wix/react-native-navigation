"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const forEach_1 = tslib_1.__importDefault(require("lodash/forEach"));
const filter_1 = tslib_1.__importDefault(require("lodash/filter"));
const invoke_1 = tslib_1.__importDefault(require("lodash/invoke"));
const ts_mockito_1 = require("ts-mockito");
const LayoutTreeParser_1 = require("./LayoutTreeParser");
const LayoutTreeCrawler_1 = require("./LayoutTreeCrawler");
const Store_1 = require("../components/Store");
const Commands_1 = require("./Commands");
const CommandsObserver_1 = require("../events/CommandsObserver");
const NativeCommandsSender_1 = require("../adapters/NativeCommandsSender");
const OptionsProcessor_1 = require("./OptionsProcessor");
const UniqueIdProvider_1 = require("../adapters/UniqueIdProvider");
describe('Commands', () => {
    let uut;
    let mockedNativeCommandsSender;
    let mockedStore;
    let commandsObserver;
    let mockedUniqueIdProvider;
    beforeEach(() => {
        mockedNativeCommandsSender = ts_mockito_1.mock(NativeCommandsSender_1.NativeCommandsSender);
        mockedUniqueIdProvider = ts_mockito_1.mock(UniqueIdProvider_1.UniqueIdProvider);
        ts_mockito_1.when(mockedUniqueIdProvider.generate(ts_mockito_1.anything())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
        const uniqueIdProvider = ts_mockito_1.instance(mockedUniqueIdProvider);
        mockedStore = ts_mockito_1.mock(Store_1.Store);
        commandsObserver = new CommandsObserver_1.CommandsObserver(uniqueIdProvider);
        const mockedOptionsProcessor = ts_mockito_1.mock(OptionsProcessor_1.OptionsProcessor);
        const optionsProcessor = ts_mockito_1.instance(mockedOptionsProcessor);
        uut = new Commands_1.Commands(mockedStore, ts_mockito_1.instance(mockedNativeCommandsSender), new LayoutTreeParser_1.LayoutTreeParser(uniqueIdProvider), new LayoutTreeCrawler_1.LayoutTreeCrawler(ts_mockito_1.instance(mockedStore), optionsProcessor), commandsObserver, uniqueIdProvider, optionsProcessor);
    });
    describe('setRoot', () => {
        it('sends setRoot to native after parsing into a correct layout tree', () => {
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } }
            });
            ts_mockito_1.verify(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', ts_mockito_1.deepEqual({
                root: {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    children: [],
                    data: { name: 'com.example.MyScreen', options: {}, passProps: undefined }
                },
                modals: [],
                overlays: []
            }))).called();
        });
        it('returns a promise with the resolved layout', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.setRoot(ts_mockito_1.anything(), ts_mockito_1.anything())).thenResolve('the resolved layout');
            const result = await uut.setRoot({ root: { component: { name: 'com.example.MyScreen' } } });
            expect(result).toEqual('the resolved layout');
        });
        it('inputs modals and overlays', () => {
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } },
                modals: [{ component: { name: 'com.example.MyModal' } }],
                overlays: [{ component: { name: 'com.example.MyOverlay' } }]
            });
            ts_mockito_1.verify(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', ts_mockito_1.deepEqual({
                root: {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    children: [],
                    data: {
                        name: 'com.example.MyScreen',
                        options: {},
                        passProps: undefined
                    }
                },
                modals: [
                    {
                        type: 'Component',
                        id: 'Component+UNIQUE_ID',
                        children: [],
                        data: {
                            name: 'com.example.MyModal',
                            options: {},
                            passProps: undefined
                        }
                    }
                ],
                overlays: [
                    {
                        type: 'Component',
                        id: 'Component+UNIQUE_ID',
                        children: [],
                        data: {
                            name: 'com.example.MyOverlay',
                            options: {},
                            passProps: undefined
                        }
                    }
                ]
            }))).called();
        });
    });
    describe('mergeOptions', () => {
        it('passes options for component', () => {
            uut.mergeOptions('theComponentId', { blurOnUnmount: true });
            ts_mockito_1.verify(mockedNativeCommandsSender.mergeOptions('theComponentId', ts_mockito_1.deepEqual({ blurOnUnmount: true }))).called();
        });
    });
    describe('updateProps', () => {
        it('delegates to store', () => {
            uut.updateProps('theComponentId', { someProp: 'someValue' });
            ts_mockito_1.verify(mockedStore.updateProps('theComponentId', ts_mockito_1.deepEqual({ someProp: 'someValue' })));
        });
        it('notifies commands observer', () => {
            uut.updateProps('theComponentId', { someProp: 'someValue' });
            ts_mockito_1.verify(commandsObserver.notify('updateProps', ts_mockito_1.deepEqual({ componentId: 'theComponentId', props: { someProp: 'someValue' } })));
        });
    });
    describe('showModal', () => {
        it('sends command to native after parsing into a correct layout tree', () => {
            uut.showModal({ component: { name: 'com.example.MyScreen' } });
            ts_mockito_1.verify(mockedNativeCommandsSender.showModal('showModal+UNIQUE_ID', ts_mockito_1.deepEqual({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined
                },
                children: []
            }))).called();
        });
        it('returns a promise with the resolved layout', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.showModal(ts_mockito_1.anything(), ts_mockito_1.anything())).thenResolve('the resolved layout');
            const result = await uut.showModal({ component: { name: 'com.example.MyScreen' } });
            expect(result).toEqual('the resolved layout');
        });
    });
    describe('dismissModal', () => {
        it('sends command to native', () => {
            uut.dismissModal('myUniqueId', {});
            ts_mockito_1.verify(mockedNativeCommandsSender.dismissModal('dismissModal+UNIQUE_ID', 'myUniqueId', ts_mockito_1.deepEqual({}))).called();
        });
        it('returns a promise with the id', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.dismissModal(ts_mockito_1.anyString(), ts_mockito_1.anything(), ts_mockito_1.anything())).thenResolve('the id');
            const result = await uut.dismissModal('myUniqueId');
            expect(result).toEqual('the id');
        });
    });
    describe('dismissAllModals', () => {
        it('sends command to native', () => {
            uut.dismissAllModals({});
            ts_mockito_1.verify(mockedNativeCommandsSender.dismissAllModals('dismissAllModals+UNIQUE_ID', ts_mockito_1.deepEqual({}))).called();
        });
        it('returns a promise with the id', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.dismissAllModals(ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('the id');
            const result = await uut.dismissAllModals();
            expect(result).toEqual('the id');
        });
    });
    describe('push', () => {
        it('resolves with the parsed layout', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.push(ts_mockito_1.anyString(), ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('the resolved layout');
            const result = await uut.push('theComponentId', {
                component: { name: 'com.example.MyScreen' }
            });
            expect(result).toEqual('the resolved layout');
        });
        it('parses into correct layout node and sends to native', () => {
            uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
            ts_mockito_1.verify(mockedNativeCommandsSender.push('push+UNIQUE_ID', 'theComponentId', ts_mockito_1.deepEqual({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined
                },
                children: []
            }))).called();
        });
    });
    describe('pop', () => {
        it('pops a component, passing componentId', () => {
            uut.pop('theComponentId', {});
            ts_mockito_1.verify(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', ts_mockito_1.deepEqual({}))).called();
        });
        it('pops a component, passing componentId and options', () => {
            const options = { popGesture: true };
            uut.pop('theComponentId', options);
            ts_mockito_1.verify(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', options)).called();
        });
        it('pop returns a promise that resolves to componentId', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.pop(ts_mockito_1.anyString(), ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('theComponentId');
            const result = await uut.pop('theComponentId', {});
            expect(result).toEqual('theComponentId');
        });
    });
    describe('popTo', () => {
        it('pops all components until the passed Id is top', () => {
            uut.popTo('theComponentId', {});
            ts_mockito_1.verify(mockedNativeCommandsSender.popTo('popTo+UNIQUE_ID', 'theComponentId', ts_mockito_1.deepEqual({}))).called();
        });
        it('returns a promise that resolves to targetId', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.popTo(ts_mockito_1.anyString(), ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('theComponentId');
            const result = await uut.popTo('theComponentId');
            expect(result).toEqual('theComponentId');
        });
    });
    describe('popToRoot', () => {
        it('pops all components to root', () => {
            uut.popToRoot('theComponentId', {});
            ts_mockito_1.verify(mockedNativeCommandsSender.popToRoot('popToRoot+UNIQUE_ID', 'theComponentId', ts_mockito_1.deepEqual({}))).called();
        });
        it('returns a promise that resolves to targetId', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.popToRoot(ts_mockito_1.anyString(), ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('theComponentId');
            const result = await uut.popToRoot('theComponentId');
            expect(result).toEqual('theComponentId');
        });
    });
    describe('setStackRoot', () => {
        it('parses into correct layout node and sends to native', () => {
            uut.setStackRoot('theComponentId', [{ component: { name: 'com.example.MyScreen' } }]);
            ts_mockito_1.verify(mockedNativeCommandsSender.setStackRoot('setStackRoot+UNIQUE_ID', 'theComponentId', ts_mockito_1.deepEqual([
                {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    data: {
                        name: 'com.example.MyScreen',
                        options: {},
                        passProps: undefined
                    },
                    children: []
                }
            ]))).called();
        });
    });
    describe('showOverlay', () => {
        it('sends command to native after parsing into a correct layout tree', () => {
            uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
            ts_mockito_1.verify(mockedNativeCommandsSender.showOverlay('showOverlay+UNIQUE_ID', ts_mockito_1.deepEqual({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined
                },
                children: []
            }))).called();
        });
        it('resolves with the component id', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.showOverlay(ts_mockito_1.anyString(), ts_mockito_1.anything())).thenResolve('Component1');
            const result = await uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
            expect(result).toEqual('Component1');
        });
    });
    describe('dismissOverlay', () => {
        it('check promise returns true', async () => {
            ts_mockito_1.when(mockedNativeCommandsSender.dismissOverlay(ts_mockito_1.anyString(), ts_mockito_1.anyString())).thenResolve(true);
            const result = await uut.dismissOverlay('Component1');
            ts_mockito_1.verify(mockedNativeCommandsSender.dismissOverlay(ts_mockito_1.anyString(), ts_mockito_1.anyString())).called();
            expect(result).toEqual(true);
        });
        it('send command to native with componentId', () => {
            uut.dismissOverlay('Component1');
            ts_mockito_1.verify(mockedNativeCommandsSender.dismissOverlay('dismissOverlay+UNIQUE_ID', 'Component1')).called();
        });
    });
    describe('notifies commandsObserver', () => {
        let cb;
        let mockedLayoutTreeParser;
        let mockedLayoutTreeCrawler;
        let anotherMockedUniqueIdProvider;
        beforeEach(() => {
            cb = jest.fn();
            mockedLayoutTreeParser = ts_mockito_1.mock(LayoutTreeParser_1.LayoutTreeParser);
            mockedLayoutTreeCrawler = ts_mockito_1.mock(LayoutTreeCrawler_1.LayoutTreeCrawler);
            commandsObserver.register(cb);
            const mockedOptionsProcessor = ts_mockito_1.mock(OptionsProcessor_1.OptionsProcessor);
            anotherMockedUniqueIdProvider = ts_mockito_1.mock(UniqueIdProvider_1.UniqueIdProvider);
            ts_mockito_1.when(anotherMockedUniqueIdProvider.generate(ts_mockito_1.anything())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
            uut = new Commands_1.Commands(mockedStore, mockedNativeCommandsSender, ts_mockito_1.instance(mockedLayoutTreeParser), ts_mockito_1.instance(mockedLayoutTreeCrawler), commandsObserver, ts_mockito_1.instance(anotherMockedUniqueIdProvider), ts_mockito_1.instance(mockedOptionsProcessor));
        });
        function getAllMethodsOfUut() {
            const uutFns = Object.getOwnPropertyNames(Commands_1.Commands.prototype);
            const methods = filter_1.default(uutFns, (fn) => fn !== 'constructor');
            expect(methods.length).toBeGreaterThan(1);
            return methods;
        }
        describe('passes correct params', () => {
            const argsForMethodName = {
                setRoot: [{}],
                setDefaultOptions: [{}],
                mergeOptions: ['id', {}],
                updateProps: ['id', {}],
                showModal: [{}],
                dismissModal: ['id', {}],
                dismissAllModals: [{}],
                push: ['id', {}],
                pop: ['id', {}],
                popTo: ['id', {}],
                popToRoot: ['id', {}],
                setStackRoot: ['id', [{}]],
                showOverlay: [{}],
                dismissOverlay: ['id'],
                getLaunchArgs: ['id']
            };
            const paramsForMethodName = {
                setRoot: {
                    commandId: 'setRoot+UNIQUE_ID',
                    layout: { root: null, modals: [], overlays: [] }
                },
                setDefaultOptions: { options: {} },
                mergeOptions: { componentId: 'id', options: {} },
                updateProps: { componentId: 'id', props: {} },
                showModal: { commandId: 'showModal+UNIQUE_ID', layout: null },
                dismissModal: { commandId: 'dismissModal+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
                dismissAllModals: { commandId: 'dismissAllModals+UNIQUE_ID', mergeOptions: {} },
                push: { commandId: 'push+UNIQUE_ID', componentId: 'id', layout: null },
                pop: { commandId: 'pop+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
                popTo: { commandId: 'popTo+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
                popToRoot: { commandId: 'popToRoot+UNIQUE_ID', componentId: 'id', mergeOptions: {} },
                setStackRoot: {
                    commandId: 'setStackRoot+UNIQUE_ID',
                    componentId: 'id',
                    layout: [null]
                },
                showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: null },
                dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
                getLaunchArgs: { commandId: 'getLaunchArgs+UNIQUE_ID' }
            };
            forEach_1.default(getAllMethodsOfUut(), (m) => {
                it(`for ${m}`, () => {
                    expect(argsForMethodName).toHaveProperty(m);
                    expect(paramsForMethodName).toHaveProperty(m);
                    invoke_1.default(uut, m, ...argsForMethodName[m]);
                    expect(cb).toHaveBeenCalledTimes(1);
                    expect(cb).toHaveBeenCalledWith(m, paramsForMethodName[m]);
                });
            });
        });
    });
});
