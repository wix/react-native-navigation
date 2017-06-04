import _ from 'lodash';
import PrivateEventsListener from './PrivateEventsListener';
import NativeEventsReceiver from '../adapters/NativeEventsReceiver.mock';
import Store from '../containers/Store';

describe('PrivateEventsListener', () => {
  let uut;
  let nativeEventsReceiver;
  let store;

  beforeEach(() => {
    nativeEventsReceiver = new NativeEventsReceiver();
    store = new Store();
    uut = new PrivateEventsListener(nativeEventsReceiver, store);
  });

  it('register and handle containerStart', () => {
    const mockRef = {
      onStart: jest.fn()
    };
    store.setRefForContainerId('myContainerId', mockRef);
    uut.listenAndHandlePrivateEvents();
    expect(nativeEventsReceiver.containerStart).toHaveBeenCalledTimes(1);
    const callbackFunction = nativeEventsReceiver.containerStart.mock.calls[0][0];
    expect(callbackFunction).toBeInstanceOf(Function);

    expect(mockRef.onStart).not.toHaveBeenCalled();
    callbackFunction('myContainerId');

    expect(mockRef.onStart).toHaveBeenCalledTimes(1);
  });

  it('register and listen containerStop', () => {
    uut.listenAndHandlePrivateEvents();
    expect(nativeEventsReceiver.containerStop).toHaveBeenCalledTimes(1);
  });
});
