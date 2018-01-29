import * as React from 'react';
import { Text } from 'react-native';
import * as renderer from 'react-test-renderer';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';

declare module 'react-test-renderer' {
  interface ReactTestInstance {
    [P: string]: any;
  }
}

describe('ComponentWrapper', () => {
  let store;
  const componentName = 'example.MyComponent';
  let childRef;

  class MyComponent extends React.Component {
    render() {
      return <Text>{'Hello, World!'}</Text>;
    }
  }

  class TestParent extends React.Component<any, { propsFromState: {} }> {
    private ChildClass;

    constructor(props) {
      super(props);
      this.ChildClass = props.ChildClass;
      this.state = { propsFromState: {} };
    }

    render() {
      return (
        <this.ChildClass
          ref={(r) => childRef = r}
          componentId='component1'
          {...this.state.propsFromState}
        />
      );
    }
  }

  beforeEach(() => {
    store = new Store();
  });

  it('must have componentId as prop', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const orig = console.error;
    console.error = (a) => a;
    expect(() => {
      renderer.create(<NavigationComponent />);
    }).toThrowError('Component example.MyComponent does not have a componentId!');
    console.error = orig;
  });

  it('wraps the component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    expect(NavigationComponent).not.toBeInstanceOf(MyComponent);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(tree.toJSON()!.children).toEqual(['Hello, World!']);
    expect(tree.getInstance()!.originalComponentRef).toBeInstanceOf(MyComponent);
  });

  it('injects props from wrapper into original component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} myProp={'yo'} />);
    expect(tree.getInstance()!.originalComponentRef.props.myProp).toEqual('yo');
  });

  it('updates props from wrapper into original component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    expect(childRef.props.foo).toEqual(undefined);
    tree.getInstance()!.setState({ propsFromState: { foo: 'yo' } });
    expect(childRef.props.foo).toEqual('yo');
  });

  it('pulls props from the store and injects them into the inner component', () => {
    store.setPropsForComponentId('component123', { numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component123'} />);
    const originalComponentProps = tree.getInstance()!.originalComponentRef.props;
    expect(originalComponentProps).toEqual({ componentId: 'component123', numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
  });

  it('updates props from store into inner component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    store.setPropsForComponentId('component1', { myProp: 'hello' });
    expect(childRef.originalComponentRef.props.foo).toEqual(undefined);
    expect(childRef.originalComponentRef.props.myProp).toEqual(undefined);
    tree.getInstance()!.setState({ propsFromState: { foo: 'yo' } });
    expect(childRef.originalComponentRef.props.foo).toEqual('yo');
    expect(childRef.originalComponentRef.props.myProp).toEqual('hello');
  });

  it('protects id from change', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    expect(childRef.originalComponentRef.props.componentId).toEqual('component1');
    tree.getInstance()!.setState({ propsFromState: { id: 'ERROR' } });
    expect(childRef.originalComponentRef.props.componentId).toEqual('component1');
  });

  it('assignes key by id', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(tree.getInstance()!.originalComponentRef.props.componentId).toEqual('component1');
    expect(tree.getInstance()!.originalComponentRef._reactInternalInstance.key).toEqual('component1');
  });

  it('saves self ref into store', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(store.getRefForComponentId('component1')).toBeDefined();
    expect(store.getRefForComponentId('component1')).toBe(tree.getInstance());
  });

  it('cleans ref from store on unMount', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(store.getRefForComponentId('component1')).toBeDefined();
    tree.unmount();
    expect(store.getRefForComponentId('component1')).toBeUndefined();
  });

  it('holds ref to OriginalComponent', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(tree.getInstance()!.originalComponentRef).toBeDefined();
    expect(tree.getInstance()!.originalComponentRef).toBeInstanceOf(MyComponent);
  });

  it('cleans ref to internal component on unount', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    const instance = tree.getInstance()!;
    expect(instance.originalComponentRef).toBeInstanceOf(React.Component);
    tree.unmount();
    expect(instance.originalComponentRef).toBeFalsy();
  });

  describe('component lifecycle', () => {
    const didAppearCallback = jest.fn();
    const didDisappearCallback = jest.fn();
    const onNavigationButtonPressedCallback = jest.fn();

    class MyLifecycleComponent extends MyComponent {
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
      const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store);
      const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
      expect(() => tree.getInstance()!.didAppear()).not.toThrow();
      expect(() => tree.getInstance()!.didDisappear()).not.toThrow();
      expect(() => tree.getInstance()!.onNavigationButtonPressed()).not.toThrow();
    });

    it('calls didAppear on OriginalComponent', () => {
      const NavigationComponent = ComponentWrapper.wrap(componentName, MyLifecycleComponent, store);
      const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
      expect(didAppearCallback).toHaveBeenCalledTimes(0);
      tree.getInstance()!.didAppear();
      expect(didAppearCallback).toHaveBeenCalledTimes(1);
    });

    it('calls didDisappear on OriginalComponent', () => {
      const NavigationComponent = ComponentWrapper.wrap(componentName, MyLifecycleComponent, store);
      const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
      expect(didDisappearCallback).toHaveBeenCalledTimes(0);
      tree.getInstance()!.didDisappear();
      expect(didDisappearCallback).toHaveBeenCalledTimes(1);
    });

    it('calls onNavigationButtonPressed on OriginalComponent', () => {
      const NavigationComponent = ComponentWrapper.wrap(componentName, MyLifecycleComponent, store);
      const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
      expect(onNavigationButtonPressedCallback).toHaveBeenCalledTimes(0);
      tree.getInstance()!.onNavigationButtonPressed();
      expect(onNavigationButtonPressedCallback).toHaveBeenCalledTimes(1);
    });
  });
});
