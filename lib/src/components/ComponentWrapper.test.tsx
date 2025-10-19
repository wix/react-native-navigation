import * as React from 'react';
import { View, Text } from 'react-native';
import { render, act } from '@testing-library/react-native';
import { ComponentWrapper } from './ComponentWrapper';
import { Store } from './Store';
import { mock, verify, instance } from 'ts-mockito';
import { ComponentEventsObserver } from '../events/ComponentEventsObserver';

describe('ComponentWrapper', () => {
  const componentName = 'example.MyComponent';
  let store: Store;
  let myComponentProps: any;
  let mockedComponentEventsObserver: ComponentEventsObserver;
  let componentEventsObserver: ComponentEventsObserver;
  let uut: ComponentWrapper;

  class MyComponent extends React.Component<any, any> {
    static options = {
      title: 'MyComponentTitle',
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
    private ChildClass: any;

    constructor(props: any) {
      super(props);
      this.ChildClass = props.ChildClass;
      this.state = { propsFromState: {} };
    }

    setStateFromTest = (newState: any) => {
      this.setState({ propsFromState: newState });
    }

    render() {
      const { ChildClass } = this;

      return <ChildClass componentId="component1" {...this.state.propsFromState} />;
    }
  }

  beforeEach(() => {
    store = new Store();
    mockedComponentEventsObserver = mock(ComponentEventsObserver);
    componentEventsObserver = instance(mockedComponentEventsObserver);
    uut = new ComponentWrapper();
  });

  it('must have componentId as prop', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    const orig = console.error;
    console.error = (a: any) => a;
    expect(() => {
      render(<NavigationComponent />);
    }).toThrowError('Component example.MyComponent does not have a componentId!');
    console.error = orig;
  });

  it('wraps the component', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    expect(NavigationComponent).not.toBeInstanceOf(MyComponent);
    const { getByText } = render(<NavigationComponent componentId={'component1'} />);
    expect(getByText('Hello, World!')).toBeTruthy();
  });

  it('injects props from wrapper into original component', () => {
    const renderCount = jest.fn();
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    const { getByText } = render(
      <NavigationComponent componentId={'component1'} text={'yo'} renderCount={renderCount} />
    );
    expect(getByText('yo')).toBeTruthy();
    expect(renderCount).toHaveBeenCalledTimes(1);
  });

  it('updates props from wrapper into original component on state change', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );

    let parentRef: any = null;
    render(
      <TestParent
        ref={(ref) => { parentRef = ref; }}
        ChildClass={NavigationComponent}
      />
    );

    expect(myComponentProps.foo).toEqual(undefined);

    // Use the ref to call setState wrapped in act
    act(() => {
      if (parentRef) {
        parentRef.setStateFromTest({ foo: 'yo' });
      }
    });

    expect(myComponentProps.foo).toEqual('yo');
  });

  it('pulls props from the store and injects them into the inner component', () => {
    store.updateProps('component123', { numberProp: 1, stringProp: 'hello', objectProp: { a: 2 } });
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    render(<NavigationComponent componentId={'component123'} />);
    expect(myComponentProps).toEqual({
      componentId: 'component123',
      componentName,
      numberProp: 1,
      stringProp: 'hello',
      objectProp: { a: 2 },
    });
  });

  it('update props with callback', (done) => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    render(<NavigationComponent componentId={'component123'} />);

    function callback() {
      try {
        expect(true).toBe(true);
        done();
      } catch (error) {
        done(error);
      }
    }

    store.updateProps('component123', { someProp: 'someValue' }, callback);
  });

  it('updates props from store into inner component', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    render(<TestParent ChildClass={NavigationComponent} />);
    expect(myComponentProps.myProp).toEqual(undefined);

    act(() => {
      store.updateProps('component1', { myProp: 'hello' });
    });

    expect(myComponentProps.myProp).toEqual('hello');
  });

  it('updates props from state into inner component', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );

    let parentRef: any = null;
    render(
      <TestParent
        ref={(ref) => { parentRef = ref; }}
        ChildClass={NavigationComponent}
      />
    );

    expect(myComponentProps.foo).toEqual(undefined);

    act(() => {
      if (parentRef) {
        parentRef.setStateFromTest({ foo: 'yo' });
      }
    });

    expect(myComponentProps.foo).toEqual('yo');
  });

  it('protects id from change', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );

    let parentRef: any = null;
    render(
      <TestParent
        ref={(ref) => { parentRef = ref; }}
        ChildClass={NavigationComponent}
      />
    );

    expect(myComponentProps.componentId).toEqual('component1');

    if (parentRef) {
      parentRef.setStateFromTest({ id: 'ERROR' });
    }

    expect(myComponentProps.componentId).toEqual('component1');
  });

  it('cleans props from store on unMount', () => {
    store.updateProps('component123', { foo: 'bar' });
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    const { unmount } = render(<NavigationComponent componentId={'component123'} />);
    expect(store.getPropsForId('component123')).toEqual({ foo: 'bar' });
    unmount();
    expect(store.getPropsForId('component123')).toEqual({});
  });

  it('merges static members from wrapped component when generated', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    ) as any;
    expect(NavigationComponent.options).toEqual({ title: 'MyComponentTitle' });
  });

  it('calls unmounted on componentEventsObserver', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    const { unmount } = render(<NavigationComponent componentId={'component123'} />);
    verify(mockedComponentEventsObserver.unmounted('component123')).never();
    unmount();
    verify(mockedComponentEventsObserver.unmounted('component123')).once();
  });

  it('renders HOC components correctly', () => {
    const generator = () => (props: any) =>
    (
      <View>
        <MyComponent {...props} />
      </View>
    );
    uut = new ComponentWrapper();
    const NavigationComponent = uut.wrap(componentName, generator, store, componentEventsObserver);
    const { getByText } = render(<NavigationComponent componentId={'component123'} />);
    expect(getByText('Hello, World!')).toBeTruthy();
  });

  it('sets component instance in store when constructed', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    render(<NavigationComponent componentId={'component1'} />);
    expect(store.getComponentInstance('component1')).toBeTruthy();
  });

  it('Component generator is invoked only once', () => {
    const componentGenerator = jest.fn(() => MyComponent);
    uut.wrap(componentName, componentGenerator, store, componentEventsObserver);

    expect(componentGenerator.mock.calls.length).toBe(1);
  });

  describe(`register with redux store`, () => {
    class MyReduxComp extends React.Component<any> {
      static options() {
        return { foo: 123 };
      }
      render() {
        return <Text>{this.props.txt}</Text>;
      }
    }
    interface RootState {
      txt: string;
    }
    function mapStateToProps(state: RootState) {
      return {
        txt: state.txt,
      };
    }
    const ConnectedComp = require('react-redux').connect(mapStateToProps)(MyReduxComp);
    const ReduxProvider = require('react-redux').Provider;
    const initialState: RootState = { txt: 'it just works' };
    const reduxStore = {
      getState: () => initialState,
      dispatch: jest.fn(),
      subscribe: jest.fn(),
      replaceReducer: jest.fn(),
    };

    it(`wraps the component with a react-redux provider with passed store`, () => {
      const NavigationComponent = uut.wrap(
        componentName,
        () => ConnectedComp,
        store,
        componentEventsObserver,
        undefined,
        ReduxProvider,
        reduxStore
      );
      const { getByText } = render(
        <ReduxProvider store={reduxStore}>
          <NavigationComponent componentId={'theCompId'} />
        </ReduxProvider>
      );
      expect(getByText('it just works')).toBeTruthy();
      expect((NavigationComponent as any).options()).toEqual({ foo: 123 });
    });
  });

  it('initialize isMounted with false value', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );
    new NavigationComponent({ componentId: 'component123' });
    expect(store.getComponentInstance('component123')?.isMounted).toEqual(false);
  });

  it('updates isMounted on componentDidMount', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );

    render(<NavigationComponent componentId={'component123'} />);
    expect(store.getComponentInstance('component123')?.isMounted).toEqual(true);
  });

  it('clears isMounted on componentDidUnmount', () => {
    const NavigationComponent = uut.wrap(
      componentName,
      () => MyComponent,
      store,
      componentEventsObserver
    );

    const { unmount } = render(<NavigationComponent componentId={'component123'} />);
    expect(store.getComponentInstance('component123')?.isMounted).toEqual(true);
    unmount();
    expect(store.getComponentInstance('component123')?.isMounted).toBeUndefined();
  });
});
