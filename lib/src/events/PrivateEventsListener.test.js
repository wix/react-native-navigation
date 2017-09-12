const PrivateEventsListener = require('./PrivateEventsListener');
const NativeEventsReceiver = require('../adapters/NativeEventsReceiver.mock');
const Store = require('../containers/Store');

describe('PrivateEventsListener', () => {
  let uut;
  let nativeEventsReceiver;
  let store;

  beforeEach(() => {
    nativeEventsReceiver = new NativeEventsReceiver();
    store = new Store();
    uut = new PrivateEventsListener(nativeEventsReceiver, store);
  });

  it('register and handle containerDidAppear', () => {
    const mockRef = {
      didAppear: jest.fn()
    };
    store.setRefForContainerId('myContainerId', mockRef);
    uut.listenAndHandlePrivateEvents();
    expect(nativeEventsReceiver.containerDidAppear).toHaveBeenCalledTimes(1);
    const callbackFunction = nativeEventsReceiver.containerDidAppear.mock.calls[0][0];
    expect(callbackFunction).toBeInstanceOf(Function);

    expect(mockRef.didAppear).not.toHaveBeenCalled();
    callbackFunction('myContainerId');

    expect(mockRef.didAppear).toHaveBeenCalledTimes(1);
  });

  it('register and listen containerDidDisappear', () => {
    uut.listenAndHandlePrivateEvents();
    expect(nativeEventsReceiver.containerDidDisappear).toHaveBeenCalledTimes(1);
  });

  it('register and handle onNavigationButtonPressed', () => {
    const mockRef = {
      onNavigationButtonPressed: jest.fn()
    };
    store.setRefForContainerId('myContainerId', mockRef);
    uut.listenAndHandlePrivateEvents();
    expect(nativeEventsReceiver.navigationButtonPressed).toHaveBeenCalledTimes(1);
    const callbackFunction = nativeEventsReceiver.navigationButtonPressed.mock.calls[0][0];
    expect(callbackFunction).toBeInstanceOf(Function);

    expect(mockRef.onNavigationButtonPressed).not.toHaveBeenCalled();
    callbackFunction({ containerId: 'myContainerId', buttonId: 'myButtonId' });

    expect(mockRef.onNavigationButtonPressed).toHaveBeenCalledTimes(1);
  });
});
