import React from 'react';
import 'react-native';
import renderer from 'react-test-renderer';
import { Provider } from 'react-redux';
import { Navigation } from '../../lib/dist/index';

import MyComponent from './MyComponent';
import { reduxStore as myReduxStore, selectors as mySelectors } from './MyStore';

describe('redux support', () => {
  let MyConnectedComponent;
  let reduxStore;
  let selectors;

  beforeEach(() => {
    MyConnectedComponent = MyComponent;
    reduxStore = myReduxStore;
    selectors = mySelectors;
  });

  it('renders normally', () => {
    const HOC = class extends React.Component {
      render() {
        return (
          <Provider store={reduxStore}>
            <MyConnectedComponent />
          </Provider>
        );
      }
    };
    Navigation.registerComponent(
      'ComponentName',
      () => (props) => <HOC {...props} />,
      Provider,
      reduxStore
    );

    const tree = renderer.create(<HOC />);
    expect(tree.toJSON().children).toEqual(['no name']);
  });

  it('passes props into wrapped components', () => {
    const renderCountIncrement = jest.fn();

    const HOC = class extends React.Component {
      render() {
        return (
          <Provider store={reduxStore}>
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
      <Provider store={reduxStore}>
        <MyConnectedComponent renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    expect(tree.toJSON().children).toEqual(['no name']);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    reduxStore.dispatch({ type: 'redux.MyStore.setName', name: 'Bob' });
    expect(selectors.getName(reduxStore.getState())).toEqual('Bob');
    expect(tree.toJSON().children).toEqual(['Bob']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <Provider store={reduxStore}>
        <MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />
      </Provider>
    );

    expect(tree.toJSON().children).toEqual(null);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    reduxStore.dispatch({ type: 'redux.MyStore.setAge', age: 30 });
    expect(selectors.getAge(reduxStore.getState())).toEqual(30);
    expect(tree.toJSON().children).toEqual(['30']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });
});
