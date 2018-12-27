import * as React from 'react';

import { LayoutType } from './LayoutType';
import { LayoutTreeCrawler, LayoutNode } from './LayoutTreeCrawler';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider.mock';
import { Store } from '../components/Store';
import { mock, instance } from 'ts-mockito';
import { OptionsProcessor } from './OptionsProcessor';

describe('LayoutTreeCrawler', () => {
  let uut: LayoutTreeCrawler;
  let store: Store;
  let mockedOptionsProcessor: OptionsProcessor;

  beforeEach(() => {
    store = new Store();
    mockedOptionsProcessor = mock(OptionsProcessor);
    uut = new LayoutTreeCrawler(new UniqueIdProvider(), store, instance(mockedOptionsProcessor));
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
      children: [{ id: 'user defined id for child', type: LayoutType.BottomTabs, data: {}, children: [] }],
      data: {}
    };
    uut.crawl(node);
    expect(node.id).toEqual('user defined id');
    expect(node.children[0].id).toEqual('user defined id for child');
  });

  it('crawls a layout tree and ensures data exists', () => {
    const node = {
      type: LayoutType.Stack,
      children: [{ type: LayoutType.BottomTabs, data: {}, children: [] }],
      data: {}
    };
    uut.crawl(node);
    expect(node.data).toEqual({});
    expect(node.children[0].data).toEqual({});
  });

  it('crawls a layout tree and ensures children exists', () => {
    const node = {
      type: LayoutType.Stack,
      children: [{ type: LayoutType.BottomTabs, data: {}, children: [] }],
      data: {}
    };
    uut.crawl(node);
    expect(node.children[0].children).toEqual([]);
  });

  it('saves passProps into store for Component nodes', () => {
    const node = {
      type: LayoutType.BottomTabs,
      children: [
        {
          type: LayoutType.Component,
          data: { name: 'the name', passProps: { myProp: 123 } },
          children: []
        }
      ],
      data: {}
    };
    expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual({});
    uut.crawl(node);
    expect(store.getPropsForId('Component+UNIQUE_ID')).toEqual({ myProp: 123 });
  });

  it('Components: injects options from original component class static property', () => {
    const theStyle = {};
    const MyComponent = class CoolComponent extends React.Component {
      static get options() {
        return theStyle;
      }
    };

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual(theStyle);
  });

  it('Components: crawl does not cache options', () => {
    const optionsWithTitle = (title?: string) => {
      return {
        topBar: {
          title: {
            text: title
          }
        }
      };
    };

    const MyComponent = class CoolComponent extends React.Component {
      static options(props: { title: string }) {
        return {
          topBar: {
            title: {
              text: props.title
            }
          }
        };
      }
    };

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {}, passProps: { title: 'title' } },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual(optionsWithTitle('title'));

    const node2 = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    uut.crawl(node2);
    expect(node2.data.options).toEqual(optionsWithTitle(undefined));
  });

  it('Components: passes passProps to the static options function to be used by the user', () => {
    const MyComponent = class CoolComponent extends React.Component {
      static options(passProps: { bar: { baz: { value: string } } }) {
        return { foo: passProps.bar.baz.value };
      }
    };

    const node = {
      type: LayoutType.Component,
      data: {
        name: 'theComponentName',
        passProps: { bar: { baz: { value: 'hello' } } },
        options: {}
      },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual({ foo: 'hello' });
  });

  it('Components: passProps in the static options is optional', () => {
    const MyComponent = class CoolComponent extends React.Component {
      static options(passProps: string) {
        return { foo: passProps };
      }
    };

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual({ foo: {} });
  });

  it('Components: merges options from component class static property with passed options, favoring passed options', () => {
    const theStyle = {
      bazz: 123,
      inner: {
        foo: 'bar'
      },
      opt: 'exists only in static'
    };
    const MyComponent = class CoolComponent extends React.Component {
      static get options() {
        return theStyle;
      }
    };

    const passedOptions = {
      aaa: 'exists only in passed',
      bazz: 789,
      inner: {
        foo: 'this is overriden'
      }
    };

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: passedOptions },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);

    uut.crawl(node);

    expect(node.data.options).toEqual({
      aaa: 'exists only in passed',
      bazz: 789,
      inner: {
        foo: 'this is overriden'
      },
      opt: 'exists only in static'
    });
  });

  it('Component: deepClones options', () => {
    const theStyle = {};
    const MyComponent = class CoolComponent extends React.Component {
      static get options() {
        return theStyle;
      }
    };

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).not.toBe(theStyle);
  });

  it('Components: must contain data name', () => {
    const node = { type: LayoutType.Component, data: {}, children: [] };
    expect(() => uut.crawl(node)).toThrowError('Missing component data.name');
  });

  it('Components: options default obj', () => {
    const MyComponent = class extends React.Component {};

    const node = {
      type: LayoutType.Component,
      data: { name: 'theComponentName', options: {} },
      children: []
    };
    store.setComponentClassForName('theComponentName', () => MyComponent);
    uut.crawl(node);
    expect(node.data.options).toEqual({});
  });

  it('Components: omits passProps after processing so they are not passed over the bridge', () => {
    const node = {
      type: LayoutType.Component,
      data: {
        name: 'compName',
        passProps: {}
      },
      children: []
    };
    uut.crawl(node);
    expect(node.data.passProps).toBeUndefined();
  });

  describe('LayoutNode', () => {
    it('convertable from same data structure', () => {
      const x = {
        id: 'theId',
        type: LayoutType.Component,
        data: {},
        children: []
      };

      let got;
      function expectingLayoutNode(param: LayoutNode) {
        got = param;
      }
      expectingLayoutNode(x);

      expect(got).toBe(x);
    });
  });
});
