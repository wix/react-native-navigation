"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const ComponentRegistry_1 = require("./ComponentRegistry");
const Store_1 = require("./Store");
const ts_mockito_1 = require("ts-mockito");
const ComponentWrapper_1 = require("./ComponentWrapper");
const ComponentEventsObserver_1 = require("../events/ComponentEventsObserver");
const AppRegistryService_1 = require("../adapters/AppRegistryService");
const DummyComponent = () => null;
describe('ComponentRegistry', () => {
    let mockedStore;
    let mockedComponentEventsObserver;
    let mockedComponentWrapper;
    let mockedAppRegistryService;
    let componentWrapper;
    let store;
    let uut;
    beforeEach(() => {
        mockedStore = (0, ts_mockito_1.mock)(Store_1.Store);
        mockedComponentEventsObserver = (0, ts_mockito_1.mock)(ComponentEventsObserver_1.ComponentEventsObserver);
        mockedComponentWrapper = (0, ts_mockito_1.mock)(ComponentWrapper_1.ComponentWrapper);
        mockedAppRegistryService = (0, ts_mockito_1.mock)(AppRegistryService_1.AppRegistryService);
        store = (0, ts_mockito_1.instance)(mockedStore);
        componentWrapper = (0, ts_mockito_1.instance)(mockedComponentWrapper);
        uut = new ComponentRegistry_1.ComponentRegistry(store, (0, ts_mockito_1.instance)(mockedComponentEventsObserver), componentWrapper, (0, ts_mockito_1.instance)(mockedAppRegistryService));
    });
    it('registers component by componentName into AppRegistry', () => {
        uut.registerComponent('example.MyComponent.name', () => DummyComponent);
        (0, ts_mockito_1.verify)(mockedAppRegistryService.registerComponent('example.MyComponent.name', (0, ts_mockito_1.anyFunction)())).called();
    });
    it('saves the wrapper component generator to the store', () => {
        uut.registerComponent('example.MyComponent.name', () => DummyComponent);
        (0, ts_mockito_1.verify)(mockedStore.setComponentClassForName('example.MyComponent.name', (0, ts_mockito_1.anyFunction)())).called();
    });
    it('should not invoke generator', () => {
        const generator = jest.fn(() => DummyComponent);
        uut.registerComponent('example.MyComponent.name', generator);
        expect(generator).toHaveBeenCalledTimes(0);
    });
    it('should wrap component only once', () => {
        uut = new ComponentRegistry_1.ComponentRegistry(new Store_1.Store(), (0, ts_mockito_1.instance)(mockedComponentEventsObserver), componentWrapper, (0, ts_mockito_1.instance)(mockedAppRegistryService));
        componentWrapper.wrap = jest.fn();
        const generator = jest.fn(() => DummyComponent);
        const componentProvider = uut.registerComponent('example.MyComponent.name', generator);
        componentProvider();
        componentProvider();
        expect(componentWrapper.wrap).toHaveBeenCalledTimes(1);
    });
    it('should recreate wrapped component on re-register component', () => {
        uut = new ComponentRegistry_1.ComponentRegistry(new Store_1.Store(), (0, ts_mockito_1.instance)(mockedComponentEventsObserver), new ComponentWrapper_1.ComponentWrapper(), (0, ts_mockito_1.instance)(mockedAppRegistryService));
        const generator = () => DummyComponent;
        const w1 = uut.registerComponent('example.MyComponent.name', generator)();
        const w2 = uut.registerComponent('example.MyComponent.name', generator)();
        expect(w1).not.toBe(w2);
    });
});
