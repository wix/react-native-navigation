"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const OptionsProcessor_1 = require("./OptionsProcessor");
const UniqueIdProvider_1 = require("../adapters/UniqueIdProvider");
const Store_1 = require("../components/Store");
const Options_1 = require("../interfaces/Options");
const ts_mockito_1 = require("ts-mockito");
const ColorService_1 = require("../adapters/ColorService");
const AssetResolver_1 = require("../adapters/AssetResolver");
describe('navigation options', () => {
    let uut;
    const mockedStore = ts_mockito_1.mock(Store_1.Store);
    const store = ts_mockito_1.instance(mockedStore);
    beforeEach(() => {
        const mockedAssetService = ts_mockito_1.mock(AssetResolver_1.AssetService);
        ts_mockito_1.when(mockedAssetService.resolveFromRequire(ts_mockito_1.anyNumber())).thenReturn({
            height: 100,
            scale: 1,
            uri: 'lol',
            width: 100
        });
        const assetService = ts_mockito_1.instance(mockedAssetService);
        const mockedColorService = ts_mockito_1.mock(ColorService_1.ColorService);
        ts_mockito_1.when(mockedColorService.toNativeColor(ts_mockito_1.anyString())).thenReturn(666);
        const colorService = ts_mockito_1.instance(mockedColorService);
        uut = new OptionsProcessor_1.OptionsProcessor(store, new UniqueIdProvider_1.UniqueIdProvider(), colorService, assetService);
    });
    it('keeps original values if values were not processed', () => {
        const options = {
            blurOnUnmount: false,
            popGesture: false,
            modalPresentationStyle: Options_1.OptionsModalPresentationStyle.fullScreen,
            animations: { dismissModal: { alpha: { from: 0, to: 1 } } },
        };
        uut.processOptions(options);
        expect(options).toEqual({
            blurOnUnmount: false,
            popGesture: false,
            modalPresentationStyle: Options_1.OptionsModalPresentationStyle.fullScreen,
            animations: { dismissModal: { alpha: { from: 0, to: 1 } } },
        });
    });
    it('processes color keys', () => {
        const options = {
            statusBar: { backgroundColor: 'red' },
            topBar: { background: { color: 'blue' } },
        };
        uut.processOptions(options);
        expect(options).toEqual({
            statusBar: { backgroundColor: 666 },
            topBar: { background: { color: 666 } },
        });
    });
    it('processes image keys', () => {
        const options = {
            backgroundImage: 123,
            rootBackgroundImage: 234,
            bottomTab: { icon: 345, selectedIcon: 345 },
        };
        uut.processOptions(options);
        expect(options).toEqual({
            backgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
            rootBackgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
            bottomTab: {
                icon: { height: 100, scale: 1, uri: 'lol', width: 100 },
                selectedIcon: { height: 100, scale: 1, uri: 'lol', width: 100 }
            }
        });
    });
    it('calls store if component has passProps', () => {
        const passProps = { some: 'thing' };
        const options = { topBar: { title: { component: { passProps, name: 'a' } } } };
        uut.processOptions(options);
        ts_mockito_1.verify(mockedStore.updateProps('CustomComponent1', passProps)).called();
    });
    it('generates componentId for component id was not passed', () => {
        const options = { topBar: { title: { component: { name: 'a' } } } };
        uut.processOptions(options);
        expect(options).toEqual({
            topBar: { title: { component: { name: 'a', componentId: 'CustomComponent1' } } },
        });
    });
    it('copies passed id to componentId key', () => {
        const options = { topBar: { title: { component: { name: 'a', id: 'Component1' } } } };
        uut.processOptions(options);
        expect(options).toEqual({
            topBar: { title: { component: { name: 'a', id: 'Component1', componentId: 'Component1' } } },
        });
    });
    it('calls store when button has passProps and id', () => {
        const passProps = { prop: 'prop' };
        const options = { topBar: { rightButtons: [{ passProps, id: '1' }] } };
        uut.processOptions(options);
        ts_mockito_1.verify(mockedStore.updateProps('1', passProps)).called();
    });
    it('do not touch passProps when id for button is missing', () => {
        const passProps = { prop: 'prop' };
        const options = { topBar: { rightButtons: [{ passProps }] } };
        uut.processOptions(options);
        expect(options).toEqual({ topBar: { rightButtons: [{ passProps }] } });
    });
    it('omits passProps when processing buttons or components', () => {
        const options = {
            topBar: {
                rightButtons: [{ passProps: {}, id: 'btn1' }],
                leftButtons: [{ passProps: {}, id: 'btn2' }],
                title: { component: { name: 'helloThere1', passProps: {} } },
                background: { component: { name: 'helloThere2', passProps: {} } },
            },
        };
        uut.processOptions(options);
        expect(options.topBar.rightButtons[0].passProps).toBeUndefined();
        expect(options.topBar.leftButtons[0].passProps).toBeUndefined();
        expect(options.topBar.title.component.passProps).toBeUndefined();
        expect(options.topBar.background.component.passProps).toBeUndefined();
    });
});
