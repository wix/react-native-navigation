import UniqueIdProvider from './UniqueIdProvider';

describe('UniqueIdProvider', () => {
  let uut;

  beforeEach(() => {
    uut = new UniqueIdProvider();
  });

  it('provides uniqueId with optional prefix', () => {
    expect(uut.generate()).toEqual('1');
    expect(uut.generate()).toEqual('2');
    expect(uut.generate('prefix')).toEqual('prefix3');
    expect(uut.generate('prefix')).toEqual('prefix4');
    expect(uut.generate('other')).toEqual('other5');
  });

  it('id is unique across instances', () => {
    expect(uut.generate()).not.toEqual('1');
  });
});
