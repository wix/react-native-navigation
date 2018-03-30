import { Store } from './Store';

describe('Store', () => {
  let uut;

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
    uut.setPropsForId(undefined, undefined);
    expect(uut.getPropsForId('component1')).toEqual({});
  });

  it('holds original components classes by componentName', () => {
    const MyComponent = class {
      //
    };
    uut.setOriginalComponentClassForName('example.mycomponent', MyComponent);
    expect(uut.getOriginalComponentClassForName('example.mycomponent')).toEqual(MyComponent);
  });

  it('holds component refs by id', () => {
    const ref = {};
    uut.setRefForId('refUniqueId', ref);
    expect(uut.getRefForId('other')).toBeUndefined();
    expect(uut.getRefForId('refUniqueId')).toBe(ref);
  });

  it('clean by id', () => {
    uut.setRefForId('refUniqueId', {});
    uut.setPropsForId('refUniqueId', { foo: 'bar' });

    uut.cleanId('refUniqueId');

    expect(uut.getRefForId('refUniqueId')).toBeUndefined();
    expect(uut.getPropsForId('refUniqueId')).toEqual({});
  });
});
