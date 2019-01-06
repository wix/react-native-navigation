import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';
import { mock, instance, verify, anyFunction } from 'ts-mockito';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { AppRegistryService } from '../adapters/AppRegistryService';

const DummyComponent = () => null;

describe('ComponentRegistry', () => {
  let mockedStore: Store;
  let mockedComponentEventsObserver: ComponentEventsObserver;
  let mockedComponentWrapper: ComponentWrapper;
  let mockedAppRegistryService: AppRegistryService;
  let uut: ComponentRegistry;

  beforeEach(() => {
    mockedStore = mock(Store);
    mockedComponentEventsObserver = mock(ComponentEventsObserver);
    mockedComponentWrapper = mock(ComponentWrapper);
    mockedAppRegistryService = mock(AppRegistryService);

    uut = new ComponentRegistry();
    uut.store = instance(mockedStore),
    uut.componentEventsObserver = instance(mockedComponentEventsObserver),
    uut.componentWrapper = instance(mockedComponentWrapper),
    uut.appRegistryService = instance(mockedAppRegistryService)
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
    const generator = jest.fn(() => {});
    uut.registerComponent('example.MyComponent.name', generator);
    expect(generator).toHaveBeenCalledTimes(0);
  });
});
