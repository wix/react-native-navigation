import { EventsRegistry } from './EventsRegistry';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';

describe('EventsRegistry', () => {
  let uut: EventsRegistry;
  let mockNativeEventsReceiver;

  beforeEach(() => {
    mockNativeEventsReceiver = new NativeEventsReceiver();
    uut = new EventsRegistry(mockNativeEventsReceiver);
  });

  it('exposes onAppLaunched event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerOnAppLaunched.mockReturnValueOnce(subscription);

    const result = uut.onAppLaunched(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerOnAppLaunched).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerOnAppLaunched).toHaveBeenCalledWith(cb);
  });

  it('exposes componentDidAppear event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerComponentDidAppear.mockReturnValueOnce(subscription);

    const result = uut.componentDidAppear(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerComponentDidAppear).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerComponentDidAppear.mock.calls[0][0]({ componentId: 'theId', componentName: 'theName' });
    expect(cb).toHaveBeenCalledWith('theId', 'theName');
  });

  it('exposes componentDidDisappear event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerComponentDidDisappear.mockReturnValueOnce(subscription);

    const result = uut.componentDidDisappear(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerComponentDidDisappear).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerComponentDidDisappear.mock.calls[0][0]({ componentId: 'theId', componentName: 'theName' });
    expect(cb).toHaveBeenCalledWith('theId', 'theName');
  });

  it('exposes onNavigationButtonPressed event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerOnNavigationButtonPressed.mockReturnValueOnce(subscription);

    const result = uut.onNavigationButtonPressed(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerOnNavigationButtonPressed).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerOnNavigationButtonPressed.mock.calls[0][0]({ componentId: 'theId', buttonId: 'theBtnId' });
    expect(cb).toHaveBeenCalledWith('theId', 'theBtnId');
  });
});
