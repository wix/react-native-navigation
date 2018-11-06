import * as React from 'react';
import { AppRegistry, Text } from 'react-native';
import * as renderer from 'react-test-renderer';
import { ComponentRegistry } from './ComponentRegistry';
import { Store } from './Store';

describe('ComponentRegistry', () => {
  let uut;
  let store;
  let mockRegistry: any;
  let mockWrapper: any;


  class WrappedComponent extends React.Component {
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
    mockWrapper = jest.mock('./ComponentWrapper');
    mockWrapper.wrap = () => WrappedComponent;
    uut = new ComponentRegistry(store, {} as any, mockWrapper);
  });

  it('registers component by componentName into AppRegistry', () => {
    expect(mockRegistry).not.toHaveBeenCalled();
    const result = uut.registerComponent('example.MyComponent.name', () => {});
    expect(mockRegistry).toHaveBeenCalledTimes(1);
    expect(mockRegistry.mock.calls[0][0]).toEqual('example.MyComponent.name');
    expect(mockRegistry.mock.calls[0][1]()).toEqual(result());
  });

  it('saves the wrapper component generator the store', () => {
    expect(store.getComponentClassForName('example.MyComponent.name')).toBeUndefined();
    uut.registerComponent('example.MyComponent.name', () => {});
    const Class = store.getComponentClassForName('example.MyComponent.name');
    expect(Class).not.toBeUndefined();
    expect(Class()).toEqual(WrappedComponent);
    expect(Object.getPrototypeOf(Class())).toEqual(React.Component);
  });

  it('resulting in a normal component', () => {
    uut.registerComponent('example.MyComponent.name', () => {});
    const Component = mockRegistry.mock.calls[0][1]();
    const tree = renderer.create(<Component componentId='123' />);
    expect(tree.toJSON()!.children).toEqual(['Hello, World!']);
  });

  it('should not invoke generator', () => {
    const generator = jest.fn(() => {});
    uut.registerComponent('example.MyComponent.name', generator);
    expect(generator).toHaveBeenCalledTimes(0);
  });

  it('saves wrapped component to store', () => {
    jest.spyOn(store, 'setComponentClassForName');
    const generator = jest.fn(() => {});
    const componentName = 'example.MyComponent.name';
    uut.registerComponent(componentName, generator);
    expect(store.getComponentClassForName(componentName)()).toEqual(WrappedComponent);
  });
});
