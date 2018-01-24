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
    expect(nativeEventsReceiver.registerAppLaunched).toHaveBeenCalledTimes(1);
    expect(nativeEventsReceiver.registerAppLaunched).toHaveBeenCalledWith(cb);
  });

  it('exposes navigationCommands events', () => {
    const cb = jest.fn();
    uut.navigationCommands(cb);
    expect(nativeEventsReceiver.registerNavigationCommands).toHaveBeenCalledTimes(1);
    expect(nativeEventsReceiver.registerNavigationCommands).toHaveBeenCalledWith(cb);
  });

  it('exposes componentLifecycle events', () => {
    const cb = jest.fn();
    uut.componentLifecycle(cb);
    expect(nativeEventsReceiver.registerComponentLifecycle).toHaveBeenCalledTimes(1);
    expect(nativeEventsReceiver.registerComponentLifecycle).toHaveBeenCalledWith(cb);
  });
});
