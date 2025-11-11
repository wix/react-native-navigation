"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const Store_1 = require("../components/Store");
const ts_mockito_1 = require("ts-mockito");
const OptionsCrawler_1 = require("./OptionsCrawler");
const UniqueIdProvider_1 = require("../adapters/UniqueIdProvider");
describe('OptionsCrawler', () => {
    let uut;
    let mockedStore;
    let mockedUniqueIdProvider;
    beforeEach(() => {
        mockedStore = (0, ts_mockito_1.mock)(Store_1.Store);
        mockedUniqueIdProvider = (0, ts_mockito_1.mock)(UniqueIdProvider_1.UniqueIdProvider);
        (0, ts_mockito_1.when)(mockedUniqueIdProvider.generate((0, ts_mockito_1.anything)())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);
        const uniqueIdProvider = (0, ts_mockito_1.instance)(mockedUniqueIdProvider);
        uut = new OptionsCrawler_1.OptionsCrawler((0, ts_mockito_1.instance)(mockedStore), uniqueIdProvider);
    });
    it('Components: injects options object', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options = { popGesture: true };
        });
        const layout = {
            component: {
                id: 'testId',
                name: 'theComponentName',
            },
        };
        uut.crawl(layout);
        expect(layout.component.options).toEqual({ popGesture: true });
    });
    it('Components: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            component: {
                id: 'testId',
                name: 'theComponentName',
            },
        };
        uut.crawl(layout);
        expect(layout.component.options).toEqual({ popGesture: true });
    });
    it('ExternalComponent: does nothing as there is no React component for external component', () => {
        const layout = {
            externalComponent: {
                id: 'testId',
                name: 'theComponentName',
            },
        };
        uut.crawl(layout);
        expect(layout.externalComponent.options).toEqual(undefined);
    });
    it('ExternalComponent: merge options with passed options', () => {
        const layout = {
            externalComponent: {
                id: 'testId',
                name: 'theComponentName',
                options: {
                    popGesture: false,
                },
            },
        };
        uut.crawl(layout);
        expect(layout.externalComponent.options).toEqual({ popGesture: false });
    });
    it('Stack: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            stack: {
                children: [
                    {
                        component: {
                            id: 'testId',
                            name: 'theComponentName',
                        },
                    },
                ],
            },
        };
        uut.crawl(layout);
        expect(layout.stack.children[0].component.options).toEqual({ popGesture: true });
    });
    it('SideMenu: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            sideMenu: {
                left: {
                    component: {
                        id: 'testId',
                        name: 'theComponentName',
                    },
                },
                center: {
                    component: {
                        id: 'testId',
                        name: 'theComponentName',
                    },
                },
                right: {
                    component: {
                        id: 'testId',
                        name: 'theComponentName',
                    },
                },
            },
        };
        uut.crawl(layout);
        expect(layout.sideMenu.center.component.options).toEqual({ popGesture: true });
        expect(layout.sideMenu.left.component.options).toEqual({ popGesture: true });
        expect(layout.sideMenu.right.component.options).toEqual({ popGesture: true });
    });
    it('SplitView: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            splitView: {
                master: {
                    component: {
                        id: 'testId',
                        name: 'theComponentName',
                    },
                },
                detail: {
                    component: {
                        id: 'testId',
                        name: 'theComponentName',
                    },
                },
            },
        };
        uut.crawl(layout);
        expect(layout.splitView.master.component.options).toEqual({ popGesture: true });
        expect(layout.splitView.detail.component.options).toEqual({ popGesture: true });
    });
    it('BottomTabs: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            bottomTabs: {
                children: [
                    {
                        component: {
                            id: 'testId',
                            name: 'theComponentName',
                        },
                    },
                    {
                        component: {
                            id: 'testId',
                            name: 'theComponentName',
                        },
                    },
                ],
            },
        };
        uut.crawl(layout);
        expect(layout.bottomTabs.children[0].component.options).toEqual({ popGesture: true });
        expect(layout.bottomTabs.children[1].component.options).toEqual({ popGesture: true });
    });
    it('TopTabs: injects options from original component class static property', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return { popGesture: true };
            }
        });
        const layout = {
            topTabs: {
                children: [
                    {
                        component: {
                            id: 'testId',
                            name: 'theComponentName',
                        },
                    },
                    {
                        component: {
                            id: 'testId',
                            name: 'theComponentName',
                        },
                    },
                ],
            },
        };
        uut.crawl(layout);
        expect(layout.topTabs.children[0].component.options).toEqual({ popGesture: true });
        expect(layout.topTabs.children[1].component.options).toEqual({ popGesture: true });
    });
    it('Components: merges options from component class static property with passed options, favoring passed options', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options() {
                return {
                    topBar: {
                        title: { text: 'this gets overriden' },
                        subtitle: { text: 'exists only in static' },
                    },
                };
            }
        });
        const node = {
            component: {
                id: 'testId',
                name: 'theComponentName',
                options: {
                    topBar: {
                        title: {
                            text: 'exists only in passed',
                        },
                    },
                },
            },
        };
        uut.crawl(node);
        expect(node.component.options).toEqual({
            topBar: {
                title: {
                    text: 'exists only in passed',
                },
                subtitle: {
                    text: 'exists only in static',
                },
            },
        });
    });
    it('Components: options default obj', () => {
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
        });
        const node = {
            component: { name: 'theComponentName', options: {}, id: 'testId' },
            children: [],
        };
        uut.crawl(node);
        expect(node.component.options).toEqual({});
    });
    it('Components: should generate component id', () => {
        let componentIdInProps = '';
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options(props) {
                componentIdInProps = props.componentId;
            }
        });
        const node = {
            component: { name: 'theComponentName', options: {}, id: undefined },
            children: [],
        };
        uut.crawl(node);
        expect(componentIdInProps).toEqual('Component+UNIQUE_ID');
    });
    it('componentId is included in props passed to options generator', () => {
        let componentIdInProps = '';
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options(props) {
                componentIdInProps = props.componentId;
                return {};
            }
        });
        const node = {
            component: {
                id: 'testId',
                name: 'theComponentName',
                passProps: { someProp: 'here' },
            },
        };
        uut.crawl(node);
        expect(componentIdInProps).toEqual('testId');
    });
    it('componentId does not override componentId in passProps', () => {
        let componentIdInProps = '';
        (0, ts_mockito_1.when)(mockedStore.getComponentClassForName('theComponentName')).thenReturn(() => class extends React.Component {
            static options(props) {
                componentIdInProps = props.componentId;
                return {};
            }
        });
        const node = {
            component: {
                id: 'testId',
                name: 'theComponentName',
                passProps: {
                    someProp: 'here',
                    componentId: 'compIdFromPassProps',
                },
            },
        };
        uut.crawl(node);
        expect(componentIdInProps).toEqual('compIdFromPassProps');
    });
});
