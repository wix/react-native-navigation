import { EventsRegistry } from './EventsRegistry';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';
import { CommandsObserver } from './CommandsObserver';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

describe('EventsRegistry', () => {
  let uut: EventsRegistry;
  const mockNativeEventsReceiver = new NativeEventsReceiver();
  let commandsObserver: CommandsObserver;
  const mockScreenEventsRegistry = {} as any;

  beforeEach(() => {
    commandsObserver = new CommandsObserver(new UniqueIdProvider());
    uut = new EventsRegistry(mockNativeEventsReceiver, commandsObserver, mockScreenEventsRegistry);
    uut.registerOnceForAllEvents();
  });

  it('exposes appLaunched event', () => {
    const cb = jest.fn();
    const otherCb = jest.fn();

    const subscription = uut.registerAppLaunchedListener(cb);
    uut.registerComponentDidAppearListener(otherCb);

    for (const call of mockNativeEventsReceiver.registerAppLaunchedListener.mock.calls) {
      call[0]();
    }

    expect(cb).toHaveBeenCalledTimes(1);

    subscription.remove();
    for (const call of mockNativeEventsReceiver.registerAppLaunchedListener.mock.calls) {
      call[0]();
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(otherCb).not.toHaveBeenCalled();
  });

  it('delegates didAppear to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerComponentDidAppearListener(cb);

    for (const call of mockNativeEventsReceiver.registerComponentDidAppearListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates didDisappear to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerComponentDidDisappearListener(cb);

    for (const call of mockNativeEventsReceiver.registerComponentDidDisappearListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates commandCompleted to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerCommandCompletedListener(cb);

    for (const call of mockNativeEventsReceiver.registerCommandCompletedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates BottomTabsSelected to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerBottomTabSelectedListener(cb);

    for (const call of mockNativeEventsReceiver.registerBottomTabSelectedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates navigationButtonPressed to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerNavigationButtonPressedListener(cb);

    for (const call of mockNativeEventsReceiver.registerNavigationButtonPressedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates modalDismissed to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerModalDismissedListener(cb);

    for (const call of mockNativeEventsReceiver.registerModalDismissedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates searchBarUpdated to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerSearchBarUpdatedListener(cb);

    for (const call of mockNativeEventsReceiver.registerSearchBarUpdatedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates searchBarCancelPressed to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerSearchBarCancelPressedListener(cb);

    for (const call of mockNativeEventsReceiver.registerSearchBarCancelPressedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates previewCompleted to nativeEventsReceiver', () => {
    const cb = jest.fn();
    const event = {};
    uut.registerPreviewCompletedListener(cb);

    for (const call of mockNativeEventsReceiver.registerPreviewCompletedListener.mock.calls) {
      call[0](event);
    }

    expect(cb).toHaveBeenCalledTimes(1);
    expect(cb.mock.calls[0][0]).toBe(event);
  });

  it('delegates registerCommandListener to commandObserver', () => {
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

  it(`delegates bindComponent to ComponentObserver`, () => {
    const subscription = {};
    mockScreenEventsRegistry.bindComponent = jest.fn();
    mockScreenEventsRegistry.bindComponent.mockReturnValueOnce(subscription);
    expect(uut.bindComponent({} as React.Component<any>)).toEqual(subscription);
  });

  it('registers all events once', () => {
    jest.clearAllMocks();
    uut = new EventsRegistry(mockNativeEventsReceiver, commandsObserver, mockScreenEventsRegistry);

    expect(mockNativeEventsReceiver.registerAppLaunchedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerComponentDidAppearListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerComponentDidDisappearListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerNavigationButtonPressedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerModalDismissedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerSearchBarUpdatedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerSearchBarCancelPressedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerPreviewCompletedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerCommandCompletedListener).not.toHaveBeenCalled();
    expect(mockNativeEventsReceiver.registerBottomTabSelectedListener).not.toHaveBeenCalled();
    uut.registerOnceForAllEvents();
    uut.registerOnceForAllEvents();
    uut.registerOnceForAllEvents();
    uut.registerOnceForAllEvents();
    expect(mockNativeEventsReceiver.registerAppLaunchedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerComponentDidAppearListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerComponentDidDisappearListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerNavigationButtonPressedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerModalDismissedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerSearchBarUpdatedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerSearchBarCancelPressedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerPreviewCompletedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerCommandCompletedListener).toHaveBeenCalledTimes(1);
    expect(mockNativeEventsReceiver.registerBottomTabSelectedListener).toHaveBeenCalledTimes(1);
  });
});
