import Lifecycle from './Lifecycle';
import Store from '../containers/Store';

describe('Lifecycle', () => {
  let uut;
  let mockRef;

  beforeEach(() => {
    mockRef = {
      onStart: jest.fn(),
      onStop: jest.fn()
    };
    store = new Store();
    store.setRefForId('myUniqueId', mockRef);

    uut = new Lifecycle(store);
  });

  describe('containerStart', () => {
    it('calls onStart on container ref from store', () => {
      uut.containerStart('myUniqueId');
      expect(mockRef.onStart).toHaveBeenCalledTimes(1);
    });

    it('skips undefined refs', () => {
      uut.containerStart('myUniqueId2');
      expect(mockRef.onStart).not.toHaveBeenCalled();
    });

    it('skips unimplemented onStart', () => {
      mockRef = {};
      expect(mockRef.onStart).toBeUndefined();
      store.setRefForId('myUniqueId', mockRef);
      uut.containerStart('myUniqueId');
    });
  });

  describe('containerStop', () => {
    it('calls onStop on container ref from store', () => {
      uut.containerStop('myUniqueId');
      expect(mockRef.onStop).toHaveBeenCalledTimes(1);
    });

    it('skips undefined refs', () => {
      uut.containerStop('myUniqueId2');
      expect(mockRef.onStop).not.toHaveBeenCalled();
    });

    it('skips unimplemented onStop', () => {
      mockRef = {};
      expect(mockRef.onStop).toBeUndefined();
      store.setRefForId('myUniqueId', mockRef);
      uut.containerStop('myUniqueId');
    });
  });
});
