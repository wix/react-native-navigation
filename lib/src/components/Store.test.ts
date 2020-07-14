import * as React from 'react';
import { Store } from './Store';
import { IWrappedComponent } from './ComponentWrapper';

describe('Store', () => {
  let uut: Store;

  beforeEach(() => {
    uut = new Store();
  });

  it('initial state', () => {
    expect(uut.getPropsForId('component1')).toEqual({});
  });

  it('holds props by id', () => {
    uut.updateProps('component1', { a: 1, b: 2 });
    expect(uut.getPropsForId('component1')).toEqual({ a: 1, b: 2 });
  });

  it('defensive for invalid Id and props', () => {
    uut.updateProps('component1', undefined);
    expect(uut.getPropsForId('component1')).toEqual({});
  });

  it('holds original components classes by componentName', () => {
    const MyWrappedComponent = () => class MyComponent extends React.Component {};
    uut.setComponentClassForName('example.mycomponent', MyWrappedComponent);
    expect(uut.getComponentClassForName('example.mycomponent')).toEqual(MyWrappedComponent);
  });

  it('clear props by component id when clear component', () => {
    uut.updateProps('refUniqueId', { foo: 'bar' });
    uut.clearComponent('refUniqueId');
    expect(uut.getPropsForId('refUniqueId')).toEqual({});
  });

  it('clear instance by component id when clear component', () => {
    uut.setComponentInstance('refUniqueId', ({} as IWrappedComponent));
    uut.clearComponent('refUniqueId');
    expect(uut.getComponentInstance('refUniqueId')).toEqual(undefined);
  });

  it('holds component instance by id', () => {
    uut.setComponentInstance('component1', ({} as IWrappedComponent));
    expect(uut.getComponentInstance('component1')).toEqual({});
  });

  it('calls component setProps when set props by id', () => {
    const instance: any = {setProps: jest.fn()};
    const props = { foo: 'bar' };

    uut.setComponentInstance('component1', instance);
    uut.updateProps('component1', props);

    expect(instance.setProps).toHaveBeenCalledWith(props);
  });

  it('not throw exception when set props by id component not found', () => {
    expect(() => uut.updateProps('component1', { foo: 'bar' })).not.toThrow();
  });

  it('tries to register components lazily when given a lazy registrator', () => {
    const MyLazyComponent = () => class MyComponent extends React.Component {};
    const MyEagerComponent = () => class MyComponent extends React.Component {};
    uut.setComponentClassForName('eager', MyEagerComponent);
    const lazyRegistrator = jest.fn((name) => {
      if (name === 'lazy') {
        uut.setComponentClassForName(name, MyLazyComponent);
      }
    });
    uut.setLazyComponentRegistrator(lazyRegistrator);

    expect(uut.getComponentClassForName('eager')).toEqual(MyEagerComponent);
    expect(uut.getComponentClassForName('lazy')).toEqual(MyLazyComponent);
    expect(uut.getComponentClassForName('lazy')).toEqual(MyLazyComponent);
    expect(lazyRegistrator).toHaveBeenCalledTimes(1);
  });
});
