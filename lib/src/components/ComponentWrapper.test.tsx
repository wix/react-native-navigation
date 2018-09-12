import * as React from 'react';
import { Text } from 'react-native';
import * as renderer from 'react-test-renderer';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';

describe('ComponentWrapper', () => {
  const componentName = 'example.MyComponent';
  let store;
  let myComponentProps;
  const componentEventsObserver = { unmounted: jest.fn() };

  class MyComponent extends React.Component<any, any> {
    static options = {
      title: 'MyComponentTitle'
    };

    render() {
      myComponentProps = this.props;
      if (this.props.renderCount) {
        this.props.renderCount();
      }
      return <Text>{this.props.text || 'Hello, World!'}</Text>;
    }
  }

  class TestParent extends React.Component<any, any> {
    private ChildClass;

    constructor(props) {
      super(props);
      this.ChildClass = props.ChildClass;
      this.state = { propsFromState: {} };
    }

    render() {
      return (
        <this.ChildClass
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
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const orig = console.error;
    console.error = (a) => a;
    expect(() => {
      renderer.create(<NavigationComponent />);
    }).toThrowError('Component example.MyComponent does not have a componentId!');
    console.error = orig;
  });

  it('wraps the component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    expect(NavigationComponent).not.toBeInstanceOf(MyComponent);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(tree.toJSON()!.children).toEqual(['Hello, World!']);
  });

  it('injects props from wrapper into original component', () => {
    const renderCount = jest.fn();
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} text={'yo'} renderCount={renderCount} />);
    expect(tree.toJSON()!.children).toEqual(['yo']);
    expect(renderCount).toHaveBeenCalledTimes(1);
  });

  it('updates props from wrapper into original component on state change', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    expect(myComponentProps.foo).toEqual(undefined);
    (tree.getInstance() as any).setState({ propsFromState: { foo: 'yo' } });
    expect(myComponentProps.foo).toEqual('yo');
  });

  it('pulls props from the store and injects them into the inner component', () => {
    store.setPropsForId('component123', { numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    renderer.create(<NavigationComponent componentId={'component123'} />);
    expect(myComponentProps).toEqual({ componentId: 'component123', numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
  });

  it('updates props from store into inner component', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    store.setPropsForId('component1', { myProp: 'hello' });
    expect(myComponentProps.foo).toEqual(undefined);
    expect(myComponentProps.myProp).toEqual(undefined);
    (tree.getInstance() as any).setState({ propsFromState: { foo: 'yo' } });
    expect(myComponentProps.foo).toEqual('yo');
    expect(myComponentProps.myProp).toEqual('hello');
  });

  it('protects id from change', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<TestParent ChildClass={NavigationComponent} />);
    expect(myComponentProps.componentId).toEqual('component1');
    (tree.getInstance() as any).setState({ propsFromState: { id: 'ERROR' } });
    expect(myComponentProps.componentId).toEqual('component1');
  });

  it('assignes key by id', () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<NavigationComponent componentId={'component1'} />);
    expect(myComponentProps.componentId).toEqual('component1');
    expect((tree.getInstance() as any)._reactInternalInstance.child.key).toEqual('component1');
  });

  it('cleans props from store on unMount', () => {
    store.setPropsForId('component123', { foo: 'bar' });
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<NavigationComponent componentId={'component123'} />);
    expect(store.getPropsForId('component123')).toEqual({ foo: 'bar' });
    tree.unmount();
    expect(store.getPropsForId('component123')).toEqual({});
  });

  it(`merges static members from wrapped component`, () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver) as any;
    expect(NavigationComponent.options).toEqual({ title: 'MyComponentTitle' });
  });

  it(`calls unmounted on componentEventsObserver`, () => {
    const NavigationComponent = ComponentWrapper.wrap(componentName, MyComponent, store, componentEventsObserver);
    const tree = renderer.create(<NavigationComponent componentId={'component123'} />);
    expect(componentEventsObserver.unmounted).not.toHaveBeenCalled();
    tree.unmount();
    expect(componentEventsObserver.unmounted).toHaveBeenCalledTimes(1);
  });

  describe(`register with redux store`, () => {
    class MyReduxComp extends React.Component<any> {
      static get options() {
        return { foo: 123 };
      }
      render() {
        return (
          <Text>{this.props.txt}</Text>
        );
      }
    }
    function mapStateToProps(state) {
      return {
        txt: state.txt
      };
    }
    const ConnectedComp = require('react-redux').connect(mapStateToProps)(MyReduxComp);
    const ReduxProvider = require('react-redux').Provider;
    const initialState = { txt: 'it just works' };
    const reduxStore = require('redux').createStore((state = initialState) => state);

    it(`wraps the component with a react-redux provider with passed store`, () => {
      const NavigationComponent = ComponentWrapper.wrap(componentName, ConnectedComp, store, componentEventsObserver, ReduxProvider, reduxStore);
      const tree = renderer.create(<NavigationComponent componentId={'theCompId'} />);
      expect(tree.toJSON()!.children).toEqual(['it just works']);
      expect((NavigationComponent as any).options).toEqual({ foo: 123 });
    });
  });
});
