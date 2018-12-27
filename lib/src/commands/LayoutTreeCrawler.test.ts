import * as React from 'react';

import { LayoutType } from './LayoutType';
import { LayoutTreeCrawler, LayoutNode } from './LayoutTreeCrawler';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { mock, instance, verify, deepEqual, when, anyString } from 'ts-mockito';
import { OptionsProcessor } from './OptionsProcessor';
import { Options } from '../interfaces/Options';

describe('LayoutTreeCrawler', () => {
  let uut: LayoutTreeCrawler;
  let mockedUniqueIdProvider: UniqueIdProvider;
  let mockedStore: Store;
  let mockedOptionsProcessor: OptionsProcessor;

  beforeEach(() => {
    mockedUniqueIdProvider = mock(UniqueIdProvider);
    mockedStore = mock(Store);
    mockedOptionsProcessor = mock(OptionsProcessor);

    when(mockedUniqueIdProvider.generate(anyString())).thenCall((prefix) => `${prefix}+UNIQUE_ID`);

    uut = new LayoutTreeCrawler(
      instance(mockedUniqueIdProvider),
      instance(mockedStore),
      instance(mockedOptionsProcessor)
    );
  });

  it('crawls a layout tree and adds unique id to each node', () => {
    const node: LayoutNode = {
      type: LayoutType.Stack,
      children: [{ type: LayoutType.BottomTabs, data: {}, children: [] }],
      data: {}
    };
    uut.crawl(node);
    expect(node.id).toEqual('Stack+UNIQUE_ID');
    expect(node.children[0].id).toEqual('BottomTabs+UNIQUE_ID');
  });

  it('does not generate unique id when already provided', () => {
    const node: LayoutNode = {
      id: 'user defined id',
      type: LayoutType.Stack,
      children: [
        { id: 'user defined id for child', type: LayoutType.BottomTabs, data: {}, children: [] }
      ],
      data: {}
    };
    uut.crawl(node);
    expect(node.id).toEqual('user defined id');
    expect(node.children[0].id).toEqual('user defined id for child');
  });

  it('saves passProps into store for Component nodes', () => {
    const node = {
      type: LayoutType.BottomTabs,
      children: [
        {
          id: 'testId',
          type: LayoutType.Component,
          data: { name: 'the name', passProps: { myProp: 123 } },
          children: []
        }
      ],
      data: {}
    };
    uut.crawl(node);
    verify(mockedStore.setPropsForId('testId', deepEqual({ myProp: 123 }))).called();
  });

  it('Components: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(): Options {
            return { popGesture: true };
          }
        }
    );
    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({ popGesture: true });
  });

  it('Components: crawl does not cache options', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options(props: { title?: string }) {
            return { topBar: { title: { text: props.title } } };
          }
        }
    );
    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {}, passProps: { title: 'title' } },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({ topBar: { title: { text: 'title' } } });

    const node2 = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node2);
    expect(node2.data.options).toEqual({ topBar: { title: {} } });
  });

  // it('Components: passProps in the static options is optional', () => {
  //   const MyComponent = class CoolComponent extends React.Component {
  //     static options(passProps: string) {
  //       return { foo: passProps };
  //     }
  //   };

  //   const node = {
  //     type: LayoutType.Component,
  //     data: { name: 'theComponentName', options: {} },
  //     children: []
  //   };
  //   store.setComponentClassForName('theComponentName', () => MyComponent);
  //   uut.crawl(node);
  //   expect(node.data.options).toEqual({ foo: {} });
  // });

  it('Components: merges options from component class static property with passed options, favoring passed options', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options() {
            return {
              bazz: 123,
              inner: { foo: 'this gets overriden' },
              opt: 'exists only in static'
            };
          }
        }
    );

    const node = {
      type: LayoutType.Component,
      data: {
        name: 'theComponentName',
        options: {
          aaa: 'exists only in passed',
          bazz: 789,
          inner: { foo: 'this should override same keys' }
        }
      },
      children: []
    };

    uut.crawl(node);

    expect(node.data.options).toEqual({
      aaa: 'exists only in passed',
      bazz: 789,
      inner: { foo: 'this should override same keys' },
      opt: 'exists only in static'
    });
  });

  // it('Component: deepClones options', () => {
  //   const theStyle = {};
  //   const MyComponent = class CoolComponent extends React.Component {
  //     static options() {
  //       return theStyle;
  //     }
  //   };

  //   const node = {
  //     type: LayoutType.Component,
  //     data: { name: 'theComponentName', options: {} },
  //     children: []
  //   };
  //   store.setComponentClassForName('theComponentName', () => MyComponent);
  //   uut.crawl(node);
  //   expect(node.data.options).not.toBe(theStyle);
  // });

  it('Components: must contain data name', () => {
    const node = { type: LayoutType.Component, data: {}, children: [] };
    expect(() => uut.crawl(node)).toThrowError('Missing component data.name');
  });

  it('Components: options default obj', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () => class extends React.Component {}
    );

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({});
  });

  it('Components: omits passProps after processing so they are not passed over the bridge', () => {
    const node = {
      type: LayoutType.Component,
      data: {
        name: 'compName',
        passProps: { someProp: 'here' }
      },
      children: []
    };
    uut.crawl(node);
    expect(node.data.passProps).toBeUndefined();
  });
});
