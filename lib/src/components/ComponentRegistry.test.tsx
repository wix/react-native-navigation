import * as React from 'react';
import { AppRegistry, Text } from 'react-native';
import * as renderer from 'react-test-renderer';
import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';

describe('ComponentRegistry', () => {
  let uut;
  let store;
  let mockRegistry: any;

  class MyComponent extends React.Component {
    render() {
      return (
        <Text>
          {
            'Hello, World!'
          }
        </Text>);
    }
  }

  beforeEach(() => {
    store = new Store();
    mockRegistry = AppRegistry.registerComponent = jest.fn(AppRegistry.registerComponent);
    uut = new ComponentRegistry(store, {} as any);
  });

  it('registers component component by componentName into AppRegistry', () => {
    expect(mockRegistry).not.toHaveBeenCalled();
    const result = uut.registerComponent('example.MyComponent.name', () => MyComponent);
    expect(mockRegistry).toHaveBeenCalledTimes(1);
    expect(mockRegistry.mock.calls[0][0]).toEqual('example.MyComponent.name');
    expect(mockRegistry.mock.calls[0][1]()).toEqual(result);
  });

  it('saves the original component into the store', () => {
    expect(store.getOriginalComponentClassForName('example.MyComponent.name')).toBeUndefined();
    uut.registerComponent('example.MyComponent.name', () => MyComponent);
    const Class = store.getOriginalComponentClassForName('example.MyComponent.name');
    expect(Class).not.toBeUndefined();
    expect(Class).toEqual(MyComponent);
    expect(Object.getPrototypeOf(Class)).toEqual(React.Component);
  });

  it('resulting in a normal component', () => {
    uut.registerComponent('example.MyComponent.name', () => MyComponent);
    const Component = mockRegistry.mock.calls[0][1]();
    const tree = renderer.create(<Component componentId='123' />);
    expect(tree.toJSON()!.children).toEqual(['Hello, World!']);
  });
});
