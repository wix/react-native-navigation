import * as React from 'react';
import * as renderer from 'react-test-renderer';
import { ComponentEventsObserver } from './ComponentEventsObserver';

describe('ComponentEventsObserver', () => {
  const uut = new ComponentEventsObserver();
  const didAppearFn = jest.fn();
  const didDisappearFn = jest.fn();
  const didMountFn = jest.fn();
  const willUnmountFn = jest.fn();

  class SimpleScreen extends React.Component<any, any> {
    render() {
      return 'Hello';
    }
  }

  class BoundScreen extends React.Component<any, any> {
    constructor(props) {
      super(props);
      uut.bindComponent(this);
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

  it(`bindComponent expects a component with componentId`, () => {
    const tree = renderer.create(<SimpleScreen />);
    expect(() => uut.bindComponent(tree.getInstance() as any)).toThrow('');
    const tree2 = renderer.create(<SimpleScreen componentId={123} />);
    expect(() => uut.bindComponent(tree2.getInstance() as any)).toThrow('');
  });

  it(`bindComponent notifies listeners by componentId on lifecycle events`, () => {
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
    const tree = renderer.create(<SimpleScreen componentId={'123'} />);
    const instance = tree.getInstance() as any;
    instance.componentDidAppear = jest.fn();

    const result = uut.bindComponent(instance);

    uut.notifyComponentDidAppear({ componentId: '123', componentName: 'doesnt matter' });
    expect(instance.componentDidAppear).toHaveBeenCalledTimes(1);

    expect(result.remove).toBeDefined();
    result.remove();

    uut.notifyComponentDidAppear({ componentId: '123', componentName: 'doesnt matter' });
    expect(instance.componentDidAppear).toHaveBeenCalledTimes(1);
  });

  it.skip(`supports multiple listeners with same componentId`, () => {
    // TODO
  });
});
