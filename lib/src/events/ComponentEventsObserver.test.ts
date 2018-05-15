import { ComponentEventsObserver } from './ComponentEventsObserver';
import { Store } from '../components/Store';

describe(`ComponentEventsObserver`, () => {
  let uut: ComponentEventsObserver;
  let eventRegistry;
  let store: Store;
  let mockComponentRef;
  const refId = 'myUniqueId';

  beforeEach(() => {
    eventRegistry = {
      registerComponentDidAppearListener: jest.fn(),
      registerComponentDidDisappearListener: jest.fn(),
      registerNativeEventListener: jest.fn()
    };

    mockComponentRef = {
      componentDidAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
      onNavigationButtonPressed: jest.fn()
    };

    store = new Store();
    store.setRefForId(refId, mockComponentRef);

    uut = new ComponentEventsObserver(eventRegistry, store);
  });

  it('register for lifecycle events on eventRegistry', () => {
    expect(eventRegistry.registerComponentDidAppearListener).toHaveBeenCalledTimes(0);
    expect(eventRegistry.registerComponentDidDisappearListener).toHaveBeenCalledTimes(0);
    expect(eventRegistry.registerNativeEventListener).toHaveBeenCalledTimes(0);
    uut.registerForAllComponents();
    expect(eventRegistry.registerComponentDidAppearListener).toHaveBeenCalledTimes(1);
    expect(eventRegistry.registerComponentDidDisappearListener).toHaveBeenCalledTimes(1);
    expect(eventRegistry.registerNativeEventListener).toHaveBeenCalledTimes(1);
  });

  it('bubbles lifecycle to component from store', () => {
    const params = {};
    expect(mockComponentRef.componentDidAppear).toHaveBeenCalledTimes(0);
    expect(mockComponentRef.componentDidDisappear).toHaveBeenCalledTimes(0);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(0);
    uut.registerForAllComponents();
    eventRegistry.registerComponentDidAppearListener.mock.calls[0][0](refId);
    eventRegistry.registerComponentDidDisappearListener.mock.calls[0][0](refId);
    eventRegistry.registerNativeEventListener.mock.calls[0][0](refId, params);
    expect(mockComponentRef.componentDidAppear).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.componentDidDisappear).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(0);
  });

  it('bubbles onNavigationButtonPressed to component by id', () => {
    const params = {
      componentId: refId,
      buttonId: 'theButtonId'
    };
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(0);
    uut.registerForAllComponents();

    eventRegistry.registerNativeEventListener.mock.calls[0][0]('some other event name', params);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(0);

    eventRegistry.registerNativeEventListener.mock.calls[0][0]('buttonPressed', params);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledWith('theButtonId');
  });

  it('defensive unknown id', () => {
    uut.registerForAllComponents();
    expect(() => {
      eventRegistry.registerComponentDidAppearListener.mock.calls[0][0]('bad id');
      eventRegistry.registerComponentDidDisappearListener.mock.calls[0][0]('bad id');
      eventRegistry.registerNativeEventListener.mock.calls[0][0]('buttonPressed', { componentId: 'bad id' });
    }).not.toThrow();
  });

  it('defensive method impl', () => {
    store.setRefForId('myId', {});
    uut.registerForAllComponents();
    expect(() => {
      eventRegistry.registerComponentDidAppearListener.mock.calls[0][0]('myId');
      eventRegistry.registerComponentDidDisappearListener.mock.calls[0][0]('myId');
      eventRegistry.registerNativeEventListener.mock.calls[0][0]('bad event name', {});
    }).not.toThrow();
  });
});
