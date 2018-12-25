import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';
import { mock, instance, verify, anyFunction } from 'ts-mockito';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { AppRegistryService } from '../adapters/AppRegistryService';

const DummyComponent = () => null;

describe('ComponentRegistry', () => {
  let uut: ComponentRegistry;

  let mockedStore: Store;
  let store: Store;

  let mockedAppRegistryService: AppRegistryService;
  let appRegistryService: AppRegistryService;

  beforeEach(() => {
    mockedStore = mock(Store);
    store = instance(mockedStore);

    const mockedComponentEventsObserver = mock(ComponentEventsObserver);
    const componentEventsObserver = instance(mockedComponentEventsObserver);

    const mockedComponentWrapper = mock(ComponentWrapper);
    const componentWrapper = instance(mockedComponentWrapper);

    mockedAppRegistryService = mock(AppRegistryService);
    appRegistryService = instance(mockedAppRegistryService);

    uut = new ComponentRegistry(
      store,
      componentEventsObserver,
      componentWrapper,
      appRegistryService
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
    const generator = jest.fn(() => {});
    uut.registerComponent('example.MyComponent.name', generator);
    expect(generator).toHaveBeenCalledTimes(0);
  });
});
