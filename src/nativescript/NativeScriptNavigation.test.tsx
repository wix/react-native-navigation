jest.mock(
  '@nativescript/react-native',
  () => ({
    defineUIViewController: jest.fn(() => function NativeScriptViewController({children}: any) {
      return children ?? null;
    }),
  }),
  {virtual: true}
);

import { createNativeScriptNavigation } from './index';
import { getNativeScriptNavigationStore } from './NativeScriptNavigationSurface';

describe('createNativeScriptNavigation', () => {
  it('creates a Navigation surface backed by the NativeScript command sender', async () => {
    const { Navigation, NativeScriptNavigationRoot } = createNativeScriptNavigation();

    Navigation.registerComponent('com.example.Root', () => () => null);
    const result = await Navigation.setRoot({
      root: { component: { name: 'com.example.Root', id: 'rootComponent' } },
    });

    expect(result).toBe('rootComponent');
    expect(typeof NativeScriptNavigationRoot).toBe('function');
    expect(getNativeScriptNavigationStore().getState().snapshot.root).toEqual({
      type: 'Component',
      id: 'rootComponent',
      children: [],
      data: { name: 'com.example.Root', options: {}, passProps: undefined },
    });
  });

  it('pushes onto the parent stack when called with a child component id', async () => {
    const { Navigation } = createNativeScriptNavigation();

    Navigation.registerComponent('com.example.Root', () => () => null);
    Navigation.registerComponent('com.example.Detail', () => () => null);
    await Navigation.setRoot({
      root: {
        stack: {
          id: 'stack',
          children: [
            {
              component: {
                id: 'rootComponent',
                name: 'com.example.Root',
              },
            },
          ],
        },
      },
    });

    jest.useFakeTimers();
    const push = Navigation.push('rootComponent', {
      component: {
        id: 'detailComponent',
        name: 'com.example.Detail',
      },
    });
    jest.advanceTimersByTime(360);
    await push;
    jest.useRealTimers();

    expect(getNativeScriptNavigationStore().getState().snapshot.root).toEqual({
      type: 'Stack',
      id: 'stack',
      children: [
        {
          type: 'Component',
          id: 'rootComponent',
          children: [],
          data: { name: 'com.example.Root', options: {}, passProps: undefined },
        },
        {
          type: 'Component',
          id: 'detailComponent',
          children: [],
          data: { name: 'com.example.Detail', options: {}, passProps: undefined },
        },
      ],
      data: { options: undefined },
    });
  });

  it('dedupes rapid pushes while a stack transition is pending', async () => {
    const { Navigation } = createNativeScriptNavigation();

    Navigation.registerComponent('com.example.Root', () => () => null);
    Navigation.registerComponent('com.example.Detail', () => () => null);
    await Navigation.setRoot({
      root: {
        stack: {
          id: 'stack',
          children: [
            {
              component: {
                id: 'rootComponent',
                name: 'com.example.Root',
              },
            },
          ],
        },
      },
    });

    jest.useFakeTimers();
    const firstPush = Navigation.push('rootComponent', {
      component: {
        id: 'detailOne',
        name: 'com.example.Detail',
      },
    });
    const secondPush = Navigation.push('rootComponent', {
      component: {
        id: 'detailTwo',
        name: 'com.example.Detail',
      },
    });

    const children =
      getNativeScriptNavigationStore().getState().snapshot.root?.children ?? [];
    expect(children.map((child) => child.id)).toEqual(['rootComponent', 'detailOne']);

    jest.advanceTimersByTime(360);
    await expect(firstPush).resolves.toBe('detailOne');
    await expect(secondPush).resolves.toBe('detailOne');
    jest.useRealTimers();

    const finalChildren =
      getNativeScriptNavigationStore().getState().snapshot.root?.children ?? [];
    expect(finalChildren.map((child) => child.id)).toEqual(['rootComponent', 'detailOne']);
  });

  it('queues opposite stack commands while a transition is pending', async () => {
    const { Navigation } = createNativeScriptNavigation();

    Navigation.registerComponent('com.example.Root', () => () => null);
    Navigation.registerComponent('com.example.Detail', () => () => null);
    await Navigation.setRoot({
      root: {
        stack: {
          id: 'stack',
          children: [
            {
              component: {
                id: 'rootComponent',
                name: 'com.example.Root',
              },
            },
          ],
        },
      },
    });

    jest.useFakeTimers();
    const push = Navigation.push('rootComponent', {
      component: {
        id: 'detailComponent',
        name: 'com.example.Detail',
      },
    });
    const pop = Navigation.pop('detailComponent');

    expect(
      getNativeScriptNavigationStore().getState().snapshot.root?.children?.map((child) => child.id)
    ).toEqual(['rootComponent', 'detailComponent']);

    jest.advanceTimersByTime(360);
    await expect(push).resolves.toBe('detailComponent');
    await Promise.resolve();
    await Promise.resolve();

    expect(
      getNativeScriptNavigationStore().getState().snapshot.root?.children?.map((child) => child.id)
    ).toEqual(['rootComponent']);

    jest.advanceTimersByTime(360);
    await expect(pop).resolves.toBe('detailComponent');
    jest.useRealTimers();
  });

  it('syncs native back changes into the JS stack snapshot', async () => {
    const { Navigation } = createNativeScriptNavigation();

    Navigation.registerComponent('com.example.Root', () => () => null);
    Navigation.registerComponent('com.example.Detail', () => () => null);
    await Navigation.setRoot({
      root: {
        stack: {
          id: 'stack',
          children: [
            {
              component: {
                id: 'rootComponent',
                name: 'com.example.Root',
              },
            },
            {
              component: {
                id: 'detailComponent',
                name: 'com.example.Detail',
              },
            },
          ],
        },
      },
    });

    const removed = getNativeScriptNavigationStore().syncStackChildren('stack', [
      'rootComponent',
    ]);

    expect(removed).toEqual(['detailComponent']);
    expect(
      getNativeScriptNavigationStore().getState().snapshot.root?.children?.map((child) => child.id)
    ).toEqual(['rootComponent']);
  });
});
