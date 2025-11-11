const React = require('react');
require('react-native');
const { render, act } = require('@testing-library/react-native');
const { Provider } = require('react-redux');
const { Navigation } = require('../../src/index');

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

    const { getByText } = render(<HOC />);
    expect(getByText('no name')).toBeTruthy();
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

    const { getByText } = render(
      <CompFromNavigation componentId="componentId" renderCountIncrement={renderCountIncrement} />
    );
    expect(getByText('no name')).toBeTruthy();
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const { getByText, rerender } = render(
      <Provider store={store.reduxStore}>
        <MyConnectedComponent renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    expect(getByText('no name')).toBeTruthy();
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    act(() => {
      store.reduxStore.dispatch({ type: 'redux.MyStore.setName', name: 'Bob' });
    });
    expect(store.selectors.getName(store.reduxStore.getState())).toEqual('Bob');
    expect(getByText('Bob')).toBeTruthy();
    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key', () => {
    const renderCountIncrement = jest.fn();
    const { queryByText, rerender } = render(
      <Provider store={store.reduxStore}>
        <MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    // Initially should show nothing (null children means no text)
    expect(queryByText('30')).toBeNull();
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    act(() => {
      store.reduxStore.dispatch({ type: 'redux.MyStore.setAge', age: 30 });
    });
    expect(store.selectors.getAge(store.reduxStore.getState())).toEqual(30);
    expect(queryByText('30')).toBeTruthy();
    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });
});
