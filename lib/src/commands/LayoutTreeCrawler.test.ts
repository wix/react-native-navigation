import * as React from 'react';

import { LayoutType } from './LayoutType';
import { LayoutTreeCrawler } from './LayoutTreeCrawler';
import { Store } from '../components/Store';
import { mock, instance, verify, deepEqual, when } from 'ts-mockito';
import { OptionsProcessor } from './OptionsProcessor';
import { Options } from '../interfaces/Options';

describe('LayoutTreeCrawler', () => {
  let uut: LayoutTreeCrawler;
  let mockedStore: Store;
  let mockedOptionsProcessor: OptionsProcessor;

  beforeEach(() => {
    mockedStore = mock(Store);
    mockedOptionsProcessor = mock(OptionsProcessor);

    uut = new LayoutTreeCrawler(instance(mockedStore), instance(mockedOptionsProcessor));
  });

  it('saves passProps into store for Component nodes', () => {
    const node = {
      id: 'testId',
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
    verify(mockedStore.updateProps('testId', deepEqual({ myProp: 123 }))).called();
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
      id: 'testId',
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({ popGesture: true });
  });

  it('Components: injects options from original component class static property', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () =>
        class extends React.Component {
          static options = {
            popGesture: true
          };
        }
    );
    const node = {
      id: 'testId',
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
      id: 'testId',
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {}, passProps: { title: 'title' } },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({ topBar: { title: { text: 'title' } } });

    const node2 = {
      id: 'testId',
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node2);
    expect(node2.data.options).toEqual({ topBar: { title: {} } });
  });

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
      id: 'testId',
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

  it('Components: must contain data name', () => {
    const node = { type: LayoutType.Component, data: {}, children: [], id: 'testId' };
    expect(() => uut.crawl(node)).toThrowError('Missing component data.name');
  });

  it('Components: options default obj', () => {
    when(mockedStore.getComponentClassForName('theComponentName')).thenReturn(
      () => class extends React.Component {}
    );

    const node = {
      id: 'testId',
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node);
    expect(node.data.options).toEqual({});
  });

  it('Components: omits passProps after processing so they are not passed over the bridge', () => {
    const node = {
      id: 'testId',
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
