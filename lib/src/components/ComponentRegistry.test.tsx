import { AppRegistry } from 'react-native';

import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';
import { mock, instance, verify, anyFunction } from 'ts-mockito';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

describe('ComponentRegistry', () => {
  let uut: ComponentRegistry;
  let mockRegistry: any;
  const DummyComponent = () => null;

  let mockedStore = mock(Store);
  let store = instance(mockedStore);

  beforeEach(() => {
    mockRegistry = AppRegistry.registerComponent = jest.fn(AppRegistry.registerComponent);

    mockedStore = mock(Store);
    store = instance(mockedStore);

    const mockedComponentEventsObserver = mock(ComponentEventsObserver);
    const componentEventsObserver = instance(mockedComponentEventsObserver);

    const mockedComponentWrapper = mock(ComponentWrapper);
    const componentWrapper = instance(mockedComponentWrapper);

    uut = new ComponentRegistry(store, componentEventsObserver, componentWrapper);
  });

  it('registers component by componentName into AppRegistry', () => {
    expect(mockRegistry).not.toHaveBeenCalled();
    const result = uut.registerComponent('example.MyComponent.name', () => DummyComponent);
    expect(mockRegistry).toHaveBeenCalledTimes(1);
    expect(mockRegistry.mock.calls[0][0]).toEqual('example.MyComponent.name');
    expect(mockRegistry.mock.calls[0][1]()).toEqual(result());
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
