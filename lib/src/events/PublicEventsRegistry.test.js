const PublicEventsRegistry = require('./PublicEventsRegistry');
const NativeEventsReceiver = require('../adapters/NativeEventsReceiver.mock');

describe('PublicEventsRegistry', () => {
  let uut;
  let nativeEventsReceiver;

  beforeEach(() => {
    nativeEventsReceiver = new NativeEventsReceiver();
    uut = new PublicEventsRegistry(nativeEventsReceiver);
  });

  it('exposes onAppLaunch event', () => {
    const cb = jest.fn();
    uut.onAppLaunched(cb);
    expect(nativeEventsReceiver.appLaunched).toHaveBeenCalledTimes(1);
    expect(nativeEventsReceiver.appLaunched).toHaveBeenCalledWith(cb);
  });
});
