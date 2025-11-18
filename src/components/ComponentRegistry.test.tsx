import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';
import { mock, instance, verify, anyFunction } from 'ts-mockito';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { ComponentProvider } from 'react-native';

const DummyComponent = () => null;

describe('ComponentRegistry', () => {
  let mockedStore: Store;
  let mockedComponentEventsObserver: ComponentEventsObserver;
  let mockedComponentWrapper: ComponentWrapper;
  let mockedAppRegistryService: AppRegistryService;
  let componentWrapper: ComponentWrapper;
  let store: Store;
  let uut: ComponentRegistry;

  beforeEach(() => {
    mockedStore = mock(Store);
    mockedComponentEventsObserver = mock(ComponentEventsObserver);
    mockedComponentWrapper = mock(ComponentWrapper);
    mockedAppRegistryService = mock(AppRegistryService);
    store = instance(mockedStore);
    componentWrapper = instance(mockedComponentWrapper);

    uut = new ComponentRegistry(
      store,
      instance(mockedComponentEventsObserver),
      componentWrapper,
      instance(mockedAppRegistryService)
    );
  });

  it('registers component by componentName into AppRegistry', () => {
    uut.registerComponent('example.MyComponent.name', () => DummyComponent);
    verify(
      mockedAppRegistryService.registerComponent('example.MyComponent.name', anyFunction())
    ).called();
  });

  it('saves the wrapper component generator to the store', () => {
    uut.registerComponent('example.MyComponent.name', () => DummyComponent);
    verify(
      mockedStore.setComponentClassForName('example.MyComponent.name', anyFunction())
    ).called();
  });

  it('should not invoke generator', () => {
    const generator: ComponentProvider = jest.fn(() => DummyComponent);
    uut.registerComponent('example.MyComponent.name', generator);
    expect(generator).toHaveBeenCalledTimes(0);
  });

  it('should wrap component only once', () => {
    uut = new ComponentRegistry(
      new Store(),
      instance(mockedComponentEventsObserver),
      componentWrapper,
      instance(mockedAppRegistryService)
    );

    componentWrapper.wrap = jest.fn();

    const generator: ComponentProvider = jest.fn(() => DummyComponent);
    const componentProvider = uut.registerComponent('example.MyComponent.name', generator);
    componentProvider();
    componentProvider();

    expect(componentWrapper.wrap).toHaveBeenCalledTimes(1);
  });

  it('should recreate wrapped component on re-register component', () => {
    uut = new ComponentRegistry(
      new Store(),
      instance(mockedComponentEventsObserver),
      new ComponentWrapper(),
      instance(mockedAppRegistryService)
    );

    const generator: ComponentProvider = () => DummyComponent;
    const w1 = uut.registerComponent('example.MyComponent.name', generator)();
    const w2 = uut.registerComponent('example.MyComponent.name', generator)();

    expect(w1).not.toBe(w2);
  });
});
