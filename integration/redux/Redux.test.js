const React = require('react');
require('react-native');
const renderer = require('react-test-renderer');
const { Provider } = require('react-redux');

describe('redux support', () => {
  let MyConnectedContainer;
  let store;

  beforeEach(() => {
    MyConnectedContainer = require('./MyContainer');
    store = require('./MyStore');
  });

  it('renders normally', () => {
    const tree = renderer.create(
      <Provider store={store.reduxStore}>
        <MyConnectedContainer />
      </Provider>
    );
    expect(tree.toJSON().children).toEqual(['no name']);
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <Provider store={store.reduxStore}>
        <MyConnectedContainer renderCountIncrement={renderCountIncrement} />
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
        <MyConnectedContainer printAge={true} renderCountIncrement={renderCountIncrement} />
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
