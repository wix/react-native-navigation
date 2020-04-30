"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const LayoutType_1 = require("./LayoutType");
const LayoutTreeCrawler_1 = require("./LayoutTreeCrawler");
const Store_1 = require("../components/Store");
const ts_mockito_1 = require("ts-mockito");
const OptionsProcessor_1 = require("./OptionsProcessor");
describe('LayoutTreeCrawler', () => {
    let uut;
    let mockedStore;
    let mockedOptionsProcessor;
    beforeEach(() => {
        mockedStore = ts_mockito_1.mock(Store_1.Store);
        mockedOptionsProcessor = ts_mockito_1.mock(OptionsProcessor_1.OptionsProcessor);
        uut = new LayoutTreeCrawler_1.LayoutTreeCrawler(ts_mockito_1.instance(mockedStore), ts_mockito_1.instance(mockedOptionsProcessor));
    });
    it('saves passProps into store for Component nodes', () => {
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.BottomTabs,
            children: [
                {
                    id: 'testId',
                    type: LayoutType_1.LayoutType.Component,
                    data: { name: 'the name', passProps: { myProp: 123 } },
                    children: []
                }
            ],
            data: {}
        };
        uut.crawl(node);
        ts_mockito_1.verify(mockedStore.updateProps('testId', ts_mockito_1.deepEqual({ myProp: 123 }))).called();
    });
    it('Components: injects options from original component class static property', () => {
        ts_mockito_1.when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: { name: 'theComponentName', options: {} },
            children: []
        };
        uut.crawl(node);
        expect(node.data.options).toEqual({ popGesture: true });
    });
    it('Components: injects options from original component class static property', () => {
        ts_mockito_1.when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => { var _a; return _a = class extends React.Component {
            },
            _a.options = {
                popGesture: true
            },
            _a; });
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: { name: 'theComponentName', options: {} },
            children: []
        };
        uut.crawl(node);
        expect(node.data.options).toEqual({ popGesture: true });
    });
    it('Components: crawl does not cache options', () => {
        ts_mockito_1.when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options(props) {
                return { topBar: { title: { text: props.title } } };
            }
        });
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: { name: 'theComponentName', options: {}, passProps: { title: 'title' } },
            children: []
        };
        uut.crawl(node);
        expect(node.data.options).toEqual({ topBar: { title: { text: 'title' } } });
        const node2 = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: { name: 'theComponentName', options: {} },
            children: []
        };
        uut.crawl(node2);
        expect(node2.data.options).toEqual({ topBar: { title: {} } });
    });
    it('Components: merges options from component class static property with passed options, favoring passed options', () => {
        ts_mockito_1.when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return {
                    bazz: 123,
                    inner: { foo: 'this gets overriden' },
                    opt: 'exists only in static'
                };
            }
        });
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: {
                name: 'theComponentName',
                options: {
                    aaa: 'exists only in passed',
                    bazz: 789,
                    inner: { foo: 'this should override same keys' }
                }
            },
            children: []
        };
        uut.crawl(node);
        expect(node.data.options).toEqual({
            aaa: 'exists only in passed',
            bazz: 789,
            inner: { foo: 'this should override same keys' },
            opt: 'exists only in static'
        });
    });
    it('Components: must contain data name', () => {
        const node = { type: LayoutType_1.LayoutType.Component, data: {}, children: [], id: 'testId' };
        expect(() => uut.crawl(node)).toThrowError('Missing component data.name');
    });
    it('Components: options default obj', () => {
        ts_mockito_1.when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
        });
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: { name: 'theComponentName', options: {} },
            children: []
        };
        uut.crawl(node);
        expect(node.data.options).toEqual({});
    });
    it('Components: omits passProps after processing so they are not passed over the bridge', () => {
        const node = {
            id: 'testId',
            type: LayoutType_1.LayoutType.Component,
            data: {
                name: 'compName',
                passProps: { someProp: 'here' }
            },
            children: []
        };
        uut.crawl(node);
        expect(node.data.passProps).toBeUndefined();
    });
});
