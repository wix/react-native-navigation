import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';
import { mock, instance, verify, anyFunction } from 'ts-mockito';
import { ComponentWrapper } from './ComponentWrapper';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';
import { AppRegistryService } from '../adapters/AppRegistryService';
import { ComponentProvider } from 'react-native';
import * as React from 'react';

const DummyComponent = () => null;

class MyComponent extends React.Component<any, any> {}

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
    componentWrapper.wrap = jest.fn();
    let wrappedComponent: React.ComponentClass<any>;
    store.hasRegisteredWrappedComponent = jest.fn(() => wrappedComponent !== undefined);
    store.setWrappedComponent = jest.fn(() => wrappedComponent = MyComponent);

    const generator: ComponentProvider = jest.fn(() => DummyComponent);
    uut.registerComponent('example.MyComponent.name', generator)();
    uut.registerComponent('example.MyComponent.name', generator)();

    expect(componentWrapper.wrap).toHaveBeenCalledTimes(1);
  });
});
