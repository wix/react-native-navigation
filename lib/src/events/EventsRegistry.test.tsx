import { EventsRegistry } from './EventsRegistry';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';
import { CommandsObserver } from './CommandsObserver';

describe('EventsRegistry', () => {
  let uut: EventsRegistry;
  const mockNativeEventsReceiver = new NativeEventsReceiver();
  let commandsObserver: CommandsObserver;
  const mockScreenEventsRegistry = {} as any;

  beforeEach(() => {
    commandsObserver = new CommandsObserver();
    uut = new EventsRegistry(mockNativeEventsReceiver, commandsObserver, mockScreenEventsRegistry);
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

  it('delegates didAppear to nativeEventsReceiver', () => {
    const cb = jest.fn();
    uut.registerComponentDidAppearListener(cb);
    expect(mockNativeEventsReceiver.registerComponentDidAppearListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerComponentDidAppearListener).toHaveBeenCalledWith(cb);
  });

  it('delegates didDisappear to nativeEventsReceiver', () => {
    const cb = jest.fn();
    uut.registerComponentDidDisappearListener(cb);
    expect(mockNativeEventsReceiver.registerComponentDidDisappearListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerComponentDidDisappearListener).toHaveBeenCalledWith(cb);
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

  it('bottomTabSelected delegates to nativeEventsReceiver', () => {
    const cb = jest.fn();
    uut.registerBottomTabSelectedListener(cb);
    expect(mockNativeEventsReceiver.registerBottomTabSelectedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerBottomTabSelectedListener).toHaveBeenCalledWith(cb);
  });

  it(`delegates bindComponent to ComponentObserver`, () => {
    const subscription = {};
    mockScreenEventsRegistry.bindScreen = jest.fn();
    mockScreenEventsRegistry.bindScreen.mockReturnValueOnce(subscription);
    expect(uut.bindComponent({} as React.Component<any>)).toEqual(subscription);
  });
});
