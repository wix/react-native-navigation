const React = require('react');
const { Component } = require('react');

const { Text } = require('react-native');
const renderer = require('react-test-renderer');
const ContainerWrapper = require('./ContainerWrapper');
const Store = require('./Store');

describe('ContainerWrapper', () => {
  let store;
  const containerName = 'example.MyContainer';
  let childRef;

  class MyContainer extends Component {
    render() {
      return <Text>{'Hello, World!'}</Text>;
    }
  }

  class TestParent extends Component {
    constructor(props) {
      super(props);
      this.ChildClass = props.ChildClass;
      this.state = { propsFromState: {} };
    }

    render() {
      const Child = this.ChildClass;
      return (
        <Child
          ref={(r) => childRef = r}
          containerId="container1"
          {...this.state.propsFromState}
        />
      );
    }
  }

  beforeEach(() => {
    store = new Store();
  });

  it('must have containerId as prop', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const orig = console.error;
    console.error = (a) => a;
    expect(() => {
      renderer.create(<NavigationContainer />);
    }).toThrow(new Error('Container example.MyContainer does not have a containerId!'));
    console.error = orig;
  });

  it('wraps the container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    expect(NavigationContainer).not.toBeInstanceOf(MyContainer);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(tree.toJSON().children).toEqual(['Hello, World!']);
    expect(tree.getInstance().originalContainerRef).toBeInstanceOf(MyContainer);
  });

  it('injects props from wrapper into original container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} myProp={'yo'} />);
    expect(tree.getInstance().originalContainerRef.props.myProp).toEqual('yo');
  });

  it('updates props from wrapper into original container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationContainer} />);
    expect(childRef.props.foo).toEqual(undefined);
    tree.getInstance().setState({ propsFromState: { foo: 'yo' } });
    expect(childRef.props.foo).toEqual('yo');
  });

  it('pulls props from the store and injects them into the inner container', () => {
    store.setPropsForContainerId('container123', { numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container123'} />);
    const originalContainerProps = tree.getInstance().originalContainerRef.props;
    expect(originalContainerProps).toEqual({ containerId: 'container123', numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
  });

  it('updates props from store into inner container', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationContainer} />);
    store.setPropsForContainerId('container1', { myProp: 'hello' });
    expect(childRef.originalContainerRef.props.foo).toEqual(undefined);
    expect(childRef.originalContainerRef.props.myProp).toEqual(undefined);
    tree.getInstance().setState({ propsFromState: { foo: 'yo' } });
    expect(childRef.originalContainerRef.props.foo).toEqual('yo');
    expect(childRef.originalContainerRef.props.myProp).toEqual('hello');
  });

  it('protects id from change', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationContainer} />);
    expect(childRef.originalContainerRef.props.containerId).toEqual('container1');
    tree.getInstance().setState({ propsFromState: { id: 'ERROR' } });
    expect(childRef.originalContainerRef.props.containerId).toEqual('container1');
  });

  it('assignes key by id', () => {
    const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
    const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
    expect(tree.getInstance().originalContainerRef.props.containerId).toEqual('container1');
    expect(tree.getInstance().originalContainerRef._reactInternalInstance.key).toEqual('container1');
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
    expect(tree.getInstance().originalContainerRef).toBeDefined();
    expect(tree.getInstance().originalContainerRef).toBeInstanceOf(MyContainer);
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
    const didAppearCallback = jest.fn();
    const didDisappearCallback = jest.fn();
    const onNavigationButtonPressedCallback = jest.fn();

    class MyLifecycleContainer extends MyContainer {
      didAppear() {
        didAppearCallback();
      }

      didDisappear() {
        didDisappearCallback();
      }

      onNavigationButtonPressed() {
        onNavigationButtonPressedCallback();
      }
    }

    it('didAppear, didDisappear and onNavigationButtonPressed are optional', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(() => tree.getInstance().didAppear()).not.toThrow();
      expect(() => tree.getInstance().didDisappear()).not.toThrow();
      expect(() => tree.getInstance().onNavigationButtonPressed()).not.toThrow();
    });

    it('calls didAppear on OriginalContainer', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyLifecycleContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(didAppearCallback).toHaveBeenCalledTimes(0);
      tree.getInstance().didAppear();
      expect(didAppearCallback).toHaveBeenCalledTimes(1);
    });

    it('calls didDisappear on OriginalContainer', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyLifecycleContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(didDisappearCallback).toHaveBeenCalledTimes(0);
      tree.getInstance().didDisappear();
      expect(didDisappearCallback).toHaveBeenCalledTimes(1);
    });

    it('calls onNavigationButtonPressed on OriginalContainer', () => {
      const NavigationContainer = ContainerWrapper.wrap(containerName, MyLifecycleContainer, store);
      const tree = renderer.create(<NavigationContainer containerId={'container1'} />);
      expect(onNavigationButtonPressedCallback).toHaveBeenCalledTimes(0);
      tree.getInstance().onNavigationButtonPressed();
      expect(onNavigationButtonPressedCallback).toHaveBeenCalledTimes(1);
    });
  });
});
