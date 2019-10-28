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
    uut.setPropsForId('component1', { a: 1, b: 2 });
    expect(uut.getPropsForId('component1')).toEqual({ a: 1, b: 2 });
  });

  it('defensive for invalid Id and props', () => {
    uut.setPropsForId('component1', undefined);
    expect(uut.getPropsForId('component1')).toEqual({});
  });

  it('holds original components classes by componentName', () => {
    const MyWrappedComponent = () => class MyComponent extends React.Component {};
    uut.setComponentClassForName('example.mycomponent', MyWrappedComponent);
    expect(uut.getComponentClassForName('example.mycomponent')).toEqual(MyWrappedComponent);
  });

  it('clean props by component id', () => {
    uut.setPropsForId('refUniqueId', { foo: 'bar' });
    uut.cleanId('refUniqueId');
    expect(uut.getPropsForId('refUniqueId')).toEqual({});
  });

  it('clean instance by component id', () => {
    uut.setComponentInstance('refUniqueId', ({} as IWrappedComponent));
    uut.cleanId('refUniqueId');
    expect(uut.getComponentInstance('refUniqueId')).toEqual(undefined);
  });

  it('holds component instance by id', () => {
    uut.setComponentInstance('component1', ({} as IWrappedComponent));
    expect(uut.getComponentInstance('component1')).toEqual({});
  });

  it('calls component setProps when set propd by id', () => {
    const instance: any = {setProps: jest.fn()};
    const props = { foo: 'bar' };

    uut.setComponentInstance('component1', instance);
    uut.setPropsForId('component1', props);

    expect(instance.setProps).toHaveBeenCalledWith(props);
  });

  it('not throw exeption when set props by id component not found', () => {
    expect(() => uut.setPropsForId('component1', { foo: 'bar' })).not.toThrow();
  });
});
