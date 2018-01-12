const React = require('react');
const { Component } = require('react');
const { AppRegistry, Text } = require('react-native');

const renderer = require('react-test-renderer');
const ComponentRegistry = require('./ComponentRegistry');
const Store = require('./Store');

describe('ComponentRegistry', () => {
  let uut;
  let store;

  class MyComponent extends Component {
    render() {
      return <Text>{'Hello, World!'}</Text>;
    }
  }

  beforeEach(() => {
    store = new Store();
    AppRegistry.registerComponent = jest.fn(AppRegistry.registerComponent);
    uut = new ComponentRegistry(store);
  });

  it('registers component component by componentName into AppRegistry', () => {
    expect(AppRegistry.registerComponent).not.toHaveBeenCalled();
    uut.registerComponent('example.MyComponent.name', () => MyComponent);
    expect(AppRegistry.registerComponent).toHaveBeenCalledTimes(1);
    expect(AppRegistry.registerComponent.mock.calls[0][0]).toEqual('example.MyComponent.name');
  });

  it('saves the original component into the store', () => {
    expect(store.getOriginalComponentClassForName('example.MyComponent.name')).toBeUndefined();
    uut.registerComponent('example.MyComponent.name', () => MyComponent);
    const Class = store.getOriginalComponentClassForName('example.MyComponent.name');
    expect(Class).not.toBeUndefined();
    expect(Class).toEqual(MyComponent);
    expect(Object.getPrototypeOf(Class)).toEqual(Component);
  });

  it('resulting in a normal component', () => {
    uut.registerComponent('example.MyComponent.name', () => MyComponent);
    const Component = AppRegistry.registerComponent.mock.calls[0][1]();
    const tree = renderer.create(<Component componentId="123" />);
    expect(tree.toJSON().children).toEqual(['Hello, World!']);
  });
});
