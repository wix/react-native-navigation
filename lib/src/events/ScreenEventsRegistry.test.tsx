import * as React from 'react';
import * as renderer from 'react-test-renderer';
import { ScreenEventsRegistry } from './ScreenEventsRegistry';
import { LifecycleEventType } from '../adapters/NativeEventsReceiver';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver.mock';

describe('ScreenEventsRegistry', () => {
  const mockNativeEventsReceiver = new NativeEventsReceiver();
  const uut = new ScreenEventsRegistry(mockNativeEventsReceiver);
  const didAppearFn = jest.fn();
  const didDisappearFn = jest.fn();
  const didMountFn = jest.fn();
  const willUnmountFn = jest.fn();

  class SimpleScreen extends React.Component<any, any> {
    render() {
      return 'Hello';
    }
  }

  class Screen extends React.Component<any, any> {
    constructor(props) {
      super(props);
      uut.bindScreen(this);
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

    render() {
      return 'Hello';
    }
  }

  it(`bindScreen expects a screen with componentId`, () => {
    const tree = renderer.create(<SimpleScreen />);
    expect(() => uut.bindScreen(tree.getInstance() as any)).toThrow();
    const tree2 = renderer.create(<SimpleScreen componentId={123} />);
    expect(() => uut.bindScreen(tree2.getInstance() as any)).toThrow();
  });

  it(`bindScreen register for lifecycle events by the screen's id, calls events on this screen`, () => {
    const tree = renderer.create(<Screen componentId={'myCompId'} />);
    expect(tree.toJSON()).toBeDefined();
    expect(didAppearFn).not.toHaveBeenCalled();
    expect(didDisappearFn).not.toHaveBeenCalled();

    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidAppear,
      componentId: 'myCompId',
      componentName: 'doesnt matter'
    });

    expect(didAppearFn).toHaveBeenCalledTimes(1);

    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidDisappear,
      componentId: 'myCompId',
      componentName: 'doesnt matter'
    });
    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentWillUnmount,
      componentId: 'myCompId',
      componentName: 'doesnt matter'
    });

    expect(didDisappearFn).toHaveBeenCalledTimes(1);
    expect(willUnmountFn).not.toHaveBeenCalled();
  });

  it(`bindScreen componentId`, () => {
    renderer.create(<Screen componentId={'myCompId'} />);
    expect(didAppearFn).not.toHaveBeenCalled();
    expect(didDisappearFn).not.toHaveBeenCalled();

    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidAppear,
      componentId: 'otherScreen',
      componentName: 'doesnt matter'
    });

    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidDisappear,
      componentId: 'anotherScreen',
      componentName: 'doesnt matter'
    });

    expect(didAppearFn).not.toHaveBeenCalled();
    expect(didDisappearFn).not.toHaveBeenCalled();
  });

  it(`lifecycle methods are optionally implemented`, () => {
    const tree = renderer.create(<SimpleScreen componentId={'123'} />);
    uut.bindScreen(tree.getInstance() as any);

    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidAppear,
      componentId: '123',
      componentName: 'doesnt matter'
    });
    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidDisappear,
      componentId: '123',
      componentName: 'doesnt matter'
    });
  });

  it(`returns unregister fn`, () => {
    const tree = renderer.create(<SimpleScreen componentId={'123'} />);
    const result = uut.bindScreen(tree.getInstance() as any);
    expect((result as any).remove).toBeDefined();
    result.remove();
    uut.onLifecycleEvent({
      type: LifecycleEventType.ComponentDidAppear,
      componentId: '123',
      componentName: 'doesnt matter'
    });
    expect(didAppearFn).not.toHaveBeenCalled();
  });

  it(`registerForAllEvents using nativeEventsReceiver`, () => {
    expect(mockNativeEventsReceiver.registerComponentLifecycleListener).not.toHaveBeenCalled();
    uut.registerForEvents();
    expect(mockNativeEventsReceiver.registerComponentLifecycleListener).toHaveBeenCalledTimes(1);
  });

  it.skip(`supports multiple listeners with same componentId`, () => {
    // TODO
  });
});
