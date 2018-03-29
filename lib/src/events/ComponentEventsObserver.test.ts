import { ComponentEventsObserver } from './ComponentEventsObserver';
import { Store } from '../components/Store';

describe(`ComponentEventRegistry`, () => {
  let uut: ComponentEventsObserver;
  let eventRegistry;
  let store: Store;
  let mockComponentRef;
  const refId = 'myUniqueId';

  beforeEach(() => {
    eventRegistry = {
      componentDidAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
      onNavigationButtonPressed: jest.fn()
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
    expect(eventRegistry.componentDidAppear).toHaveBeenCalledTimes(0);
    expect(eventRegistry.componentDidDisappear).toHaveBeenCalledTimes(0);
    expect(eventRegistry.onNavigationButtonPressed).toHaveBeenCalledTimes(0);
    uut.registerForAllComponents();
    expect(eventRegistry.componentDidAppear).toHaveBeenCalledTimes(1);
    expect(eventRegistry.componentDidDisappear).toHaveBeenCalledTimes(1);
    expect(eventRegistry.onNavigationButtonPressed).toHaveBeenCalledTimes(1);
  });

  it('bubbles lifecycle to component from store', () => {
    const params = {};
    expect(mockComponentRef.componentDidAppear).toHaveBeenCalledTimes(0);
    expect(mockComponentRef.componentDidDisappear).toHaveBeenCalledTimes(0);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(0);
    uut.registerForAllComponents();
    eventRegistry.componentDidAppear.mock.calls[0][0](refId);
    eventRegistry.componentDidDisappear.mock.calls[0][0](refId);
    eventRegistry.onNavigationButtonPressed.mock.calls[0][0](refId, params);
    expect(mockComponentRef.componentDidAppear).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.componentDidDisappear).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledTimes(1);
    expect(mockComponentRef.onNavigationButtonPressed).toHaveBeenCalledWith(params);
  });

  it('defensive unknown id', () => {
    uut.registerForAllComponents();
    expect(() => {
      eventRegistry.componentDidAppear.mock.calls[0][0]('bad id');
      eventRegistry.componentDidDisappear.mock.calls[0][0]('bad id');
      eventRegistry.onNavigationButtonPressed.mock.calls[0][0]('bad id', {});
    }).not.toThrow();
  });

  it('defensive method impl', () => {
    store.setRefForId('myId', {});
    uut.registerForAllComponents();
    expect(() => {
      eventRegistry.componentDidAppear.mock.calls[0][0]('myId');
      eventRegistry.componentDidDisappear.mock.calls[0][0]('myId');
      eventRegistry.onNavigationButtonPressed.mock.calls[0][0]('myId', {});
    }).not.toThrow();
  });
});
