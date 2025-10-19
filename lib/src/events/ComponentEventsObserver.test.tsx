import * as React from 'react';
import { render } from '@testing-library/react-native';
import { ComponentEventsObserver } from './ComponentEventsObserver';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';
import { EventSubscription } from '../interfaces/EventSubscription';
import { Store } from '../components/Store';
import { ComponentDidAppearEvent, ComponentWillAppearEvent } from '../interfaces/ComponentEvents';

describe('ComponentEventsObserver', () => {
  const mockEventsReceiver = new NativeEventsReceiver();
  const mockStore = new Store();
  const willAppearFn = jest.fn();
  const didAppearFn = jest.fn();
  const didDisappearFn = jest.fn();
  const didMountFn = jest.fn();
  const willUnmountFn = jest.fn();
  const navigationButtonPressedFn = jest.fn();
  const searchBarUpdatedFn = jest.fn();
  const searchBarCancelPressedFn = jest.fn();
  const previewCompletedFn = jest.fn();
  const screenPoppedFn = jest.fn();
  let subscription: EventSubscription;
  let uut: ComponentEventsObserver;

  class UnboundScreen extends React.Component<any, any> {
    constructor(props: any) {
      super(props);
    }

    componentDidMount() {
      didMountFn();
    }

    componentWillUnmount() {
      willUnmountFn();
    }

    componentWillAppear() {
      willAppearFn();
    }

    componentDidAppear() {
      didAppearFn();
    }

    componentDidDisappear() {
      didDisappearFn();
    }

    navigationButtonPressed(event: any) {
      navigationButtonPressedFn(event);
    }

    searchBarUpdated(event: any) {
      searchBarUpdatedFn(event);
    }

    searchBarCancelPressed(event: any) {
      searchBarCancelPressedFn(event);
    }

    previewCompleted(event: any) {
      previewCompletedFn(event);
    }

    screenPopped(event: any) {
      screenPoppedFn(event);
    }

    render() {
      return 'Hello';
    }
  }

  class BoundScreen extends React.Component<any, any> {
    constructor(props: any) {
      super(props);
      subscription = uut.bindComponent(this);
    }

    componentDidMount() {
      didMountFn();
    }

    componentWillUnmount() {
      willUnmountFn();
    }

    componentWillAppear(event: ComponentWillAppearEvent) {
      willAppearFn(event);
    }

    componentDidAppear(event: ComponentDidAppearEvent) {
      didAppearFn(event);
    }

    componentDidDisappear() {
      didDisappearFn();
    }

    navigationButtonPressed(event: any) {
      navigationButtonPressedFn(event);
    }

    searchBarUpdated(event: any) {
      searchBarUpdatedFn(event);
    }

    searchBarCancelPressed(event: any) {
      searchBarCancelPressedFn(event);
    }

    previewCompleted(event: any) {
      previewCompletedFn(event);
    }

    screenPopped(event: any) {
      screenPoppedFn(event);
    }

    render() {
      return 'Hello';
    }
  }

  beforeEach(() => {
    jest.clearAllMocks();
    uut = new ComponentEventsObserver(mockEventsReceiver, mockStore);
  });

  it(`bindComponent expects a component with componentId`, () => {
    // Create a mock component instance for testing
    const mockComponent = {
      props: {},
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
    };

    expect(() => uut.bindComponent(mockComponent as any)).toThrow('');

    const mockComponent2 = {
      props: { componentId: 123 },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
    };

    expect(() => uut.bindComponent(mockComponent2 as any)).toThrow('');
  });

  it(`bindComponent accepts an optional componentId`, () => {
    // Create a mock component instance with the required methods
    const mockComponent = {
      props: { componentId: 'test' },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
      componentDidAppear: didAppearFn,
      componentWillAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
    };

    uut.bindComponent(mockComponent as any, 'myCompId');
    expect(didAppearFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);
  });

  it(`bindComponent should use optional componentId if component has a componentId in props`, () => {
    const mockComponent = {
      props: { componentId: 'doNotUseThisId' },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
      componentDidAppear: didAppearFn,
      componentWillAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
    };

    uut.bindComponent(mockComponent as any, 'myCompId');

    uut.notifyComponentDidAppear({
      componentId: 'dontUseThisId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);
  });

  it(`bindComponent notifies listeners by componentId on events`, () => {
    const { unmount } = render(<BoundScreen componentId={'myCompId'} />);
    expect(didMountFn).toHaveBeenCalledTimes(1);
    expect(didAppearFn).not.toHaveBeenCalled();
    expect(didDisappearFn).not.toHaveBeenCalled();
    expect(willUnmountFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);

    uut.notifyComponentDidDisappear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didDisappearFn).toHaveBeenCalledTimes(1);

    uut.notifyNavigationButtonPressed({ componentId: 'myCompId', buttonId: 'myButtonId' });
    expect(navigationButtonPressedFn).toHaveBeenCalledTimes(1);
    expect(navigationButtonPressedFn).toHaveBeenCalledWith({
      buttonId: 'myButtonId',
      componentId: 'myCompId',
    });

    uut.notifySearchBarUpdated({ componentId: 'myCompId', text: 'theText', isFocused: true });
    expect(searchBarUpdatedFn).toHaveBeenCalledTimes(1);
    expect(searchBarUpdatedFn).toHaveBeenCalledWith({
      componentId: 'myCompId',
      text: 'theText',
      isFocused: true,
    });

    uut.notifySearchBarCancelPressed({ componentId: 'myCompId' });
    expect(searchBarCancelPressedFn).toHaveBeenCalledTimes(1);
    expect(searchBarCancelPressedFn).toHaveBeenCalledWith({ componentId: 'myCompId' });

    uut.notifyPreviewCompleted({ componentId: 'myCompId' });
    expect(previewCompletedFn).toHaveBeenCalledTimes(1);
    expect(previewCompletedFn).toHaveBeenCalledWith({ componentId: 'myCompId' });

    uut.notifyScreenPopped({ componentId: 'myCompId' });
    expect(screenPoppedFn).toHaveBeenCalledTimes(1);
    expect(screenPoppedFn).toHaveBeenLastCalledWith({ componentId: 'myCompId' });

    unmount();
    expect(willUnmountFn).toHaveBeenCalledTimes(1);
  });

  it(`registerComponentListener accepts listener object`, () => {
    render(<UnboundScreen />);
    const didAppearListenerFn = jest.fn();
    uut.registerComponentListener(
      {
        componentDidAppear: didAppearListenerFn,
      },
      'myCompId'
    );

    // Component rendered successfully
    expect(didAppearListenerFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearListenerFn).toHaveBeenCalledTimes(1);
  });

  it(`componentDidAppear should receive component props from store`, () => {
    const event = {
      componentId: 'myCompId',
      componentType: 'Component',
      passProps: {
        propA: 'propA',
      },
      componentName: 'doesnt matter',
    };
    render(<BoundScreen componentId={event.componentId} />);
    mockStore.updateProps(event.componentId, event.passProps);
    expect(didAppearFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);
    expect(didAppearFn).toHaveBeenCalledWith(event);
  });

  it(`componentWillAppear should receive component props from store`, () => {
    const event = {
      componentId: 'myCompId',
      componentType: 'Component',
      passProps: {
        propA: 'propA',
      },
      componentName: 'doesnt matter',
    };
    render(<BoundScreen componentId={event.componentId} />);
    mockStore.updateProps(event.componentId, event.passProps);
    expect(willAppearFn).not.toHaveBeenCalled();

    uut.notifyComponentWillAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(willAppearFn).toHaveBeenCalledTimes(1);
    expect(willAppearFn).toHaveBeenCalledWith(event);
  });

  it(`doesnt call other componentIds`, () => {
    render(<BoundScreen componentId={'myCompId'} />);
    uut.notifyComponentDidAppear({
      componentId: 'other',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).not.toHaveBeenCalled();
  });

  it(`doesnt call unimplemented methods`, () => {
    const mockComponent = {
      props: { componentId: 'myCompId' },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
      // Note: componentDidAppear is not defined, which is what we're testing
    };

    expect((mockComponent as any).componentDidAppear).toBeUndefined();
    uut.bindComponent(mockComponent as any);
    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
  });

  it(`returns unregister fn`, () => {
    render(<BoundScreen componentId={'123'} />);

    uut.notifyComponentDidAppear({
      componentId: '123',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);

    subscription.remove();

    uut.notifyComponentDidAppear({
      componentId: '123',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).toHaveBeenCalledTimes(1);
  });

  it(`removeAllListenersForComponentId`, () => {
    render(<BoundScreen componentId={'123'} />);
    render(<BoundScreen componentId={'123'} />);

    uut.unmounted('123');

    uut.notifyComponentDidAppear({
      componentId: '123',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(didAppearFn).not.toHaveBeenCalled();
  });

  it(`supports multiple listeners with same componentId`, () => {
    const mockComponent1 = {
      props: { componentId: 'myCompId' },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
      componentDidAppear: jest.fn(),
      componentWillAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
    };

    const mockComponent2 = {
      props: { componentId: 'myCompId' },
      componentDidMount: jest.fn(),
      componentWillUnmount: jest.fn(),
      componentDidAppear: jest.fn(),
      componentWillAppear: jest.fn(),
      componentDidDisappear: jest.fn(),
    };

    const result1 = uut.bindComponent(mockComponent1 as any);
    const result2 = uut.bindComponent(mockComponent2 as any);
    expect(result1).not.toEqual(result2);

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });

    expect(mockComponent1.componentDidAppear).toHaveBeenCalledTimes(1);
    expect(mockComponent2.componentDidAppear).toHaveBeenCalledTimes(1);

    result2.remove();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(mockComponent1.componentDidAppear).toHaveBeenCalledTimes(2);
    expect(mockComponent2.componentDidAppear).toHaveBeenCalledTimes(1);

    result1.remove();

    uut.notifyComponentDidAppear({
      componentId: 'myCompId',
      componentName: 'doesnt matter',
      componentType: 'Component',
    });
    expect(mockComponent1.componentDidAppear).toHaveBeenCalledTimes(2);
    expect(mockComponent2.componentDidAppear).toHaveBeenCalledTimes(1);
  });

  it(`register for all native component events notifies self on events, once`, () => {
    expect(mockEventsReceiver.registerComponentDidAppearListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerComponentDidDisappearListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerNavigationButtonPressedListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerSearchBarUpdatedListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerSearchBarCancelPressedListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerPreviewCompletedListener).not.toHaveBeenCalled();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    expect(mockEventsReceiver.registerComponentDidAppearListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerComponentDidDisappearListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerNavigationButtonPressedListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerSearchBarUpdatedListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerSearchBarCancelPressedListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerPreviewCompletedListener).toHaveBeenCalledTimes(1);
  });
});
