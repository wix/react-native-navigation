/* eslint-disable consistent-this, react/no-multi-comp */
import React, { Component } from 'react';
import { AppRegistry, Text } from 'react-native';
import renderer from 'react-test-renderer';

import ContainerWrapper from './ContainerWrapper';
import Store from './Store';

describe('ContainerWrapper', () => {
  let myContainerRef;
  let testParentRef;
  let store;
  const containerName = 'example.MyContainer';

  class MyContainer extends Component {
    constructor(props) {
      super(props);
      myContainerRef = this;
    }

    render() {
      return <Text>{'Hello, World!'}</Text>;
    }
  }

  class TestParent extends Component {
    constructor(props) {
      super(props);
      testParentRef = this;
      this.ChildClass = props.ChildClass;
      this.state = { propsFromState: {} };
    }

    render() {
      const Child = this.ChildClass;
      return (
        <Child containerId="container1" {...this.state.propsFromState} />
      );
    }
  }

  beforeEach(() => {
    store = new Store();
  });

  it('must have containerId as prop', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const orig = console.error; //eslint-disable-line
    console.error = (a) => a; //eslint-disable-line
    expect(() => {
      renderer.create(<NavigationContainer />);
    }).toThrow(new Error('Container example.MyContainer does not have a containerId!'));
    console.error = orig; //eslint-disable-line
  });

  it('wraps the container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    expect(NavigationContainer).not.toBeInstanceOf(MyContainer);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(tree.toJSON().children).toEqual(['Hello, World!']);
    expect(myContainerRef).toBeInstanceOf(MyContainer);
  });

  it('injects props from wrapper into original container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<NavigationContainer containerId={'container1'} myProp={'yo'} />);
    expect(myContainerRef.props.myProp).toEqual('yo');
  });

  it('updates props from wrapper into original container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<TestParent ChildClass={NavigationContainer} />);
    expect(myContainerRef.props.foo).toEqual(undefined);
    testParentRef.setState({ propsFromState: { foo: 'yo' } });
    expect(myContainerRef.props.foo).toEqual('yo');
  });

  it('pulls props from the store and injects them into the inner container', () => {
    store.setPropsForContainerId('container123', { numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<NavigationContainer containerId={'container123'} />);
    expect(myContainerRef.props).toEqual({ containerId: 'container123', numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
  });

  it('updates props from store into inner container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<TestParent ChildClass={NavigationContainer} />);
    store.setPropsForContainerId('container1', { myProp: 'hello' });
    expect(myContainerRef.props.foo).toEqual(undefined);
    expect(myContainerRef.props.myProp).toEqual(undefined);
    testParentRef.setState({ propsFromState: { foo: 'yo' } });
    expect(myContainerRef.props.foo).toEqual('yo');
    expect(myContainerRef.props.myProp).toEqual('hello');
  });

  it('protects id from change', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<TestParent ChildClass={NavigationContainer} />);
    expect(myContainerRef.props.containerId).toEqual('container1');
    testParentRef.setState({ propsFromState: { id: 'ERROR' } });
    expect(myContainerRef.props.containerId).toEqual('container1');
  });

  it('assignes key by id', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(myContainerRef.props.containerId).toEqual('container1');
    expect(myContainerRef._reactInternalInstance.key).toEqual('container1');
  });

  it('saves self ref into store', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(store.getRefForContainerId('container1')).toBeDefined();
    expect(store.getRefForContainerId('container1')).toBe(tree.getInstance());
  });

  it('cleans ref from store on unMount', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(store.getRefForContainerId('container1')).toBeDefined();
    tree.unmount();
    expect(store.getRefForContainerId('container1')).toBeUndefined();
  });

  it('holds ref to OriginalContainer', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(tree.getInstance().originalContainerRef).toBe(myContainerRef);
  });

  it('cleans ref to internal container on unount', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    const instance = tree.getInstance();
    expect(instance.originalContainerRef).toBeInstanceOf(Component);
    tree.unmount();
    expect(instance.originalContainerRef).toBeFalsy();
  });

  describe('container lifecycle', () => {
    const onStartCallback = jest.fn();
    const onStopCallback = jest.fn();

    class MyLifecycleContainer extends MyContainer {
      onStart() {
        onStartCallback();
      }

      onStop() {
        onStopCallback();
      }
    }

    it('onStart and onStop are optional', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(() => tree.getInstance().onStart()).not.toThrow();
      expect(() => tree.getInstance().onStop()).not.toThrow();
    });

    it('calls onStart on OriginalContainer', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyLifecycleContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(onStartCallback).toHaveBeenCalledTimes(0);
      tree.getInstance().onStart();
      expect(onStartCallback).toHaveBeenCalledTimes(1);
    });

    it('calls onSop on OriginalContainer', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyLifecycleContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(onStopCallback).toHaveBeenCalledTimes(0);
      tree.getInstance().onStop();
      expect(onStopCallback).toHaveBeenCalledTimes(1);
    });
  });
});
