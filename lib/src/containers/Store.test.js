import Store from './Store';

describe('Store', () => {
  let uut;

  beforeEach(() => {
    uut = new Store();
  });

  it('initial state', () => {
    expect(uut.getPropsForContainerId('container1')).toEqual({});
  });

  it('holds props by containerId', () => {
    uut.setPropsForContainerId('container1', { a: 1, b: 2 });
    expect(uut.getPropsForContainerId('container1')).toEqual({ a: 1, b: 2 });
  });

  it('defensive for invalid containerId and props', () => {
    uut.setPropsForContainerId('container1', undefined);
    uut.setPropsForContainerId(undefined, undefined);
    expect(uut.getPropsForContainerId('container1')).toEqual({});
  });

  it('holds original containers classes by containerName', () => {
    const MyComponent = class {
      //
    };
    uut.setOriginalContainerClassForName('example.mycontainer', MyComponent);
    expect(uut.getOriginalContainerClassForName('example.mycontainer')).toEqual(MyComponent);
  });

  it('holds container refs by id', () => {
    const ref = {};
    uut.setRefForId('refUniqueId', ref);
    expect(uut.getRefForId('other')).toBeUndefined();
    expect(uut.getRefForId('refUniqueId')).toBe(ref);
  });

  it('clean by id', () => {
    uut.setRefForId('refUniqueId', {});
    uut.setPropsForContainerId('refUniqueId', { foo: 'bar' });

    uut.cleanId('refUniqueId');

    expect(uut.getRefForId('refUniqueId')).toBeUndefined();
    expect(uut.getPropsForContainerId('refUniqueId')).toEqual({});
  });
});

