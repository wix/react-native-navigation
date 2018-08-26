import * as React from 'react';
import * as renderer from 'react-test-renderer';
import { ComponentEventsObserver } from './ComponentEventsObserver';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';

describe('ComponentEventsObserver', () => {
  const mockEventsReceiver = new NativeEventsReceiver();
  const uut = new ComponentEventsObserver(mockEventsReceiver);
  const didAppearFn = jest.fn();
  const didDisappearFn = jest.fn();
  const didMountFn = jest.fn();
  const willUnmountFn = jest.fn();
  const navigationButtonPressedFn = jest.fn();
  const searchBarUpdatedFn = jest.fn();
  const searchBarCancelPressedFn = jest.fn();
  const modalDismissedFn = jest.fn();
  let subscription;

  class SimpleScreen extends React.Component<any, any> {
    render() {
      return 'Hello';
    }
  }

  class BoundScreen extends React.Component<any, any> {
    constructor(props) {
      super(props);
      subscription = uut.bindComponent(this);
    }

    componentDidMount() {
      didMountFn();
    }

    componentWillUnmount() {
      willUnmountFn();
    }

    componentDidAppear() {
      didAppearFn();
    }

    componentDidDisappear() {
      didDisappearFn();
    }

    navigationButtonPressed(event) {
      navigationButtonPressedFn(event);
    }

    modalDismissed(event) {
      modalDismissedFn(event);
    }

    searchBarUpdated(event) {
      searchBarUpdatedFn(event);
    }

    searchBarCancelPressed(event) {
      searchBarCancelPressedFn(event);
    }

    render() {
      return 'Hello';
    }
  }

  it(`bindComponent expects a component with componentId`, () => {
    const tree = renderer.create(<SimpleScreen />);
    expect(() => uut.bindComponent(tree.getInstance() as any)).toThrow('');
    const tree2 = renderer.create(<SimpleScreen componentId={123} />);
    expect(() => uut.bindComponent(tree2.getInstance() as any)).toThrow('');
  });

  it(`bindComponent notifies listeners by componentId on events`, () => {
    const tree = renderer.create(<BoundScreen componentId={'myCompId'} />);
    expect(tree.toJSON()).toBeDefined();
    expect(didMountFn).toHaveBeenCalledTimes(1);
    expect(didAppearFn).not.toHaveBeenCalled();
    expect(didDisappearFn).not.toHaveBeenCalled();
    expect(willUnmountFn).not.toHaveBeenCalled();

    uut.notifyComponentDidAppear({ componentId: 'myCompId', componentName: 'doesnt matter' });
    expect(didAppearFn).toHaveBeenCalledTimes(1);

    uut.notifyComponentDidDisappear({ componentId: 'myCompId', componentName: 'doesnt matter' });
    expect(didDisappearFn).toHaveBeenCalledTimes(1);

    uut.notifyNavigationButtonPressed({ componentId: 'myCompId', buttonId: 'myButtonId' });
    expect(navigationButtonPressedFn).toHaveBeenCalledTimes(1);
    expect(navigationButtonPressedFn).toHaveBeenCalledWith({ buttonId: 'myButtonId', componentId: 'myCompId' });

    uut.notifyModalDismissed({ componentId: 'myCompId' });
    expect(modalDismissedFn).toHaveBeenCalledTimes(1);
    expect(modalDismissedFn).toHaveBeenLastCalledWith({ componentId: 'myCompId' })

    uut.notifySearchBarUpdated({ componentId: 'myCompId', text: 'theText', isFocused: true });
    expect(searchBarUpdatedFn).toHaveBeenCalledTimes(1);
    expect(searchBarUpdatedFn).toHaveBeenCalledWith({ componentId: 'myCompId', text: 'theText', isFocused: true });

    uut.notifySearchBarCancelPressed({ componentId: 'myCompId' });
    expect(searchBarCancelPressedFn).toHaveBeenCalledTimes(1);
    expect(searchBarCancelPressedFn).toHaveBeenCalledWith({ componentId: 'myCompId' });

    tree.unmount();
    expect(willUnmountFn).toHaveBeenCalledTimes(1);
  });

  it(`doesnt call other componentIds`, () => {
    renderer.create(<BoundScreen componentId={'myCompId'} />);
    uut.notifyComponentDidAppear({ componentId: 'other', componentName: 'doesnt matter' });
    expect(didAppearFn).not.toHaveBeenCalled();
  });

  it(`doesnt call unimplemented methods`, () => {
    const tree = renderer.create(<SimpleScreen componentId={'myCompId'} />);
    expect((tree.getInstance() as any).componentDidAppear).toBeUndefined();
    uut.bindComponent(tree.getInstance() as any);
    uut.notifyComponentDidAppear({ componentId: 'myCompId', componentName: 'doesnt matter' });
  });

  it(`returns unregister fn`, () => {
    renderer.create(<BoundScreen componentId={'123'} />);

    uut.notifyComponentDidAppear({ componentId: '123', componentName: 'doesnt matter' });
    expect(didAppearFn).toHaveBeenCalledTimes(1);

    subscription.remove();

    uut.notifyComponentDidAppear({ componentId: '123', componentName: 'doesnt matter' });
    expect(didAppearFn).toHaveBeenCalledTimes(1);
  });

  it(`removeAllListenersForComponentId`, () => {
    renderer.create(<BoundScreen componentId={'123'} />);
    renderer.create(<BoundScreen componentId={'123'} />);

    uut.unmounted('123');

    uut.notifyComponentDidAppear({ componentId: '123', componentName: 'doesnt matter' });
    expect(didAppearFn).not.toHaveBeenCalled();
  });

  it(`supports multiple listeners with same componentId`, () => {
    const tree1 = renderer.create(<SimpleScreen componentId={'myCompId'} />);
    const tree2 = renderer.create(<SimpleScreen componentId={'myCompId'} />);
    const instance1 = tree1.getInstance() as any;
    const instance2 = tree2.getInstance() as any;
    instance1.componentDidAppear = jest.fn();
    instance2.componentDidAppear = jest.fn();

    const result1 = uut.bindComponent(instance1);
    const result2 = uut.bindComponent(instance2);
    expect(result1).not.toEqual(result2);

    uut.notifyComponentDidAppear({ componentId: 'myCompId', componentName: 'doesnt matter' });

    expect(instance1.componentDidAppear).toHaveBeenCalledTimes(1);
    expect(instance2.componentDidAppear).toHaveBeenCalledTimes(1);

    result2.remove();

    uut.notifyComponentDidAppear({ componentId: 'myCompId', componentName: 'doesnt matter' });
    expect(instance1.componentDidAppear).toHaveBeenCalledTimes(2);
    expect(instance2.componentDidAppear).toHaveBeenCalledTimes(1);

    result1.remove();

    uut.notifyComponentDidAppear({ componentId: 'myCompId', componentName: 'doesnt matter' });
    expect(instance1.componentDidAppear).toHaveBeenCalledTimes(2);
    expect(instance2.componentDidAppear).toHaveBeenCalledTimes(1);
  });

  it(`register for all native component events notifies self on events, once`, () => {
    expect(mockEventsReceiver.registerComponentDidAppearListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerComponentDidDisappearListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerNavigationButtonPressedListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerSearchBarUpdatedListener).not.toHaveBeenCalled();
    expect(mockEventsReceiver.registerSearchBarCancelPressedListener).not.toHaveBeenCalled();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    uut.registerOnceForAllComponentEvents();
    expect(mockEventsReceiver.registerComponentDidAppearListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerComponentDidDisappearListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerNavigationButtonPressedListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerSearchBarUpdatedListener).toHaveBeenCalledTimes(1);
    expect(mockEventsReceiver.registerSearchBarCancelPressedListener).toHaveBeenCalledTimes(1);
  });
});
