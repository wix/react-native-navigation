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
    nativeEventsReceiver.registerNavigationCommands.mockReturnValueOnce(() => {});
    uut.navigationCommands(cb);
    expect(nativeEventsReceiver.registerNavigationCommands).toHaveBeenCalledTimes(1);
    expect(nativeEventsReceiver.registerNavigationCommands).toHaveBeenCalledWith(cb);
  });
});
