import React from 'react';
import 'react-native';
import renderer from 'react-test-renderer';
import { Navigation } from '../../lib/dist/index';

import MyComponent from './MyComponent';
import { getters, setters } from './MyStore';

describe('remx support', () => {
  let MyConnectedComponent;
  let store;

  beforeEach(() => {
    MyConnectedComponent = MyComponent;
    store = {
      getters,
      setters,
    };
  });

  it('renders normally', () => {
    const tree = renderer.create(<MyConnectedComponent />);
    expect(tree.toJSON().children).toEqual(['no name']);
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <MyConnectedComponent renderCountIncrement={renderCountIncrement} />
    );

    expect(tree.toJSON().children).toEqual(['no name']);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setName('Bob');
    expect(store.getters.getName()).toEqual('Bob');
    expect(tree.toJSON().children).toEqual(['Bob']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(
      <MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />
    );

    expect(tree.toJSON().children).toEqual(null);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setAge(30);
    expect(store.getters.getAge()).toEqual(30);
    expect(tree.toJSON().children).toEqual(['30']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('support for static members in connected components', () => {
    expect(MyConnectedComponent.options).toEqual({ title: 'MyComponent' });

    const registeredComponentClass = Navigation.registerComponent(
      'MyComponentName',
      () => MyConnectedComponent
    )();
    expect(registeredComponentClass.options).toEqual({ title: 'MyComponent' });
  });
});
