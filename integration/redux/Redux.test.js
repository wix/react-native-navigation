const React = require('react');
require('react-native');
const renderer = require('react-test-renderer');
const { Provider } = require('react-redux');
const { Navigation } = require('../../lib/src/index');

describe('redux support', () => {
  let MyConnectedComponent;
  let store;

  beforeEach(() => {
    MyConnectedComponent = require('./MyComponent');
    store = require('./MyStore');
  });

  it('renders normally', () => {
    const HOC = class extends React.Component {
      render() {
        return (
          <Provider store={store.reduxStore}>
            <MyConnectedComponent />
          </Provider>
        );
      }
    };
    Navigation.registerComponent(
      'ComponentName',
      () => (props) => <HOC {...props} />,
      Provider,
      store.reduxStore
    );

    const tree = renderer.create(<HOC />);
    expect(tree.toJSON().children).toEqual(['no name']);
  });

  it('passes props into wrapped components', () => {
    const renderCountIncrement = jest.fn();

    const HOC = class extends React.Component {
      render() {
        return (
          <Provider store={store.reduxStore}>
            <MyConnectedComponent {...this.props} />
          </Provider>
        );
      }
    };
    const CompFromNavigation = Navigation.registerComponent('ComponentName', () => (props) => (
      <HOC {...props} />
    ))();

    const tree = renderer.create(
      <CompFromNavigation componentId="componentId" renderCountIncrement={renderCountIncrement} />
    );
    expect(tree.toJSON().children).toEqual(['no name']);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <Provider store={store.reduxStore}>
        <MyConnectedComponent renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    expect(tree.toJSON().children).toEqual(['no name']);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.reduxStore.dispatch({ type: 'redux.MyStore.setName', name: 'Bob' });
    expect(store.selectors.getName(store.reduxStore.getState())).toEqual('Bob');
    expect(tree.toJSON().children).toEqual(['Bob']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <Provider store={store.reduxStore}>
        <MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    expect(tree.toJSON().children).toEqual(null);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.reduxStore.dispatch({ type: 'redux.MyStore.setAge', age: 30 });
    expect(store.selectors.getAge(store.reduxStore.getState())).toEqual(30);
    expect(tree.toJSON().children).toEqual(['30']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });
});
