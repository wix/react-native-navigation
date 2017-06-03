import React from 'react';
require('react-native');
import renderer from 'react-test-renderer';

describe('remx support', () => {
  let MyConnectedContainer;
  let store;

  beforeEach(() => {
    jest.resetModules();
    MyConnectedContainer = require('./component').default;
    store = require('./store');
  });

  it('renders normally', () => {
    const tree = renderer.create(<MyConnectedContainer />);
    expect(tree.toJSON().children).toEqual(['no name']);
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(<MyConnectedContainer renderCountIncrement={renderCountIncrement} />);
    const instance = tree.getInstance();

    expect(tree.toJSON().children).toEqual(['no name']);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setName('Bob');
    expect(store.getters.getName()).toEqual('Bob');
    expect(tree.toJSON().children).toEqual(['Bob']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key using merge', () => {
    const renderCountIncrement = jest.fn();
    const tree = renderer.create(<MyConnectedContainer printAge={true} renderCountIncrement={renderCountIncrement} />);
    const instance = tree.getInstance();

    expect(tree.toJSON().children).toEqual(null);
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setAge(30);
    expect(store.getters.getAge()).toEqual(30);
    expect(tree.toJSON().children).toEqual(['30']);

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });
});
