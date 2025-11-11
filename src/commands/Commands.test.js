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
const LayoutProcessor_1 = require("../processors/LayoutProcessor");
const LayoutProcessorsStore_1 = require("../processors/LayoutProcessorsStore");
const CommandName_1 = require("../interfaces/CommandName");
const OptionsCrawler_1 = require("./OptionsCrawler");
const react_1 = tslib_1.__importDefault(require("react"));
describe('Commands', () => {
    let uut;
    let mockedNativeCommandsSender;
    let mockedOptionsProcessor;
    let mockedStore;
    let commandsObserver;
    let mockedUniqueIdProvider;
    let layoutProcessor;
    beforeEach(() => {
        mockedNativeCommandsSender = (0, ts_mockito_1.mock)(NativeCommandsSender_1.NativeCommandsSender);
        mockedUniqueIdProvider = (0, ts_mockito_1.mock)(UniqueIdProvider_1.UniqueIdProvider);
        (0, ts_mockito_1.when)(mockedUniqueIdProvider.generate((0, ts_mockito_1.anything)())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
        const uniqueIdProvider = (0, ts_mockito_1.instance)(mockedUniqueIdProvider);
        mockedStore = (0, ts_mockito_1.mock)(Store_1.Store);
        commandsObserver = new CommandsObserver_1.CommandsObserver(uniqueIdProvider);
        const layoutProcessorsStore = new LayoutProcessorsStore_1.LayoutProcessorsStore();
        mockedOptionsProcessor = (0, ts_mockito_1.mock)(OptionsProcessor_1.OptionsProcessor);
        const optionsProcessor = (0, ts_mockito_1.instance)(mockedOptionsProcessor);
        layoutProcessor = new LayoutProcessor_1.LayoutProcessor(layoutProcessorsStore);
        jest.spyOn(layoutProcessor, 'process');
        uut = new Commands_1.Commands((0, ts_mockito_1.instance)(mockedStore), (0, ts_mockito_1.instance)(mockedNativeCommandsSender), new LayoutTreeParser_1.LayoutTreeParser(uniqueIdProvider), new LayoutTreeCrawler_1.LayoutTreeCrawler((0, ts_mockito_1.instance)(mockedStore), optionsProcessor), commandsObserver, uniqueIdProvider, optionsProcessor, layoutProcessor, new OptionsCrawler_1.OptionsCrawler((0, ts_mockito_1.instance)(mockedStore), uniqueIdProvider));
    });
    describe('setRoot', () => {
        it('sends setRoot to native after parsing into a correct layout tree', () => {
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } },
            });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', (0, ts_mockito_1.deepEqual)({
                root: {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    children: [],
                    data: { name: 'com.example.MyScreen', options: {}, passProps: undefined },
                },
                modals: [],
                overlays: [],
            }))).called();
        });
        it('returns a promise with the resolved layout', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.setRoot((0, ts_mockito_1.anything)(), (0, ts_mockito_1.anything)())).thenResolve('the resolved layout');
            const result = await uut.setRoot({ root: { component: { name: 'com.example.MyScreen' } } });
            expect(result).toEqual('the resolved layout');
        });
        it('inputs modals and overlays', () => {
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } },
                modals: [{ component: { name: 'com.example.MyModal' } }],
                overlays: [{ component: { name: 'com.example.MyOverlay' } }],
            });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.setRoot('setRoot+UNIQUE_ID', (0, ts_mockito_1.deepEqual)({
                root: {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    children: [],
                    data: {
                        name: 'com.example.MyScreen',
                        options: {},
                        passProps: undefined,
                    },
                },
                modals: [
                    {
                        type: 'Component',
                        id: 'Component+UNIQUE_ID',
                        children: [],
                        data: {
                            name: 'com.example.MyModal',
                            options: {},
                            passProps: undefined,
                        },
                    },
                ],
                overlays: [
                    {
                        type: 'Component',
                        id: 'Component+UNIQUE_ID',
                        children: [],
                        data: {
                            name: 'com.example.MyOverlay',
                            options: {},
                            passProps: undefined,
                        },
                    },
                ],
            }))).called();
        });
        it('process layout with layoutProcessor', () => {
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } },
            });
            expect(layoutProcessor.process).toBeCalledWith({ component: { name: 'com.example.MyScreen', options: {}, id: 'Component+UNIQUE_ID' } }, CommandName_1.CommandName.SetRoot);
        });
        it('pass component static options to layoutProcessor', () => {
            (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('com.example.MyScreen')).thenReturn(() => class extends react_1.default.Component {
                static options() {
                    return {
                        topBar: {
                            visible: false,
                        },
                    };
                }
            });
            uut.setRoot({
                root: { component: { name: 'com.example.MyScreen' } },
            });
            expect(layoutProcessor.process).toBeCalledWith({
                component: {
                    id: 'Component+UNIQUE_ID',
                    name: 'com.example.MyScreen',
                    options: { topBar: { visible: false } },
                },
            }, CommandName_1.CommandName.SetRoot);
        });
        it('retains passProps properties identity', () => {
            const obj = { some: 'content' };
            uut.setRoot({ root: { component: { name: 'com.example.MyScreen', passProps: { obj } } } });
            const args = (0, ts_mockito_1.capture)(mockedStore.setPendingProps).last();
            expect(args[1].obj).toBe(obj);
        });
    });
    describe('mergeOptions', () => {
        it('passes options for component', () => {
            uut.mergeOptions('theComponentId', { blurOnUnmount: true });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.mergeOptions('theComponentId', (0, ts_mockito_1.deepEqual)({ blurOnUnmount: true }))).called();
        });
        it('show warning when invoking before componentDidMount', () => {
            jest.spyOn(console, 'warn');
            (0, ts_mockito_1.when)(mockedStore.getComponentInstance('component1')).thenReturn({});
            const componentId = 'component1';
            uut.mergeOptions(componentId, { blurOnUnmount: true });
            expect(console.warn).toBeCalledWith(`Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`);
        });
        it('should not show warning for mounted component', () => {
            jest.spyOn(console, 'warn');
            const componentId = 'component1';
            (0, ts_mockito_1.when)(mockedStore.getComponentInstance('component1')).thenReturn({
                isMounted: true,
            });
            uut.mergeOptions('component1', { blurOnUnmount: true });
            expect(console.warn).not.toBeCalledWith(`Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`);
        });
        it('should not show warning for component id that does not exist', () => {
            jest.spyOn(console, 'warn');
            const componentId = 'component1';
            (0, ts_mockito_1.when)(mockedStore.getComponentInstance('stackId')).thenReturn(undefined);
            uut.mergeOptions('stackId', { blurOnUnmount: true });
            expect(console.warn).not.toBeCalledWith(`Navigation.mergeOptions was invoked on component with id: ${componentId} before it is mounted, this can cause UI issues and should be avoided.\n Use static options instead.`);
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    dismissModal: {
                        enabled: false,
                    },
                },
            };
            uut.mergeOptions('myUniqueId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.MergeOptions, (0, ts_mockito_1.deepEqual)(options), undefined)).called();
        });
        it('processing mergeOptions should pass component props', async () => {
            const options = {
                animations: {
                    dismissModal: {
                        enabled: false,
                    },
                },
            };
            const passProps = { prop: '1' };
            (0, ts_mockito_1.when)(mockedStore.getPropsForId('myUniqueId')).thenReturn(passProps);
            uut.mergeOptions('myUniqueId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.MergeOptions, (0, ts_mockito_1.deepEqual)(options), passProps)).called();
        });
    });
    describe('updateProps', () => {
        it('delegates to store', () => {
            uut.updateProps('theComponentId', { someProp: 'someValue' });
            (0, ts_mockito_1.verify)(mockedStore.updateProps('theComponentId', (0, ts_mockito_1.deepEqual)({ someProp: 'someValue' })));
        });
        it('notifies commands observer', () => {
            uut.updateProps('theComponentId', { someProp: 'someValue' });
            (0, ts_mockito_1.verify)(commandsObserver.notify('updateProps', (0, ts_mockito_1.deepEqual)({ componentId: 'theComponentId', props: { someProp: 'someValue' } })));
        });
        it('update props with callback', () => {
            const callback = jest.fn();
            uut.updateProps('theComponentId', { someProp: 'someValue' }, callback);
            const args = (0, ts_mockito_1.capture)(mockedStore.updateProps).last();
            expect(args[0]).toEqual('theComponentId');
            expect(args[1]).toEqual({ someProp: 'someValue' });
            expect(args[2]).toEqual(callback);
        });
    });
    describe('showModal', () => {
        it('sends command to native after parsing into a correct layout tree', () => {
            uut.showModal({ component: { name: 'com.example.MyScreen' } });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.showModal('showModal+UNIQUE_ID', (0, ts_mockito_1.deepEqual)({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined,
                },
                children: [],
            }))).called();
        });
        it('returns a promise with the resolved layout', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.showModal((0, ts_mockito_1.anything)(), (0, ts_mockito_1.anything)())).thenResolve('the resolved layout');
            const result = await uut.showModal({ component: { name: 'com.example.MyScreen' } });
            expect(result).toEqual('the resolved layout');
        });
        it('process layout with layoutProcessor', () => {
            uut.showModal({ component: { name: 'com.example.MyScreen' } });
            expect(layoutProcessor.process).toBeCalledWith({ component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } }, CommandName_1.CommandName.ShowModal);
        });
        it('retains passProps properties identity', () => {
            const obj = { some: 'content' };
            uut.showModal({ component: { name: 'com.example.MyScreen', passProps: { obj } } });
            const args = (0, ts_mockito_1.capture)(mockedStore.setPendingProps).last();
            expect(args[1].obj).toBe(obj);
        });
    });
    describe('dismissModal', () => {
        it('sends command to native', () => {
            uut.dismissModal('myUniqueId', {});
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.dismissModal('dismissModal+UNIQUE_ID', 'myUniqueId', (0, ts_mockito_1.deepEqual)({}))).called();
        });
        it('returns a promise with the id', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.dismissModal((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)(), (0, ts_mockito_1.anything)())).thenResolve('the id');
            const result = await uut.dismissModal('myUniqueId');
            expect(result).toEqual('the id');
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    dismissModal: {
                        enabled: false,
                    },
                },
            };
            uut.dismissModal('myUniqueId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.DismissModal, options)).called();
        });
    });
    describe('dismissAllModals', () => {
        it('sends command to native', () => {
            uut.dismissAllModals({});
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.dismissAllModals('dismissAllModals+UNIQUE_ID', (0, ts_mockito_1.deepEqual)({}))).called();
        });
        it('returns a promise with the id', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.dismissAllModals((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('the id');
            const result = await uut.dismissAllModals();
            expect(result).toEqual('the id');
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    dismissModal: {
                        enabled: false,
                    },
                },
            };
            uut.dismissAllModals(options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.DismissAllModals, options)).called();
        });
    });
    describe('push', () => {
        it('resolves with the parsed layout', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.push((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('the resolved layout');
            const result = await uut.push('theComponentId', {
                component: { name: 'com.example.MyScreen' },
            });
            expect(result).toEqual('the resolved layout');
        });
        it('parses into correct layout node and sends to native', () => {
            uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.push('push+UNIQUE_ID', 'theComponentId', (0, ts_mockito_1.deepEqual)({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined,
                },
                children: [],
            }))).called();
        });
        it('process layout with layoutProcessor', () => {
            uut.push('theComponentId', { component: { name: 'com.example.MyScreen' } });
            expect(layoutProcessor.process).toBeCalledWith({ component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } }, CommandName_1.CommandName.Push);
        });
        it('retains passProps properties identity', () => {
            const obj = { some: 'content' };
            uut.push('theComponentId', {
                component: { name: 'com.example.MyScreen', passProps: { obj } },
            });
            const args = (0, ts_mockito_1.capture)(mockedStore.setPendingProps).last();
            expect(args[1].obj).toBe(obj);
        });
    });
    describe('pop', () => {
        it('pops a component, passing componentId', () => {
            uut.pop('theComponentId', {});
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', (0, ts_mockito_1.deepEqual)({}))).called();
        });
        it('pops a component, passing componentId and options', () => {
            const options = { popGesture: true };
            uut.pop('theComponentId', options);
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.pop('pop+UNIQUE_ID', 'theComponentId', options)).called();
        });
        it('pop returns a promise that resolves to componentId', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.pop((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('theComponentId');
            const result = await uut.pop('theComponentId', {});
            expect(result).toEqual('theComponentId');
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    pop: {
                        enabled: false,
                    },
                },
            };
            uut.pop('theComponentId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.Pop, options)).called();
        });
    });
    describe('popTo', () => {
        it('pops all components until the passed Id is top', () => {
            uut.popTo('theComponentId', {});
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.popTo('popTo+UNIQUE_ID', 'theComponentId', (0, ts_mockito_1.deepEqual)({}))).called();
        });
        it('returns a promise that resolves to targetId', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.popTo((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('theComponentId');
            const result = await uut.popTo('theComponentId');
            expect(result).toEqual('theComponentId');
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    pop: {
                        enabled: false,
                    },
                },
            };
            uut.popTo('theComponentId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.PopTo, options)).called();
        });
    });
    describe('popToRoot', () => {
        it('pops all components to root', () => {
            uut.popToRoot('theComponentId', {});
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.popToRoot('popToRoot+UNIQUE_ID', 'theComponentId', (0, ts_mockito_1.deepEqual)({}))).called();
        });
        it('returns a promise that resolves to targetId', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.popToRoot((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('theComponentId');
            const result = await uut.popToRoot('theComponentId');
            expect(result).toEqual('theComponentId');
        });
        it('processes mergeOptions', async () => {
            const options = {
                animations: {
                    pop: {
                        enabled: false,
                    },
                },
            };
            uut.popToRoot('theComponentId', options);
            (0, ts_mockito_1.verify)(mockedOptionsProcessor.processOptions(CommandName_1.CommandName.PopToRoot, options)).called();
        });
    });
    describe('setStackRoot', () => {
        it('parses into correct layout node and sends to native', () => {
            uut.setStackRoot('theComponentId', [{ component: { name: 'com.example.MyScreen' } }]);
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.setStackRoot('setStackRoot+UNIQUE_ID', 'theComponentId', (0, ts_mockito_1.deepEqual)([
                {
                    type: 'Component',
                    id: 'Component+UNIQUE_ID',
                    data: {
                        name: 'com.example.MyScreen',
                        options: {},
                        passProps: undefined,
                    },
                    children: [],
                },
            ]))).called();
        });
        it('process layout with layoutProcessor', () => {
            uut.setStackRoot('theComponentId', [{ component: { name: 'com.example.MyScreen' } }]);
            expect(layoutProcessor.process).toBeCalledWith({ component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } }, CommandName_1.CommandName.SetStackRoot);
        });
        it('retains passProps properties identity', () => {
            const obj = { some: 'content' };
            uut.setStackRoot('theComponentId', [
                { component: { name: 'com.example.MyScreen', passProps: { obj } } },
            ]);
            const args = (0, ts_mockito_1.capture)(mockedStore.setPendingProps).last();
            expect(args[1].obj).toBe(obj);
        });
    });
    describe('showOverlay', () => {
        it('sends command to native after parsing into a correct layout tree', () => {
            uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.showOverlay('showOverlay+UNIQUE_ID', (0, ts_mockito_1.deepEqual)({
                type: 'Component',
                id: 'Component+UNIQUE_ID',
                data: {
                    name: 'com.example.MyScreen',
                    options: {},
                    passProps: undefined,
                },
                children: [],
            }))).called();
        });
        it('resolves with the component id', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.showOverlay((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anything)())).thenResolve('Component1');
            const result = await uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
            expect(result).toEqual('Component1');
        });
        it('process layout with layoutProcessor', () => {
            uut.showOverlay({ component: { name: 'com.example.MyScreen' } });
            expect(layoutProcessor.process).toBeCalledWith({ component: { id: 'Component+UNIQUE_ID', name: 'com.example.MyScreen', options: {} } }, CommandName_1.CommandName.ShowOverlay);
        });
        it('retains passProps properties identity', () => {
            const obj = { some: 'content' };
            uut.showOverlay({ component: { name: 'com.example.MyScreen', passProps: { obj } } });
            const args = (0, ts_mockito_1.capture)(mockedStore.setPendingProps).last();
            expect(args[1].obj).toBe(obj);
        });
    });
    describe('dismissOverlay', () => {
        it('check promise returns true', async () => {
            (0, ts_mockito_1.when)(mockedNativeCommandsSender.dismissOverlay((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)())).thenResolve('true');
            const result = await uut.dismissOverlay('Component1');
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.dismissOverlay((0, ts_mockito_1.anyString)(), (0, ts_mockito_1.anyString)())).called();
            expect(result).toEqual('true');
        });
        it('send command to native with componentId', () => {
            uut.dismissOverlay('Component1');
            (0, ts_mockito_1.verify)(mockedNativeCommandsSender.dismissOverlay('dismissOverlay+UNIQUE_ID', 'Component1')).called();
        });
    });
    describe('notifies commandsObserver', () => {
        let cb;
        let mockedLayoutTreeParser;
        let mockedLayoutTreeCrawler;
        beforeEach(() => {
            cb = jest.fn();
            mockedLayoutTreeParser = (0, ts_mockito_1.mock)(LayoutTreeParser_1.LayoutTreeParser);
            mockedLayoutTreeCrawler = (0, ts_mockito_1.mock)(LayoutTreeCrawler_1.LayoutTreeCrawler);
            commandsObserver.register(cb);
            const mockedOptionsProcessor = (0, ts_mockito_1.mock)(OptionsProcessor_1.OptionsProcessor);
            uut = new Commands_1.Commands(mockedStore, mockedNativeCommandsSender, (0, ts_mockito_1.instance)(mockedLayoutTreeParser), (0, ts_mockito_1.instance)(mockedLayoutTreeCrawler), commandsObserver, (0, ts_mockito_1.instance)(mockedUniqueIdProvider), (0, ts_mockito_1.instance)(mockedOptionsProcessor), new LayoutProcessor_1.LayoutProcessor(new LayoutProcessorsStore_1.LayoutProcessorsStore()), new OptionsCrawler_1.OptionsCrawler((0, ts_mockito_1.instance)(mockedStore), mockedUniqueIdProvider));
        });
        function getAllMethodsOfUut() {
            const uutFns = Object.getOwnPropertyNames(Commands_1.Commands.prototype);
            const methods = (0, filter_1.default)(uutFns, (fn) => fn !== 'constructor');
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
                dismissAllOverlays: [{}],
                getLaunchArgs: ['id'],
            };
            const paramsForMethodName = {
                setRoot: {
                    commandId: 'setRoot+UNIQUE_ID',
                    layout: { root: null, modals: [], overlays: [] },
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
                    layout: [null],
                },
                showOverlay: { commandId: 'showOverlay+UNIQUE_ID', layout: null },
                dismissOverlay: { commandId: 'dismissOverlay+UNIQUE_ID', componentId: 'id' },
                dismissAllOverlays: { commandId: 'dismissAllOverlays+UNIQUE_ID' },
                getLaunchArgs: { commandId: 'getLaunchArgs+UNIQUE_ID' },
            };
            (0, forEach_1.default)(getAllMethodsOfUut(), (m) => {
                it(`for ${m}`, () => {
                    expect(argsForMethodName).toHaveProperty(m);
                    expect(paramsForMethodName).toHaveProperty(m);
                    (0, invoke_1.default)(uut, m, ...argsForMethodName[m]);
                    expect(cb).toHaveBeenCalledTimes(1);
                    expect(cb).toHaveBeenCalledWith(m, paramsForMethodName[m]);
                });
            });
        });
    });
});
