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

    await Navigation.push('rootComponent', {
      component: {
        id: 'detailComponent',
        name: 'com.example.Detail',
      },
    });

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
});
