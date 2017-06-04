import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';
import renderer from 'react-test-renderer';

import ContainerRegistry from './ContainerRegistry';
import Store from './Store';

describe('ContainerRegistry', () => {
  let uut;
  let store;

  class MyContainer extends Component {
    render() {
      return <Text>{'Hello, World!'}</Text>;
    }
  }

  beforeEach(() => {
    store = new Store();
    AppRegistry.registerComponent = jest.fn(AppRegistry.registerComponent);
    uut = new ContainerRegistry(store);
  });

  it('registers container component by containerName into AppRegistry', () => {
    expect(AppRegistry.registerComponent).not.toHaveBeenCalled();
    uut.registerContainer('example.MyContainer.name', () => MyContainer);
    expect(AppRegistry.registerComponent).toHaveBeenCalledTimes(1);
    expect(AppRegistry.registerComponent.mock.calls[0][0]).toEqual('example.MyContainer.name');
  });

  it('saves the original container into the store', () => {
    expect(store.getOriginalContainerClassForName('example.MyContainer.name')).toBeUndefined();
    uut.registerContainer('example.MyContainer.name', () => MyContainer);
    const Class = store.getOriginalContainerClassForName('example.MyContainer.name');
    expect(Class).not.toBeUndefined();
    expect(Class).toEqual(MyContainer);
    expect(Object.getPrototypeOf(Class)).toEqual(Component);
  });

  it('resulting in a normal component', () => {
    uut.registerContainer('example.MyContainer.name', () => MyContainer);
    const Container = AppRegistry.registerComponent.mock.calls[0][1]();
    const tree = renderer.create(<Container containerId="123" />);
    expect(tree.toJSON().children).toEqual(['Hello, World!']);
  });
});
