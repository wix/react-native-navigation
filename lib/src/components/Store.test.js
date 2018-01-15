const Store = require('./Store');

describe('Store', () => {
  let uut;

  beforeEach(() => {
    uut = new Store();
  });

  it('initial state', () => {
    expect(uut.getPropsForComponentId('component1')).toEqual({});
  });

  it('holds props by componentId', () => {
    uut.setPropsForComponentId('component1', { a: 1, b: 2 });
    expect(uut.getPropsForComponentId('component1')).toEqual({ a: 1, b: 2 });
  });

  it('defensive for invalid componentId and props', () => {
    uut.setPropsForComponentId('component1', undefined);
    uut.setPropsForComponentId(undefined, undefined);
    expect(uut.getPropsForComponentId('component1')).toEqual({});
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
    uut.setRefForComponentId('refUniqueId', ref);
    expect(uut.getRefForComponentId('other')).toBeUndefined();
    expect(uut.getRefForComponentId('refUniqueId')).toBe(ref);
  });

  it('clean by id', () => {
    uut.setRefForComponentId('refUniqueId', {});
    uut.setPropsForComponentId('refUniqueId', { foo: 'bar' });

    uut.cleanId('refUniqueId');

    expect(uut.getRefForComponentId('refUniqueId')).toBeUndefined();
    expect(uut.getPropsForComponentId('refUniqueId')).toEqual({});
  });
});

