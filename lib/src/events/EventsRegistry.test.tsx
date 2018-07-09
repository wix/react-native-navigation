import { EventsRegistry } from './EventsRegistry';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';
import { CommandsObserver } from './CommandsObserver';
import { LifecycleEventType } from '../adapters/NativeEventsReceiver';

describe('EventsRegistry', () => {
  let uut: EventsRegistry;
  const mockNativeEventsReceiver = new NativeEventsReceiver();
  let commandsObserver: CommandsObserver;

  beforeEach(() => {
    commandsObserver = new CommandsObserver();
    uut = new EventsRegistry(mockNativeEventsReceiver, commandsObserver);
  });

  it('exposes appLaunched event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerAppLaunchedListener.mockReturnValueOnce(subscription);

    const result = uut.registerAppLaunchedListener(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerAppLaunchedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerAppLaunchedListener).toHaveBeenCalledWith(cb);
  });

  it('exposes componentLifecycle event', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerComponentLifecycleListener.mockReturnValueOnce(subscription);

    const result = uut.registerComponentLifecycleListener(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerComponentLifecycleListener).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerComponentLifecycleListener.mock.calls[0][0]({ type: LifecycleEventType.ComponentDidAppear, componentId: 'theId', componentName: 'theName' });
    expect(cb).toHaveBeenCalledWith({ type: 'ComponentDidAppear', componentId: 'theId', componentName: 'theName' });
  });

  it('exposes registerCommandListener registers listener to commandObserver', () => {
    const cb = jest.fn();
    const result = uut.registerCommandListener(cb);
    expect(result).toBeDefined();
    commandsObserver.notify('theCommandName', { x: 1 });
    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb).toHaveBeenCalledWith('theCommandName', { x: 1 });
  });

  it('registerCommandListener unregister', () => {
    const cb = jest.fn();
    const result = uut.registerCommandListener(cb);
    result.remove();
    commandsObserver.notify('theCommandName', { x: 1 });
    expect(cb).not.toHaveBeenCalled();
  });

  it('registerCommandCompletedListener', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerCommandCompletedListener.mockReturnValueOnce(subscription);

    const result = uut.registerCommandCompletedListener(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerCommandCompletedListener).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerCommandCompletedListener.mock.calls[0][0]({ commandId: 'theCommandId', completionTime: 12345, params: { a: 1 } });
    expect(cb).toHaveBeenCalledWith({ commandId: 'theCommandId', completionTime: 12345, params: { a: 1 } });
  });

  it('registerNativeEventListener', () => {
    const subscription = {};
    const cb = jest.fn();
    mockNativeEventsReceiver.registerNativeEventListener.mockReturnValueOnce(subscription);

    const result = uut.registerNativeEventListener(cb);

    expect(result).toBe(subscription);
    expect(mockNativeEventsReceiver.registerNativeEventListener).toHaveBeenCalledTimes(1);

    mockNativeEventsReceiver.registerNativeEventListener.mock.calls[0][0]({ name: 'the event name', params: { a: 1 } });
    expect(cb).toHaveBeenCalledWith('the event name', { a: 1 });
  });
});
