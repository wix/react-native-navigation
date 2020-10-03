import keys from 'lodash/keys';
import { LayoutTreeParser } from './LayoutTreeParser';
import { LayoutType } from './LayoutType';
import { Options } from '../interfaces/Options';
import { Layout } from '../interfaces/Layout';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { mock, instance, when, anything } from 'ts-mockito';

describe('LayoutTreeParser', () => {
  let uut: LayoutTreeParser;
  let mockedUniqueIdProvider: UniqueIdProvider;

  beforeEach(() => {
    mockedUniqueIdProvider = mock(UniqueIdProvider);
    when(mockedUniqueIdProvider.generate(anything())).thenReturn('myUniqueId');
    uut = new LayoutTreeParser(instance(mockedUniqueIdProvider));
  });

  describe('parses into { type, data, children }', () => {
    it('unknown type', () => {
      expect(() => uut.parse({ wut: {} } as Layout)).toThrowError('unknown LayoutType "wut"');
    });

    it('single component', () => {
      expect(uut.parse(LayoutExamples.singleComponent)).toEqual({
        id: 'myUniqueId',
        type: LayoutType.Component,
        data: {
          name: 'MyReactComponent',
          options: LayoutExamples.options,
          passProps: LayoutExamples.passProps,
        },
        children: [],
      });
    });

    it('external component', () => {
      expect(uut.parse(LayoutExamples.externalComponent)).toEqual({
        id: 'myUniqueId',
        type: LayoutType.ExternalComponent,
        data: {
          name: 'MyReactComponent',
          options: LayoutExamples.options,
          passProps: LayoutExamples.passProps,
        },
        children: [],
      });
    });

    it('pass props', () => {
      const result = uut.parse({
        component: {
          name: 'MyScreen',
          passProps: LayoutExamples.passProps,
        },
      });
      expect(result).toEqual({
        id: 'myUniqueId',
        type: LayoutType.Component,
        data: { name: 'MyScreen', passProps: LayoutExamples.passProps },
        children: [],
      });
      expect(result.data.passProps).toBe(LayoutExamples.passProps);
    });

    it('stack of components with top bar', () => {
      expect(uut.parse(LayoutExamples.stackWithTopBar)).toEqual({
        id: 'myUniqueId',
        type: LayoutType.Stack,
        data: {
          options: LayoutExamples.options,
        },
        children: [
          {
            id: 'myUniqueId',
            type: LayoutType.Component,
            data: { name: 'MyReactComponent1' },
            children: [],
          },
          {
            id: 'myUniqueId',
            type: LayoutType.Component,
            data: { name: 'MyReactComponent2', options: LayoutExamples.options },
            children: [],
          },
        ],
      });
    });

    it('bottom tabs', () => {
      const result = uut.parse(LayoutExamples.bottomTabs);
      expect(keys(result)).toEqual(['id', 'type', 'data', 'children']);
      expect(result.id).toEqual('myUniqueId');
      expect(result.type).toEqual(LayoutType.BottomTabs);
      expect(result.data).toEqual({});
      expect(result.children.length).toEqual(3);
      expect(result.children[0].type).toEqual(LayoutType.Stack);
      expect(result.children[1].type).toEqual(LayoutType.Stack);
      expect(result.children[2].type).toEqual(LayoutType.Component);
    });

    it('side menus', () => {
      const result = uut.parse(LayoutExamples.sideMenu);
      expect(keys(result)).toEqual(['id', 'type', 'data', 'children']);
      expect(result.id).toEqual('myUniqueId');
      expect(result.type).toEqual(LayoutType.SideMenuRoot);
      expect(result.data).toEqual({});
      expect(result.children.length).toEqual(3);
      expect(result.children[0].type).toEqual(LayoutType.SideMenuLeft);
      expect(result.children[1].type).toEqual(LayoutType.SideMenuCenter);
      expect(result.children[2].type).toEqual(LayoutType.SideMenuRight);
      expect(result.children[0].children.length).toEqual(1);
      expect(result.children[0].children[0].type).toEqual(LayoutType.Component);
      expect(result.children[1].children.length).toEqual(1);
      expect(result.children[1].children[0].type).toEqual(LayoutType.Stack);
      expect(result.children[2].children.length).toEqual(1);
      expect(result.children[2].children[0].type).toEqual(LayoutType.Component);
    });

    it('top tabs', () => {
      const result = uut.parse(LayoutExamples.topTabs);
      expect(keys(result)).toEqual(['id', 'type', 'data', 'children']);
      expect(result.id).toEqual('myUniqueId');
      expect(result.type).toEqual(LayoutType.TopTabs);
      expect(result.data).toEqual({ options: LayoutExamples.options });
      expect(result.children.length).toEqual(5);
      expect(result.children[0].type).toEqual(LayoutType.Component);
      expect(result.children[1].type).toEqual(LayoutType.Component);
      expect(result.children[2].type).toEqual(LayoutType.Component);
      expect(result.children[3].type).toEqual(LayoutType.Component);
      expect(result.children[4].type).toEqual(LayoutType.Stack);
    });

    it('complex layout example', () => {
      const result = uut.parse(LayoutExamples.complexLayout);
      expect(result.type).toEqual('SideMenuRoot');
      expect(result.children[1].type).toEqual('SideMenuCenter');
      expect(result.children[1].children[0].type).toEqual('BottomTabs');
      expect(result.children[1].children[0].children[2].type).toEqual('Stack');
    });

    it('split view', () => {
      const result = uut.parse(LayoutExamples.splitView);
      const master = uut.parse(LayoutExamples.splitView.splitView!.master!);
      const detail = uut.parse(LayoutExamples.splitView.splitView!.detail!);

      expect(result.type).toEqual('SplitView');
      expect(result.children[0]).toEqual(master);
      expect(result.children[1]).toEqual(detail);
    });
  });

  it('options for all containing types', () => {
    expect(uut.parse({ component: { name: 'lol', options } }).data.options).toBe(options);
    expect(uut.parse({ stack: { options } }).data.options).toBe(options);
    expect(uut.parse({ bottomTabs: { options } }).data.options).toBe(options);
    expect(uut.parse({ topTabs: { options } }).data.options).toBe(options);
    expect(
      uut.parse({ sideMenu: { options, center: { component: { name: 'lool' } } } }).data.options
    ).toBe(options);
    expect(uut.parse(LayoutExamples.splitView).data.options).toBe(optionsSplitView);
  });

  it('pass user provided id as is', () => {
    const component = { id: 'compId', name: 'loool' };
    expect(uut.parse({ component }).id).toEqual('compId');
    expect(uut.parse({ stack: { id: 'stackId' } }).id).toEqual('stackId');
    expect(uut.parse({ stack: { children: [{ component }] } }).children[0].id).toEqual('compId');
    expect(uut.parse({ bottomTabs: { id: 'myId' } }).id).toEqual('myId');
    expect(uut.parse({ bottomTabs: { children: [{ component }] } }).children[0].id).toEqual(
      'compId'
    );
    expect(uut.parse({ topTabs: { id: 'myId' } }).id).toEqual('myId');
    expect(uut.parse({ topTabs: { children: [{ component }] } }).children[0].id).toEqual('compId');
    expect(uut.parse({ sideMenu: { id: 'myId', center: { component } } }).id).toEqual('myId');
  });
});

/* Layout Examples: */

const passProps = {
  strProp: 'string prop',
  numProp: 12345,
  objProp: { inner: { foo: 'bar' } },
  fnProp: () => 'Hello from a function',
};

const options: Options = {
  topBar: {
    title: {
      text: 'Hello1',
    },
  },
};

const optionsSplitView: Options = {
  topBar: {
    title: {
      text: 'Hello1',
    },
  },
  splitView: {
    displayMode: 'auto',
    primaryEdge: 'leading',
    minWidth: 150,
    maxWidth: 300,
    primaryBackgroundStyle: 'sidebar',
  },
};

const singleComponent = {
  component: {
    name: 'MyReactComponent',
    options,
    passProps,
  },
};

const externalComponent = {
  externalComponent: {
    name: 'MyReactComponent',
    options,
    passProps,
  },
};

const stackWithTopBar = {
  stack: {
    children: [
      {
        component: {
          name: 'MyReactComponent1',
        },
      },
      {
        component: {
          name: 'MyReactComponent2',
          options,
        },
      },
    ],
    options,
  },
};

const bottomTabs = {
  bottomTabs: {
    children: [
      stackWithTopBar,
      stackWithTopBar,
      {
        component: {
          name: 'MyReactComponent1',
        },
      },
    ],
  },
};

const sideMenu = {
  sideMenu: {
    left: singleComponent,
    center: stackWithTopBar,
    right: singleComponent,
  },
};

const topTabs = {
  topTabs: {
    children: [singleComponent, singleComponent, singleComponent, singleComponent, stackWithTopBar],
    options,
  },
};

const complexLayout: Layout = {
  sideMenu: {
    left: singleComponent,
    center: {
      bottomTabs: {
        children: [
          stackWithTopBar,
          stackWithTopBar,
          {
            stack: {
              children: [singleComponent, singleComponent, singleComponent, singleComponent],
            },
          },
        ],
      },
    },
  },
};

const splitView: Layout = {
  splitView: {
    master: {
      stack: {
        children: [singleComponent],
        options,
      },
    },
    detail: stackWithTopBar,
    options: optionsSplitView,
  },
};

const LayoutExamples = {
  passProps,
  options,
  singleComponent,
  stackWithTopBar,
  bottomTabs,
  sideMenu,
  topTabs,
  complexLayout,
  externalComponent,
  splitView,
};
