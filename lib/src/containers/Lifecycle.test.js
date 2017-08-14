const Lifecycle = require('./Lifecycle');
const Store = require('../containers/Store');

describe('Lifecycle', () => {
  let store;
  let uut;
  let mockRef;

  beforeEach(() => {
    mockRef = {
      didAppear: jest.fn(),
      didDisappear: jest.fn()
    };
    store = new Store();
    store.setRefForContainerId('myUniqueId', mockRef);

    uut = new Lifecycle(store);
  });

  describe('containerDidAppear', () => {
    it('calls didAppear on container ref from store', () => {
      uut.containerDidAppear('myUniqueId');
      expect(mockRef.didAppear).toHaveBeenCalledTimes(1);
    });

    it('skips undefined refs', () => {
      uut.containerDidAppear('myUniqueId2');
      expect(mockRef.didAppear).not.toHaveBeenCalled();
    });

    it('skips unimplemented didAppear', () => {
      mockRef = {};
      expect(mockRef.didAppear).toBeUndefined();
      store.setRefForContainerId('myUniqueId', mockRef);
      uut.containerDidAppear('myUniqueId');
    });
  });

  describe('containerDidDisappear', () => {
    it('calls didDisappear on container ref from store', () => {
      uut.containerDidDisappear('myUniqueId');
      expect(mockRef.didDisappear).toHaveBeenCalledTimes(1);
    });

    it('skips undefined refs', () => {
      uut.containerDidDisappear('myUniqueId2');
      expect(mockRef.didDisappear).not.toHaveBeenCalled();
    });

    it('skips unimplemented didDisappear', () => {
      mockRef = {};
      expect(mockRef.didDisappear).toBeUndefined();
      store.setRefForContainerId('myUniqueId', mockRef);
      uut.containerDidDisappear('myUniqueId');
    });
  });
});
